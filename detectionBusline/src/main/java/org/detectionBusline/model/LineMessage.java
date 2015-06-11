package org.detectionBusline.model;

public class LineMessage implements Cloneable {
	/**线路*/
	private int id;
	/**是否开出*/
	private boolean isOnline = false;
	/**1表示上行，2表示下行*/
	private int markUpDown;
	/**上行始发站*/
	private int upstartstationid;
	private LngLat upStartStation;
	/**上行终点站*/
	private int upendstationid;
	private LngLat upEndStation;
	/**下行始发站*/
	private int downstartstationid;
	private LngLat downStartStation;
	/**下行终点站*/
	private int downendstationid;
	private LngLat downEndStation;
	/**线路长度*/
	private float length;
	/**发车时间*/
	private long start;
	/**结束时间*/
	private long end;
	
	@Override
	public Object clone() throws CloneNotSupportedException {
		// 实现clone方法
		return super.clone();
	}
	/**
	 * @return the numb
	 */
	public int getID() {
		return id;
	}
	/**
	 * @param numb the numb to set
	 */
	public void setLineID(int numb) {
		this.id = numb;
	}
	/**
	 * @return the isOnline
	 */
	public boolean isOnline() {
		return isOnline;
	}
	/**
	 * @param isOnline the isOnline to set
	 */
	public void setOnline(boolean isOnline) {
		this.isOnline = isOnline;
	}
	/**
	 * @return the markUpDown
	 */
	public int getMarkUpDown() {
		return markUpDown;
	}
	/**
	 * @param markUpDown the markUpDown to set
	 */
	public void setMarkUpDown(int markUpDown) {
		this.markUpDown = markUpDown;
	}
	/**
	 * @return the upStartStation
	 */
	public LngLat getUpStartStation() {
		return upStartStation;
	}
	/**
	 * @param upStartStation the upStartStation to set
	 */
	public void setUpStartStation(LngLat upStartStation) {
		this.upStartStation = upStartStation;
	}
	/**
	 * @return the upEndStation
	 */
	public LngLat getUpEndStation() {
		return upEndStation;
	}
	/**
	 * @param upEndStation the upEndStation to set
	 */
	public void setUpEndStation(LngLat upEndStation) {
		this.upEndStation = upEndStation;
	}
	/**
	 * @return the downStartStation
	 */
	public LngLat getDownStartStation() {
		return downStartStation;
	}
	/**
	 * @param downStartStation the downStartStation to set
	 */
	public void setDownStartStation(LngLat downStartStation) {
		this.downStartStation = downStartStation;
	}
	/**
	 * @return the downEndStation
	 */
	public LngLat getDownEndStation() {
		return downEndStation;
	}
	/**
	 * @param downEndStation the downEndStation to set
	 */
	public void setDownEndStation(LngLat downEndStation) {
		this.downEndStation = downEndStation;
	}
	/**
	 * @return the start
	 */
	public long getStart() {
		return start;
	}
	/**
	 * @param start the start to set
	 */
	public void setStart(long start) {
		this.start = start;
	}
	/**
	 * @return the end
	 */
	public long getEnd() {
		return end;
	}
	/**
	 * @param end the end to set
	 */
	public void setEnd(long end) {
		this.end = end;
	}
	/**
	 * @return the length
	 */
	public float getLength() {
		return length;
	}
	/**
	 * @param length the length to set
	 */
	public void setLength(float length) {
		this.length = length;
	}
	/**
	 * @return the upstartstationid
	 */
	public int getUpstartstationid() {
		return upstartstationid;
	}
	/**
	 * @param upstartstationid the upstartstationid to set
	 */
	public void setUpstartstationid(int upstartstationid) {
		this.upstartstationid = upstartstationid;
	}
	/**
	 * @return the upendstationid
	 */
	public int getUpendstationid() {
		return upendstationid;
	}
	/**
	 * @param upendstationid the upendstationid to set
	 */
	public void setUpendstationid(int upendstationid) {
		this.upendstationid = upendstationid;
	}
	/**
	 * @return the downstartstationid
	 */
	public int getDownstartstationid() {
		return downstartstationid;
	}
	/**
	 * @param downstartstationid the downstartstationid to set
	 */
	public void setDownstartstationid(int downstartstationid) {
		this.downstartstationid = downstartstationid;
	}
	/**
	 * @return the downendstationid
	 */
	public int getDownendstationid() {
		return downendstationid;
	}
	/**
	 * @param downendstationid the downendstationid to set
	 */
	public void setDownendstationid(int downendstationid) {
		this.downendstationid = downendstationid;
	}
}
