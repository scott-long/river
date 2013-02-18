package org.scottlong.river.resource;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.scottlong.river.domain.Measurement;
import org.scottlong.river.domain.Measurements;

public class MeasurementListHandler {

	private final static Logger logger = Logger.getLogger(MeasurementListHandler.class);

	private static Map<String, Measurements> cache = new HashMap<String, Measurements>();

	// http://waterdata.usgs.gov/nwis/uv?cb_00010=on&cb_00065=on&cb_00060=on&cb_00095=on&cb_63680=on&format=rdb&period=1&site_no=02336000

	public Measurements getMeasurements(String stationId, int days) throws IOException {

		if (logger.isTraceEnabled())
			logger.trace("getMeasurements");

		String key = stationId + "_" + days;

		Measurements cached = cache.get(key);

		if (cached != null) {
			long age = new Date().getTime() - cached.getLoaded().getTime();
			if (age < (1000 * 60 * 15)) {
				if (logger.isTraceEnabled())
					logger.trace("getMeasurements: cache hit");
				return cached;
			}
		}

		// generate URL
		String url = "http://waterdata.usgs.gov/nwis/uv?";

		url += "cb_" + Parameter.Celcius.getCode() + "=on";
		url += "&cb_" + Parameter.Discharge.getCode() + "=on";
		url += "&cb_" + Parameter.Fahrenheit.getCode() + "=on";
		url += "&cb_" + Parameter.GageHeight.getCode() + "=on";
		url += "&cb_" + Parameter.Turbidity.getCode() + "=on";
		url += "&format=rdb";
		url += "&period=" + days;
		url += "&site_no=" + stationId;

		if (logger.isTraceEnabled())
			logger.trace("getMeasurements: url=" + url);

		InputStream is = null;

		try {
			is = (InputStream) new URL(url).getContent();
		}

		catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}

		// Convert to Objects
		Measurements measurements = loadMeasurements(is);

		if (!measurements.getMeasurements().isEmpty()) {
			cache.remove(key);
			cache.put(key, measurements);
		}
		else if ((cached != null) && !cached.getMeasurements().isEmpty()) {
			logger.warn("getStations: returning expired cached value because we got no data");
			measurements = cached;
		}

		return measurements;
	}

	private Measurements loadMeasurements(InputStream is) throws IOException {

		UsgsResponseHandler<Measurement> rh = new UsgsResponseHandler<Measurement>(is) {

			// Override to convert a row of data to an object
			Measurement getItem(List<String> fields) {
				try {
					Measurement m = new Measurement();
					String dt = fields.get(getIndices().get(Parameter.DateTime));
					String tz = fields.get(getIndices().get(Parameter.TimeZone));
					Date d = parseDate(dt, tz);

					m.setTimestamp(d);

					Double c = getDouble(fields, Parameter.Celcius);
					Double f = getDouble(fields, Parameter.Fahrenheit);

					if (c == null) {
						// See if we got fahrenheit
						if (f != null) {
							c = (f - 32.0) * 5.0 / 9.0;
						}
					}

					m.setTemperature(c);
					m.setGageHeight(getDouble(fields, Parameter.GageHeight));
					m.setDischarge(getDouble(fields, Parameter.Discharge));
					m.setConductance(getDouble(fields, Parameter.Conductance));
					m.setTurbidity(getDouble(fields, Parameter.Turbidity));

					return m;
				}
				catch (Throwable t) {
					if (logger.isDebugEnabled())
						logger.error("getItem: line=" + getCurrentLineNumber() + " fields=" + fields, t);
					else
						logger.error("getItem: " + t + " line=" + getCurrentLineNumber() + " fields=" + fields);

					return null;
				}
			}
		};

		List<Measurement> measurements = rh.getItems();
		return new Measurements(measurements);
	}
}
