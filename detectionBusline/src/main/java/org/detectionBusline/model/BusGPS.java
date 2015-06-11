package org.detectionBusline.model;

import java.io.Serializable;
import java.util.Date;

public class BusGPS implements Serializable{

	private static final long serialVersionUID = 4158005166331030815L;
	/**车牌号码(要是没有车牌号码就用能唯一标识每一辆车的编号)*/
	private int numb;
	/**经度*/
	private double latitude;
	/**纬度*/
	private double longitude;
	/**角度*/
	private float angle;
	/**速度*/
	private float speed;
	/**数据时间*/
	private Date time;
	/**上传时间*/
	private Date zk_create_time;
	/**车辆线路*/
	private int lineID;
	
	private String road_id;
	
	private int mark = 1;

	public int getNumb() {
		return numb;
	}

	public void setNumb(int numb) {
		this.numb = numb;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public float getAngle() {
		return angle;
	}

	public void setAngle(float angle) {
		this.angle = angle;
	}

	public float getSpeed() {
		return speed;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public Date getZk_create_time() {
		return zk_create_time;
	}

	public void setZk_create_time(Date zk_create_time) {
		this.zk_create_time = zk_create_time;
	}

	public int getLineID() {
		return lineID;
	}

	public void setLineID(int lineID) {
		this.lineID = lineID;
	}

	public String getRoad_id() {
		return road_id;
	}

	public void setRoad_id(String road_id) {
		this.road_id = road_id;
	}

	public int getMark() {
		return mark;
	}

	public void setMark(int mark) {
		this.mark = mark;
	}

}
