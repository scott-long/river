package org.scottlong.river.resource;

import java.io.IOException;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.apache.log4j.Logger;
import org.jboss.resteasy.annotations.providers.jaxb.Formatted;
import org.scottlong.river.domain.Measurements;
import org.scottlong.river.domain.Station;
import org.scottlong.river.domain.Stations;

@Path("/")
public class StationResource {

	private final static Logger logger = Logger.getLogger(StationResource.class);

	@GET
	@Path("/station/{stationId}")
	@Produces("application/xml")
	@Formatted
	public Station getStation(@PathParam("stationId") String stationId, @QueryParam("days") int days) throws IOException {
		if (logger.isDebugEnabled())
			logger.debug("getStation: stationId=" + stationId + " days=" + days);
		
		if ((days > 7) || (days < 1)) {
			days = 1;
		}

		Stations stations = new StationListHandler().getStations("usa");

		if (stations != null) {
			if ((stations.getStations() != null) && (stationId != null) && (stationId.length() > 0)) {
				for (Station s : stations.getStations()) {
					if (s.getStationId().equalsIgnoreCase(stationId)) {
						Measurements m = new MeasurementListHandler().getMeasurements(stationId, days);
						s.setMeasurements(m);
						return s;
					}
				}
			}
		}

		return null;
	}

	@GET
	@Path("/stations/{state}")
	@Produces("application/xml")
	@Formatted
	public Stations getStations(@PathParam("state") String state, @QueryParam("miles") double miles,
	        @QueryParam("latitude") double latitude, @QueryParam("longitude") double longitude) throws IOException {
		if (logger.isDebugEnabled())
			logger.debug("getStations: state=" + state + " miles=" + miles + " latitude=" + latitude
			        + " longitude=" + longitude);

		Stations stations = null;
		if (miles != 0.0 && latitude != 0.0 && longitude != 0.0) {
			stations = new StationListHandler().getStationsByLocation(miles, latitude, longitude);
		}
		else {
			stations = new StationListHandler().getStations(state);
		}

		List<Station> list = stations.getStations();
		if (logger.isDebugEnabled())
			logger.debug("getStations: " + (list == null ? 0 : list.size()));

		return stations;
	}
}
