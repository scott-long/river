package org.scottlong.river.domain;

import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.log4j.Logger;

@XmlRootElement(name = "measurements")
public class Measurements {

	private static final Logger logger = Logger.getLogger(Stations.class);

	private List<Measurement> measurements;
	private Date loaded;

	public Measurements() {
		if (logger.isTraceEnabled())
			logger.trace("<init>");
	}

	public Measurements(List<Measurement> measurements) {
		if (logger.isTraceEnabled())
			logger.trace("<init> measurements=" + (measurements == null ? 0 : measurements.size()));
		this.measurements = measurements;
		this.loaded = new Date();
	}

	@XmlElement
	public Date getLoaded() {
		return loaded;
	}

	@XmlElementRef
	public List<Measurement> getMeasurements() {
		return measurements;
	}

	public void setMeasurements(List<Measurement> measurements) {
		this.measurements = measurements;
	}

	public String toString() {
		return "Measurements [loaded=" + loaded + "]";
	}
}
