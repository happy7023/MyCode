package com.xwp.jt809.wzjtj.model;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import com.xwp.jt809.wzjtj.tools.ChangeType;
import com.xwp.jt809.wzjtj.tools.Tools;

public class VehicleLocation {

	/**车辆基本信息*/
	private ExgMsg exg;
	/**是否加密：1加密，0未加密*/
	private Byte encrypt;
	/**日期:年（2Byte），月（1Byte），日（1Byte）*/
	private String date;
	/**时（1Byte）分（1Byte）秒（1Byte）*/
	private String time;
	/**经度(单位1*10-6度)*/
	private double longi;
	/**纬度(单位1*10-6度)*/
	private double lati;
	/**速度,卫星定位设备上传速度（KM/H）*/
	private int vec1;
	/**速度，车辆行驶设备上上传的速度（KM/H）*/
	private int vec2;
	/**车辆当前总里程数（KM）*/
	private int vec3;
	/**方向，顺时针，正北0*/
	private int direction;
	/**海拔高度*/
	private int altitude;
	/**车辆状态，二进制表示：B31....B0*/
	private int state;
	/**报警状态，0正常，1报警*/
	private int alarm;
	
	public VehicleLocation(){}
	public VehicleLocation(List<Byte> blist,ExgMsg exg){
		this.exg = exg;
		this.encrypt = blist.get(0);
		byte[] yearByte = {blist.get(3),blist.get(4)};
		this.date = blist.get(1)+"/"+blist.get(2)+"/"+ChangeType.bytesTo2Int(yearByte, 0);
		this.time = blist.get(5)+":"+blist.get(6)+":"+blist.get(7);
		byte[] longiBytes = new byte[4];
		byte[] latiBytes = new byte[4];
		byte[] vec1Bytes = new byte[2];
		byte[] vec2Bytes = new byte[2];
		byte[] vec3Bytes = new byte[4];
		byte[] directionBytes = new byte[2];
		byte[] altitudeBytes = new byte[4];
		byte[] stateBytes = new byte[4];
		byte[] alarmBytes = new byte[4];
		for(int i=0;i<4;i++){
			longiBytes[i]=blist.get(i+8);
			latiBytes[i]=blist.get(i+12);
			vec3Bytes[i]=blist.get(i+22);
			stateBytes[i]=blist.get(i+28);
			alarmBytes[i]=blist.get(i+32);
		}
		for(int i=0;i<2;i++){
			vec1Bytes[i]=blist.get(i+16);
			vec2Bytes[i]=blist.get(i+18);
			directionBytes[i]=blist.get(i+24);
			altitudeBytes[i]=blist.get(i+26);
		}
		this.longi = ChangeType.bytesTo4Int(longiBytes, 0)*0.000001;
		this.lati = ChangeType.bytesTo4Int(latiBytes, 0)*0.000001;
		this.vec1 = ChangeType.bytesTo2Int(vec1Bytes, 0);
		this.vec2 = ChangeType.bytesTo2Int(vec2Bytes, 0);
		this.vec3 = ChangeType.bytesTo4Int(vec3Bytes, 0);
		this.direction = ChangeType.bytesTo2Int(directionBytes, 0);
		this.altitude = ChangeType.bytesTo2Int(altitudeBytes, 0);
		this.state = ChangeType.bytesTo4Int(stateBytes, 0);
		this.alarm = ChangeType.bytesTo4Int(alarmBytes, 0);
	}
	
	public ExgMsg getExg() {
		return exg;
	}
	public void setExg(ExgMsg exg) {
		this.exg = exg;
	}
	public Byte getEncrypt() {
		return encrypt;
	}
	public void setEncrypt(Byte encrypt) {
		this.encrypt = encrypt;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public double getLongi() {
		return longi;
	}
	public void setLongi(double longi) {
		this.longi = longi;
	}
	public double getLati() {
		return lati;
	}
	public void setLati(double lati) {
		this.lati = lati;
	}
	public int getVec1() {
		return vec1;
	}
	public void setVec1(int vec1) {
		this.vec1 = vec1;
	}
	public int getVec2() {
		return vec2;
	}
	public void setVec2(int vec2) {
		this.vec2 = vec2;
	}
	public int getVec3() {
		return vec3;
	}
	public void setVec3(int vec3) {
		this.vec3 = vec3;
	}
	public int getDirection() {
		return direction;
	}
	public void setDirection(int direction) {
		this.direction = direction;
	}
	public int getAltitude() {
		return altitude;
	}
	public void setAltitude(int altitude) {
		this.altitude = altitude;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public int getAlarm() {
		return alarm;
	}
	public void setAlarm(int alarm) {
		this.alarm = alarm;
	}
	
	public List<Byte> getBytes() throws UnsupportedEncodingException{
		List<Byte> list = new ArrayList<Byte>();
		list = Tools.combine(list,exg.getVehivleNo().getBytes("GBK"),21);
		list.add(exg.getVehicleColor());
		list = Tools.combine(list,ChangeType.intTo2Bytes(exg.getDataType()),2);
		list = Tools.combine(list, ChangeType.intTo4Bytes(exg.getDataLength()),4);
		list.add((byte) 36);
		list.add(getEncrypt());
		list.add((byte) 27);
		list.add((byte) 7);
		list = Tools.combine(list, ChangeType.intTo2Bytes(2015),2);
		list.add((byte) 00);
		list.add((byte) 00);
		list.add((byte) 00);
		list = Tools.combine(list, ChangeType.intTo4Bytes((int) (getLongi()*1000000)),4);
		list = Tools.combine(list, ChangeType.intTo4Bytes((int) (getLati()*1000000)),4);
		list = Tools.combine(list, ChangeType.intTo2Bytes(getVec1()),2);
		list = Tools.combine(list, ChangeType.intTo2Bytes(getVec2()),2);
		list = Tools.combine(list, ChangeType.intTo4Bytes(getVec3()),4);
		list = Tools.combine(list, ChangeType.intTo2Bytes(getDirection()),2);
		list = Tools.combine(list, ChangeType.intTo2Bytes(getAltitude()),2);
		list = Tools.combine(list, ChangeType.intTo4Bytes(getState()),4);
		list = Tools.combine(list, ChangeType.intTo4Bytes(getAlarm()),4);
		return list;
	}
}
