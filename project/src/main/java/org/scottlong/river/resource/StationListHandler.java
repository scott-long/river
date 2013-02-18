package org.scottlong.river.resource;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.scottlong.river.domain.Station;
import org.scottlong.river.domain.Stations;
import org.scottlong.river.domain.UsState;
import org.scottlong.util.StringUtil;

public class StationListHandler {

	private final static Logger logger = Logger.getLogger(StationListHandler.class);

	private static Map<UsState, Stations> cache = new HashMap<UsState, Stations>();

	public StationListHandler() {
	}

	public Stations getStationsByLocation(double miles, double latitude, double longitude) throws IOException {
		if (logger.isDebugEnabled())
			logger.debug("getStationsByLocation: miles=" + miles + " latitude=" + latitude + " longitude="
			        + longitude);

		Stations stations = getStations(UsState.USA.name());

		List<Station> list = stations.getStations();
		List<Station> sublist = new ArrayList<Station>();

		if (list != null) {
			for (Station station : list) {
				if (station.inRange(miles, latitude, longitude)) {
					sublist.add(station);
				}
			}

			stations = new Stations();
			stations.setStations(sublist);

		}

		return stations;
	}

	public Stations getStations(String stateCode) throws IOException {

		if (logger.isDebugEnabled())
			logger.debug("getStations: state=" + stateCode);

		UsState state = UsState.forCode(stateCode);
		if (state == null)
			return null;

		Stations cached = cache.get(state);

		if (cached != null) {
			long age = new Date().getTime() - cached.getLoaded().getTime();
			if (age < (1000 * 60 * 15)) {
				if (logger.isTraceEnabled())
					logger.trace("getStations: cache hit");
				return cached;
			}
		}

		String url = "http://nwis.waterdata.usgs.gov/nwis/current";

		int i = 0;
		url += "?index_pmcode_" + Parameter.Celcius.getCode() + "=" + i++;
		url += "&index_pmcode_" + Parameter.Discharge.getCode() + "=" + i++;
		url += "&index_pmcode_" + Parameter.Fahrenheit.getCode() + "=" + i++;
		url += "&index_pmcode_" + Parameter.GageHeight.getCode() + "=" + i++;
		url += "&index_pmcode_" + Parameter.Turbidity.getCode() + "=" + i++;

		url += "&sort_key=site_no";
		url += "&group_key=NONE";

		// We want sites, not realtime data
		url += "&format=sitefile_output";
		url += "&sitefile_output_format=rdb";
		
		url += "&column_name=" + Parameter.DecimalLatitude.getCode();
		url += "&column_name=" + Parameter.DecimalLongitude.getCode();
		url += "&column_name=" + Parameter.SiteNumber.getCode();
		url += "&column_name=" + Parameter.StateCode.getCode();
		url += "&column_name=" + Parameter.StationName.getCode();
		url += "&sort_key_2=site_no"; // ? 

		url += "&html_table_group_key=NONE";
		url += "&rdb_compression=file";

		// We want to choose sites based on which realtime data they have
		// available
		url += "&list_of_search_criteria=realtime_parameter_selection";

		if (logger.isDebugEnabled())
			logger.debug("getStatons: url=" + url);

		InputStream is = null;

		try {
			is = (InputStream) new URL(url).getContent();
		}

		catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}

		// Convert to Objects
		Stations stations = loadStations(is, state);
		
		if (!stations.getStations().isEmpty()) {
			cache.remove(state);
			cache.put(state, stations);
		}
		else if ((cached != null) && !cached.getStations().isEmpty()) {
			logger.warn("getStations: returning expired cached value because we got no data");
			stations = cached;
		}

		return stations;
	}

	private Stations loadStations(InputStream is, final UsState targetState) throws IOException {

		UsgsResponseHandler<Station> rh = new UsgsResponseHandler<Station>(is) {

			// Override to convert a row of data to an object
			Station getItem(List<String> fields) {
				try {
					String id = getString(fields, Parameter.StateCode);
					UsState state = null;

					if (!StringUtil.isEmpty(id)) {
						state = UsState.forId(Integer.parseInt(id));
					}

					if ((targetState != null) && (targetState != UsState.USA) && (state != targetState)) {
						return null;
					}

					Station s = new Station();
					s.setStateEnum(state);
					s.setAgencyCode(getString(fields, Parameter.AgencyCode));
					s.setStationId(getString(fields, Parameter.SiteNumber));
					s.setStationName(getString(fields, Parameter.StationName));

					/*
					 * // Try and detect expired sites String s1 =
					 * getString(fields, Parameter.PeakStreamflowDataBeginDate);
					 * String e1 = getString(fields,
					 * Parameter.PeakStreamflowDataEndDate); String s2 =
					 * getString(fields, Parameter.WaterQualityDataBeginDate);
					 * String e2 = getString(fields,
					 * Parameter.WaterQualityDataEndDate); String s3 =
					 * getString(fields, Parameter.SiteVisitDataBeginDate);
					 * String e3 = getString(fields,
					 * Parameter.SiteVisitDataEndDate); String s4 =
					 * getString(fields,
					 * Parameter.FieldWaterLevelMeasurementsBeginDate); String
					 * e4 = getString(fields,
					 * Parameter.FieldWaterLevelMeasurementsEndDate);
					 * 
					 * boolean ok1 = !StringUtil.isEmpty(s1) &&
					 * StringUtil.isEmpty(e1); boolean ok2 =
					 * !StringUtil.isEmpty(s2) && StringUtil.isEmpty(e2);
					 * boolean ok3 = !StringUtil.isEmpty(s3) &&
					 * StringUtil.isEmpty(e3); boolean ok4 =
					 * !StringUtil.isEmpty(s3) && StringUtil.isEmpty(e4);
					 * 
					 * if (!ok1 && !ok2 && !ok3 && !ok4) { if
					 * (logger.isInfoEnabled()) logger.info("discarding " +
					 * s.getStationId() + " " + s.getStationName() + "s1=" + s1
					 * + "e1= " + e1 + " s2=" + s2 + " e2=" + e2 + " s3=" + s3 +
					 * " e3=" + e3 + " s4=" + s4 + " e4=" + e4 ); return null; }
					 */

					s.setLatitude(getString(fields, Parameter.DecimalLatitude));
					s.setLongitude(getString(fields, Parameter.DecimalLongitude));
					return s;
				}

				catch (Throwable t) {
					logger.debug("getItem: " + t + " line=" + getCurrentLineNumber() + " fields=" + fields);
					return null;
				}
			}
		};

		List<Station> stations = rh.getItems();

		// Remove duplicates
		Map<String, Station> m = new HashMap<String, Station>();
		for (Station station : stations) {
			m.put(station.getStationId(), station);
		}

		stations.clear();
		stations.addAll(m.values());

		// Sort by state then name
		Collections.sort(stations, new Comparator<Station>() {
			public int compare(Station s1, Station s2) {
				String x1 = s1.getStateEnum() == null ? "" : s1.getStateEnum().name();
				String x2 = s2.getStateEnum() == null ? "" : s2.getStateEnum().name();
				int x = x1.compareTo(x2);
				if (x != 0)
					return x;
				x1 = s1.getStationName() == null ? "" : s1.getStationName();
				x2 = s2.getStationName() == null ? "" : s2.getStationName();
				return x1.compareTo(x2);
			}
		});

		return new Stations(stations);
	}
}
