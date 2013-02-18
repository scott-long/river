package org.scottlong.river.resource;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.scottlong.util.StringUtil;

public abstract class UsgsResponseHandler<T> {

	private final static Logger logger = Logger.getLogger(UsgsResponseHandler.class);

	int currentLineNumber;
	String content;
	Map<Parameter, Integer> indices;

	public UsgsResponseHandler(InputStream is) throws IOException {
		if (logger.isDebugEnabled())
			logger.debug("<init>");

		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		byte[] buf = new byte[16384];
		while (true) {
			int read = is.read(buf);
			if (read <= 0)
				break;
			bytes.write(buf, 0, read);
		}

		this.content = new String(bytes.toByteArray());
		this.indices = null;
	}

	public UsgsResponseHandler(String content) {
		if (logger.isDebugEnabled())
			logger.debug("<init>");

		this.content = content;
		this.indices = null;
	}

	abstract T getItem(List<String> fields);

	public int getCurrentLineNumber() {
		return currentLineNumber;
	}

	protected Map<Parameter, Integer> getIndices() {
		return indices;
	}

	public List<T> getItems() {

		List<T> items = new ArrayList<T>();
		StringReader sr = new StringReader(content);
		BufferedReader br = new BufferedReader(sr);
		indices = null;
		boolean skippedDataTypes = false;
		currentLineNumber = 0;
		int headerColumnCount = 0;

		while (true) {
			String line;
			try {
				line = br.readLine();
			}
			catch (IOException e) {
				throw new RuntimeException(e);
			}

			if (line == null)
				break;

			currentLineNumber++;

			if (line.startsWith("#"))
				continue;

			String[] f = line.split("\\t");
			List<String> fields = new ArrayList<String>(Arrays.asList(f));

			if (indices == null) {
				headerColumnCount = fields.size();
				indices = getParameterIndices(fields);
				if (logger.isDebugEnabled())
					logger.debug("getItems: indices=" + indices);
				continue;
			}

			if (!skippedDataTypes) {
				// Have we skipped over the row that has no data, just data types?
				skippedDataTypes = true;
				continue;
			}
			
			try {
				while (fields.size() < headerColumnCount)
					fields.add(null);
				T item = getItem(fields);
				if (item != null)
					items.add(item);
			}

			catch (Throwable t) {
				logger.debug("getItems: line=" + currentLineNumber + ": " + t);
			}
		}

		if (logger.isDebugEnabled())
			logger.debug("getItems: lines=" + currentLineNumber + " items=" + items.size());

		return items;
	}

	protected Double getDouble(List<String> fields, Parameter p) {
		Double d = null;
		String s = getString(fields, p);
		if (!StringUtil.isEmpty(s)) {
			d = new Double(s.trim());
		}
		return d;
	}

	protected String getString(List<String> fields, Parameter p) {
		String s = null;
		Integer i = indices.get(p);
		if (i != null) {
			if (i >= 0) {
				String x = fields.get(i);
				if (x != null) {
					if (!x.equals("--")) {
						s = x;
					}
				}
			}
		}
		return s;
	}

	private Map<Parameter, Integer> getParameterIndices(List<String> header) {
		Map<Parameter, Integer> m = new HashMap<Parameter, Integer>();
		for (Parameter p : Parameter.values()) {
			m.put(p, new Integer(-1));
			if (header != null) {
				for (int i = 0; i < header.size(); i++) {
					if (header.get(i).endsWith(p.getCode())) {
						m.put(p, new Integer(i));
						break;
					}
				}
			}
		}
		return m;
	}

	Date parseDate(String dt, String tz) throws ParseException {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm z");
		Date d = df.parse(dt + " " + tz);
		return d;
	}

}
