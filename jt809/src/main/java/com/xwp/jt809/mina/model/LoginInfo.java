package com.xwp.jt809.mina.model;

import java.util.ArrayList;
import java.util.List;

import com.xwp.jt809.tools.ChangeType;
import com.xwp.jt809.tools.Tools;

public class LoginInfo {

	private int userId;
	
	private String passward;
	
	private String downLinkIP;
	
	private int downLinkPort;
	
	public LoginInfo(int userId,String passward,String downLinkIp,int port){
		this.userId = userId;
		this.passward = passward;
		this.downLinkIP = downLinkIp;
		this.downLinkPort = port;
	}
	
	public LoginInfo(List<Byte> info){
		byte[] userb = {info.get(0),info.get(1),info.get(2),info.get(3)};
		this.userId = ChangeType.bytesTo4Int(userb, 0);
		byte[] passwardb = new byte[8];
		for(int i=0;i<8;i++){
			passwardb[i] = info.get(i+4);
		}
		passwardb = Tools.cleanBefore0x00(passwardb);
		passwardb = Tools.cleanAfter0x00(passwardb);
		this.passward = new String(passwardb);
		byte[] ipb = new byte[32];
		for(int i=0;i<32;i++){
			ipb[i] = info.get(i+12);
		}
		ipb = Tools.cleanAfter0x00(ipb);
		ipb = Tools.cleanBefore0x00(ipb);
		this.downLinkIP = new String(ipb);
		byte[] portb = {info.get(44),info.get(45)};
		this.downLinkPort = ChangeType.bytesTo2Int(portb, 0);
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getPassward() {
		return passward;
	}

	public void setPassward(String passward) {
		this.passward = passward;
	}

	public String getDownLinkIP() {
		return downLinkIP;
	}

	public void setDownLinkIP(String downLinkIP) {
		this.downLinkIP = downLinkIP;
	}

	public int getDownLinkPort() {
		return downLinkPort;
	}

	public void setDownLinkPort(int downLinkPort) {
		this.downLinkPort = downLinkPort;
	}
	
	public List<Byte> getBytes(){
		List<Byte> list = new ArrayList<Byte>();
		list = Tools.combine(list,ChangeType.intTo4Bytes(userId),4);
		list = Tools.combine8(list,passward.getBytes());
		list = Tools.combine32(list, downLinkIP.getBytes());
		list = Tools.combine(list, ChangeType.intTo2Bytes(downLinkPort),2);
		return list;
	}

	@Override
	public String toString() {
		return "LoginInfo [userId=" + userId + ", passward=" + passward
				+ ", downLinkIP=" + downLinkIP + ", downLinkPort="
				+ downLinkPort + "]";
	}
}
