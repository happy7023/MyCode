package com.xwp.jt809.mina.model;

import java.io.UnsupportedEncodingException;
import java.util.List;

import com.xwp.jt809.tools.ChangeType;
import com.xwp.jt809.tools.Tools;

public class ExgMsg {

	/**车牌*/
	private String vehivleNo;
	/**车牌颜色*/
	private Byte vehicleColor;
	/**子业务数据类型*/
	private int dataType;
	/**数据长度*/
	private int dataLength;
	
	public ExgMsg(){}
	
	public ExgMsg(List<Byte> blist) throws UnsupportedEncodingException{
		byte[] plaNoBytes = new byte[21];
		byte[] dataTypeBytes = new byte[2];
		byte[] dataLengthByte = new byte[4];
		for(int i=0;i<21;i++){
			plaNoBytes[i] = blist.get(i);
		}
		plaNoBytes = Tools.cleanAfter0x00(plaNoBytes);
		this.vehivleNo = new String(plaNoBytes,"GBK");
		this.vehicleColor = blist.get(21);
		dataTypeBytes[0] = blist.get(22);
		dataTypeBytes[1] = blist.get(23);
		this.dataType = ChangeType.bytesTo2Int(dataTypeBytes, 0);
		for(int i=0;i<4;i++){
			dataLengthByte[i] = blist.get(i+24);
		}
		this.dataLength = ChangeType.bytesTo4Int(dataLengthByte, 0);
	}
	
	public String getVehivleNo() {
		return vehivleNo;
	}
	public void setVehivleNo(String vehivleNo) {
		this.vehivleNo = vehivleNo;
	}
	public Byte getVehicleColor() {
		return vehicleColor;
	}
	public void setVehicleColor(Byte vehicleColor) {
		this.vehicleColor = vehicleColor;
	}
	public int getDataType() {
		return dataType;
	}
	public void setDataType(int dataType) {
		this.dataType = dataType;
	}
	public int getDataLength() {
		return dataLength;
	}
	public void setDataLength(int dataLength) {
		this.dataLength = dataLength;
	}
	
}
