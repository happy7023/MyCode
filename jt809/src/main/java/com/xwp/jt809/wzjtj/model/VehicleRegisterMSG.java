package com.xwp.jt809.wzjtj.model;

import java.util.List;

public class VehicleRegisterMSG{
	/**车辆基本信息*/
	private ExgMsg exg;
	/**平台唯一编码*/
	private Byte[] platformId = new Byte[11];
	/**车载终端厂商唯一编码*/
	private Byte[] producerId = new Byte[11];
	/**车载终端型号，不足8位以“\0”终结*/
	private Byte[] terminalModelType = new Byte[8];
	/**车载终端编号，大写字母和数据组成*/
	private Byte[] terminalId = new Byte[7];
	/**车载终端SIM卡电话号码。号码不足12位，前面补0*/
	private String terminalSimCode;
	
	public VehicleRegisterMSG(List<Byte> blist,ExgMsg exg){
		this.exg = exg;
		for(int i=0;i<11;i++){
			platformId[i] = blist.get(i);
			producerId[i] = blist.get(i+11);
		}
		for(int i=0;i<8;i++){
			terminalModelType[i] = blist.get(i+22);
		}
		for(int i=0;i<7;i++){
			terminalModelType[i] = blist.get(i+30);
		}
		byte[] terminalSimCode = new byte[12];
		for(int i=0;i<12;i++){
			terminalModelType[i] = blist.get(i+37);
		}
		this.terminalSimCode = new String(terminalSimCode);
	}

	public ExgMsg getExg() {
		return exg;
	}

	public void setExg(ExgMsg exg) {
		this.exg = exg;
	}

	public Byte[] getPlatformId() {
		return platformId;
	}

	public void setPlatformId(Byte[] platformId) {
		this.platformId = platformId;
	}

	public Byte[] getProducerId() {
		return producerId;
	}

	public void setProducerId(Byte[] producerId) {
		this.producerId = producerId;
	}

	public Byte[] getTerminalModelType() {
		return terminalModelType;
	}

	public void setTerminalModelType(Byte[] terminalModelType) {
		this.terminalModelType = terminalModelType;
	}

	public Byte[] getTerminalId() {
		return terminalId;
	}

	public void setTerminalId(Byte[] terminalId) {
		this.terminalId = terminalId;
	}

	public String getTerminalSimCode() {
		return terminalSimCode;
	}

	public void setTerminalSimCode(String terminalSimCode) {
		this.terminalSimCode = terminalSimCode;
	}	
}
