package org.detectionBusline.model;

public class ElectronicFenceSpeedCXZS {

	private String t_date;
	
	private int t_hour;
	
	private String area_name;
	
	private double speed;
	
	private double cxzs;

	public String getT_date() {
		return t_date;
	}

	public void setT_date(String t_date) {
		this.t_date = t_date;
	}

	public int getT_hour() {
		return t_hour;
	}

	public void setT_hour(int t_hour) {
		this.t_hour = t_hour;
	}

	public String getArea_name() {
		return area_name;
	}

	public void setArea_name(String area_name) {
		this.area_name = area_name;
	}

	public double getSpeed() {
		return speed;
	}

	public void setSpeed(double speed) {
		this.speed = speed;
		this.cxzs = (Math.min(speed/30, 1)*10);
	}

	public double getCxzs() {
		return cxzs;
	}

	public void setCxzs(double cxzs) {
		this.cxzs = cxzs;
	}
	
}
