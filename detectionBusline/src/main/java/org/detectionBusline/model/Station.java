package org.detectionBusline.model;

import java.io.Serializable;

public class Station implements Serializable {

	private static final long serialVersionUID = -5733005697063726938L;

	private int id;
	
	private double longitude;
	
	private double latitude;
	
	private String shortname;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public double getLongi() {
		return longitude;
	}

	public void setLongi(double longitude) {
		this.longitude = longitude;
	}

	public double getLati() {
		return latitude;
	}

	public void setLati(double latitude) {
		this.latitude = latitude;
	}

	public String getShortname() {
		return shortname;
	}

	public void setShortname(String shortname) {
		this.shortname = shortname;
	}
	
}
