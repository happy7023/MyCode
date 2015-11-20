package com.xwp.jt809.mina.model;

import java.util.List;

public class DriverMsg {

	private ExgMsg exg;
	//驾驶员姓名
	private String driverName;
	//身份证号
	private String driverId;
	//从业资格证号
	private String licence;
	//发证机构名称
	private String orgName;

	public DriverMsg(List<Byte> blist,ExgMsg exg){
		this.exg = exg;
		byte[] driverNameBytes = new byte[16];
		byte[] driverIdBytes = new byte[20];
		byte[] licenceBytes = new byte[40];
		byte[] orgNameBytes = new byte[200];
		for(int i=0;i<16;i++){
			driverNameBytes[i] = blist.get(i);
		}
		for(int i=0;i<20;i++){
			driverIdBytes[i] = blist.get(i+16);
		}
		for(int i=0;i<40;i++){
			licenceBytes[i] = blist.get(i+36);
		}
		for(int i=0;i<200;i++){
			orgNameBytes[i] = blist.get(i+76);
		}
		this.driverName = new String(driverNameBytes);
		this.driverId = new String(driverIdBytes);
		this.licence = new String(licenceBytes);
		this.orgName = new String(orgNameBytes);
	}
	
	public ExgMsg getExg() {
		return exg;
	}

	public void setExg(ExgMsg exg) {
		this.exg = exg;
	}

	public String getDriverName() {
		return driverName;
	}

	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}

	public String getDriverId() {
		return driverId;
	}

	public void setDriverId(String driverId) {
		this.driverId = driverId;
	}

	public String getLicence() {
		return licence;
	}

	public void setLicence(String licence) {
		this.licence = licence;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	
	
}
