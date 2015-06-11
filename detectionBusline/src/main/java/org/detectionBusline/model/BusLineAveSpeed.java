package org.detectionBusline.model;

import java.util.Date;

public class BusLineAveSpeed {
	
	private String t_date;
	
	private int t_hour;
	/**线路ID*/
	private int lineID;
	/**车牌号*/
	private int numb;
	/**平均速度*/
	private double speed;
	/**发车时间*/
	private Date starttime;
	/**到终点站时间*/
	private Date endtime;
	/**上下行标志 1：上行 2：下行*/
	private int upORdown;
	/**公交畅行指数*/
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
	
	public int getLineID() {
		return lineID;
	}

	public void setLineID(int lineID) {
		this.lineID = lineID;
	}

	public int getNumb() {
		return numb;
	}

	public void setNumb(int numb) {
		this.numb = numb;
	}

	public double getSpeed() {
		return speed;
	}

	public void setSpeed(double speed) {
		this.speed = speed;
	}

	public Date getStarttime() {
		return starttime;
	}

	public void setStarttime(Date starttime) {
		this.starttime = starttime;
	}

	public Date getEndtime() {
		return endtime;
	}

	public void setEndtime(Date endtime) {
		this.endtime = endtime;
	}

	public int getUpORdown() {
		return upORdown;
	}

	public void setUpORdown(int upORdown) {
		this.upORdown = upORdown;
	}

	public double getCxzs() {
		return cxzs;
	}

	public void setCxzs(double cxzs) {
		this.cxzs = cxzs;
	}

	
}
