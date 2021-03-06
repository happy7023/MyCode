package org.detectionBusline.model;

public class AllLineMessages {

	/**车牌*/
	private int lineID;
	/**是否开出*/
	private boolean isOnline = false;
	/**1表示上行，2表示下行*/
	private int markUpDown;
	/**上行始发站*/
	private int upstart;
	private LngLat upStartStation;
	/**上行终点站*/
	private int upend;
	private LngLat upEndStation;
	/**下行始发站*/
	private int downstart;
	private LngLat downStartStation;
	/**下行终点站*/
	private int downend;
	private LngLat downEndStation;
	/**线路长度*/
	private float length;
	/**发车时间*/
	private long start;
	/**结束时间*/
	private long end;
	/**
	 * @return the numb
	 */
	public int getLineID() {
		return lineID;
	}
	/**
	 * @param numb the numb to set
	 */
	public void setLineID(int numb) {
		this.lineID = numb;
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
	 * @return the upstart
	 */
	public int getUpstart() {
		return upstart;
	}
	/**
	 * @param upstart the upstart to set
	 */
	public void setUpstart(int upstart) {
		this.upstart = upstart;
	}
	/**
	 * @return the upend
	 */
	public int getUpend() {
		return upend;
	}
	/**
	 * @param upend the upend to set
	 */
	public void setUpend(int upend) {
		this.upend = upend;
	}
	/**
	 * @return the downstart
	 */
	public int getDownstart() {
		return downstart;
	}
	/**
	 * @param downstart the downstart to set
	 */
	public void setDownstart(int downstart) {
		this.downstart = downstart;
	}
	/**
	 * @return the downend
	 */
	public int getDownend() {
		return downend;
	}
	/**
	 * @param downend the downend to set
	 */
	public void setDownend(int downend) {
		this.downend = downend;
	}
}
