package org.scottlong.river.domain;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class XmlTimestampAdapter extends XmlAdapter<String, Date> {

	private SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm z");

	public String marshal(Date d) throws Exception {
		if (d == null)
			return null;
		else
			return df.format(d);
	}

	public Date unmarshal(String s) throws Exception {
		if (s == null)
			return null;
		else
			return df.parse(s);
	}

}
