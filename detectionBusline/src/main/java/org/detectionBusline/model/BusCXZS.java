package org.detectionBusline.model;

public class BusCXZS {

	private String date_yyyymmdd;
	
	private int date_hour;
	
	private int ID;
	
	private double speed;
	
	private double cxzs;
	
	private int data_sum;
	
	private String tablename;

	public String getDate_yyyymmdd() {
		return date_yyyymmdd;
	}

	public void setDate_yyyymmdd(String date_yyyymmdd) {
		this.date_yyyymmdd = date_yyyymmdd;
	}

	public int getDate_hour() {
		return date_hour;
	}

	public void setDate_hour(int date_hour) {
		this.date_hour = date_hour;
	}

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public double getSpeed() {
		return speed;
	}

	public void setSpeed(double speed) {
		this.speed = speed;
		this.cxzs = Math.min(speed/30,1)*10;
	}

	public double getCxzs() {
		return cxzs;
	}

	public void setCxzs(double cxzs) {
		this.cxzs = cxzs;
	}

	public int getSum() {
		return data_sum;
	}

	public void setSum(int sum) {
		this.data_sum = sum;
	}

	public String getTablename() {
		return tablename;
	}

	public void setTablename(String tablename) {
		this.tablename = tablename;
	}
	
}
