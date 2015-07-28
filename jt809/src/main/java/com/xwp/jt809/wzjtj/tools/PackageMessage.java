package com.xwp.jt809.wzjtj.tools;

import java.util.ArrayList;
import java.util.List;

public class PackageMessage {

	private static final List<Byte> listHead = new ArrayList<Byte>();
	private int msgnum = 0;
	private int jrm = 3303001;
	private int encryptKey = 0x6666;
	
	public List<Byte> packagemsg(int msgCode,byte dataCode){
		listHead.clear();
		listHead.add((byte) 0x5b);		//头标识
		int msgLength;
		if(dataCode == -1){
			msgLength = 1+4+4+2+4+3+1+4+2+1;
		}else{
			msgLength = 1+4+4+2+4+3+1+4+1+2+1;
		}
		listHead.addAll(ChangeType.intTo4Bytes(msgLength));		//报文长度
		listHead.addAll(ChangeType.intTo4Bytes(msgnum));	//报文序列
		if(msgnum == Integer.MIN_VALUE){		//序列号自增
			msgnum = 0;
		}else{
			msgnum++;
		}
		listHead.addAll(ChangeType.intTo2Bytes(msgCode));
		listHead.addAll(ChangeType.intTo4Bytes(jrm)); 	//接入码
		listHead.add((byte) 0x01);		//协议版本号
		listHead.add((byte) 0x02);
		listHead.add((byte) 0x0F);
		listHead.add((byte) 0);			//协议是否加密
		listHead.addAll(ChangeType.intTo4Bytes(encryptKey));	//秘钥
		if(dataCode != -1){
			listHead.add((byte) dataCode);			//数据体
		}
		listHead.addAll(ChangeType.intTo2Bytes(encryptKey));	//校验
		listHead.add((byte) 0x5d);			//尾标识
			
		return listHead;
	}
	
	public List<Byte> packagemsg(int msgCode,List<Byte> listb){
		listHead.clear();
		listHead.add((byte) 0x5b); //头标识
		int msgLength = 1+4+4+2+4+3+1+4+listb.size()+2+1;
		listHead.addAll(ChangeType.intTo4Bytes(msgLength));		//报文长度
		listHead.addAll(ChangeType.intTo4Bytes(msgnum));	//报文序列
		if(msgnum == Integer.MIN_VALUE){		//序列号自增
			msgnum = 0;
		}else{
			msgnum++;
		}
		listHead.addAll(ChangeType.intTo2Bytes(msgCode));
	
		listHead.addAll(ChangeType.intTo4Bytes(jrm)); 	//接入码
		listHead.add((byte) 0x01);		//协议版本号
		listHead.add((byte) 0x02);
		listHead.add((byte) 0x0F);
		listHead.add((byte) 0);			//协议是否加密
		listHead.addAll(ChangeType.intTo4Bytes(encryptKey));	//秘钥
		for(Byte b:listb){
			listHead.add(b);
		}
//		listHead.addAll(login.getUserId().getBytes());			//数据体
		listHead.addAll(ChangeType.intTo2Bytes(encryptKey));	//校验
		listHead.add((byte) 0x5d);			//尾标识
			
		return listHead;
	}
	
}
