package org.scottlong.river.domain;

import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.log4j.Logger;

@XmlRootElement(name = "stations")
public class Stations {

	private static final Logger logger = Logger.getLogger(Stations.class);

	private List<Station> stations;
	private Date loaded;

	public Stations() {
		if (logger.isTraceEnabled())
			logger.trace("<init>");
	}

	public Stations(List<Station> stations) {
		if (logger.isTraceEnabled())
			logger.trace("<init> stations=" + (stations == null ? 0 : stations.size()));
		this.stations = stations;
		this.loaded = new Date();
	}

	@XmlElement
	public Date getLoaded() {
		return loaded;
	}

	@XmlElementRef
	public List<Station> getStations() {
		return stations;
	}

	public void setStations(List<Station> stations) {
		this.stations = stations;
	}
}
