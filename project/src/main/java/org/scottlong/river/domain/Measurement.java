package org.scottlong.river.domain;

import java.util.Date;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "measurement")
public class Measurement {

	Date timestamp;
	Double temperature;
	Double gageHeight;
	Double discharge;
	Double conductance;
	Double turbidity;

	@XmlElement
	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	@XmlElement
	public Double getTemperature() {
		return temperature;
	}

	public void setTemperature(Double temperature) {
		this.temperature = temperature;
	}

	@XmlElement
	public Double getGageHeight() {
		return gageHeight;
	}

	public void setGageHeight(Double gageHeight) {
		this.gageHeight = gageHeight;
	}

	@XmlElement
	public Double getDischarge() {
		return discharge;
	}

	public void setDischarge(Double discharge) {
		this.discharge = discharge;
	}

	@XmlElement
	public Double getConductance() {
		return conductance;
	}

	public void setConductance(Double conductance) {
		this.conductance = conductance;
	}

	@XmlElement
	public Double getTurbidity() {
		return turbidity;
	}

	public void setTurbidity(Double turbidity) {
		this.turbidity = turbidity;
	}

	public String toString() {
		return "Measurement [timestamp=" + timestamp + ", temperature=" + temperature + ", gageHeight="
		        + gageHeight + ", discharge=" + discharge + ", conductance=" + conductance + ", turbidity="
		        + turbidity + "]";
	}

}
