package com.xwp.jt809.mina.model;

import java.util.List;

import com.xwp.jt809.tools.ChangeType;

public class EwayBill {

	private ExgMsg exg;
	
	private int ewaybillLength;
	
	private String ewaybillInfo;
	
	public EwayBill (List<Byte> blist,ExgMsg exg){
		byte[] ewaybillLengthByte = new byte[4];
		for(int i=0;i<4;i++){
			ewaybillLengthByte[i] = blist.get(i);
		}
		this.ewaybillLength = ChangeType.bytesTo4Int(ewaybillLengthByte, 0);
		byte[] ewaybillInfoByte = new byte[ewaybillLength];
		for(int i=0;i<ewaybillLength;i++){
			ewaybillInfoByte[i] = blist.get(i+4);
		}
		this.ewaybillInfo = new String(ewaybillInfoByte);
	}

	public ExgMsg getExg() {
		return exg;
	}

	public void setExg(ExgMsg exg) {
		this.exg = exg;
	}

	public int getEwaybillLength() {
		return ewaybillLength;
	}

	public void setEwaybillLength(int ewaybillLength) {
		this.ewaybillLength = ewaybillLength;
	}

	public String getEwaybillInfo() {
		return ewaybillInfo;
	}

	public void setEwaybillInfo(String ewaybillInfo) {
		this.ewaybillInfo = ewaybillInfo;
	}
}
