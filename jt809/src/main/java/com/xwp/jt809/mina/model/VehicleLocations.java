package com.xwp.jt809.mina.model;

import java.util.List;

public class VehicleLocations {

	private ExgMsg exgMsg;
	/**卫星定位数据个数*/
	private Byte gnssCnt;
	
	private List<VehicleLocation> listLocation;
	
	public VehicleLocations(List<Byte> blist,ExgMsg exg){		
		this.exgMsg = exg;
		this.gnssCnt = blist.get(0);
		for(int i=0;i<gnssCnt;i++){
			List<Byte> tmp = blist.subList(1+i*36, 1+(i+1)*36);
			VehicleLocation vehicleLocation = new VehicleLocation(tmp,exg);
			listLocation.add(vehicleLocation);
		}
	}

	public ExgMsg getExgMsg() {
		return exgMsg;
	}

	public void setExgMsg(ExgMsg exgMsg) {
		this.exgMsg = exgMsg;
	}

	public Byte getGnssCnt() {
		return gnssCnt;
	}

	public void setGnssCnt(Byte gnssCnt) {
		this.gnssCnt = gnssCnt;
	}

	public List<VehicleLocation> getListLocation() {
		return listLocation;
	}

	public void setListLocation(List<VehicleLocation> listLocation) {
		this.listLocation = listLocation;
	}
}
