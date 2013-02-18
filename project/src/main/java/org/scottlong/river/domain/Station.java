package org.scottlong.river.domain;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import org.apache.log4j.Logger;
import org.scottlong.util.StringUtil;

@XmlRootElement(name = "station")
@XmlType(propOrder = { "agencyCode", "stationId", "stationName", "state", "latitude", "longitude", "measurements" })
public class Station {

	private final static Logger logger = Logger.getLogger(Station.class);

	private String agencyCode;
	private String stationId;
	private String stationName;
	private String latitude;
	private String longitude;
	private UsState state;
	private Measurements measurements;

	@XmlElement
	public String getAgencyCode() {
		return agencyCode;
	}

	public void setAgencyCode(String agencyCode) {
		this.agencyCode = agencyCode;
	}

	@XmlElement
	public String getStationId() {
		return stationId;
	}

	public void setStationId(String stationId) {
		this.stationId = stationId;
	}

	@XmlElement
	public String getStationName() {
		return stationName;
	}

	public void setStationName(String sn) {

		if (sn != null) {
			sn = trim(sn);
			sn = sn.toLowerCase();
			sn = StringUtil.capitalizePhrase(sn);
		}

		this.stationName = sn;
		trimStateFromName();
	}

	@XmlElement
	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	@XmlElement
	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	@XmlElement
	public String getState() {
		return state == null ? null : state.name();
	}

	public void setState(String code) {
		UsState uss = UsState.forCode(code);
		setStateEnum(uss);
	}

	@XmlTransient
	public UsState getStateEnum() {
		return state;
	}

	public void setStateEnum(UsState state) {
		this.state = state;
		trimStateFromName();
	}

	@XmlElement
	public Measurements getMeasurements() {
		return measurements;
	}

	public void setMeasurements(Measurements measurements) {
		this.measurements = measurements;
	}

	private void trimStateFromName() {
		if (this.state != null) {
			// See if the station exists and ends with the state name
			if (stationName != null) {
				String sn = this.state.name().toUpperCase();
				if (this.stationName.toUpperCase().endsWith(sn)) {
					this.stationName = this.stationName.substring(0, this.stationName.length() - sn.length())
					        .trim();
					this.stationName = trim(this.stationName);
				}
			}
		}
	}

	private String trim(String s) {
		boolean modified = true;
		while (modified) {
			modified = false;

			if (s.endsWith(",")) {
				s = s.substring(0, s.length() - 1).trim();
				modified = true;
			}

			if (s.endsWith(".")) {
				s = s.substring(0, s.length() - 1).trim();
				modified = true;
			}
		}

		return s;

	}

	@Override
	public String toString() {
		return "Station [agencyCode=" + agencyCode + ", stationId=" + stationId + ", stationName=" + stationName
		        + ", latitude=" + latitude + ", longitude=" + longitude + ", state=" + state + "]";
	}

	public boolean inRange(double miles, double latitude2, double longitude2) {
		try {
			double lat = Double.parseDouble(latitude);
			double lon = Double.parseDouble(longitude);
			double distance = distance(lat, lon, latitude2, longitude2, 'M');
			if (distance < miles)
				return true;
			else
				return false;

		}
		catch (Throwable t) {
			return false;
		}
	}

	private double distance(double lat1, double lon1, double lat2, double lon2, char unit) {

		if (logger.isDebugEnabled())
			logger.debug("distance: lat1=" + lat1 + " lon1=" + lon1 + " lat2=" + lat2 + " lon2=" + lon2);

		double theta = lon1 - lon2;
		double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1))
		        * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
		dist = Math.acos(dist);
		dist = rad2deg(dist);
		dist = dist * 60 * 1.1515;
		if (unit == 'K') {
			dist = dist * 1.609344;
		}
		else if (unit == 'N') {
			dist = dist * 0.8684;
		}

		if (logger.isDebugEnabled())
			logger.debug("distance: lat1=" + lat1 + " lon1=" + lon1 + " lat2=" + lat2 + " lon2=" + lon2
			        + " miles=" + dist);
		return dist;
	}

	/* ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::: */
	/* :: This function converts decimal degrees to radians : */
	/* ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::: */
	private double deg2rad(double deg) {
		return (deg * Math.PI / 180.0);
	}

	/* ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::: */
	/* :: This function converts radians to decimal degrees : */
	/* ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::: */
	private double rad2deg(double rad) {
		return (rad * 180.0 / Math.PI);
	}
}
