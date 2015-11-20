package com.xwp.jt809.mina.codec;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.xwp.jt809.mina.model.LoginInfo;
import com.xwp.jt809.mina.model.MyMap;
import com.xwp.jt809.tools.ChangeType;
import com.xwp.jt809.tools.Encrypt;
import com.xwp.jt809.tools.LogicMark;
import com.xwp.jt809.tools.TabCode;
import com.xwp.jt809.tools.Transform;

public class ExplainMessage {

	/**
	 * 解析用户登录
	 * @param blist
	 * @return
	 * @throws IOException 
	 */
	public static LoginInfo explainLogin(List<Byte> blist) throws IOException{
		byte[] megLength = new byte[4];
		byte[] megGnsscenterid = new byte[4];
		
		byte[] encryptKey = new byte[4];
		// 反转义
		blist = Transform.unchange(blist);
		for (int i = 0; i < 4; i++) {
			megLength[i] = blist.get(i + 1);
			megGnsscenterid[i] = blist.get(i + 11);
			encryptKey[i] = blist.get(i + 19);
		}
		int k = ChangeType.bytesTo4Int(megLength, 0);
		int gnsscenterid = ChangeType.bytesTo4Int(megGnsscenterid, 0);
		int key = ChangeType.bytesTo4Int(encryptKey, 0);
		// 数据长度和接入码都正确，才进行处理
		if (blist.size() == k && gnsscenterid == LogicMark.GNSSCENTERID) {
			byte[] msgidByte = { blist.get(9), blist.get(10) };
			Byte encrypt = blist.get(18);
			int MSGID = ChangeType.bytesTo2Int(msgidByte, 0);
			if (MSGID == TabCode.UP_CONNECT_REQ) {
				blist = new ArrayList<Byte>(blist.subList(23, blist.size()-3));
				if(encrypt.intValue() == 1){
					byte[] tmp = new byte[blist.size()];
					for(int i=0;i<blist.size();i++){
						tmp[i] = blist.get(i);
					}
					tmp = Encrypt.en(key, tmp, tmp.length);
					blist.clear();
					for(int i=0;i<tmp.length;i++){
						blist.add(tmp[i]);
					}
				}
				return new LoginInfo(blist);
			}
		}
		return null;
	}
	
	/**
	 * 解析byte应答
	 * @param blist
	 * @return
	 * @throws IOException 
	 */
	public static MyMap explainResponse(List<Byte> blist) throws IOException{
		byte[] megLength = new byte[4];
		byte[] megGnsscenterid = new byte[4];
		
		byte[] encryptKey = new byte[4];
		// 反转义
		blist = Transform.unchange(blist);
		for (int i = 0; i < 4; i++) {
			megLength[i] = blist.get(i + 1);
			megGnsscenterid[i] = blist.get(i + 11);
			encryptKey[i] = blist.get(i + 19);
		}
		int k = ChangeType.bytesTo4Int(megLength, 0);
		int gnsscenterid = ChangeType.bytesTo4Int(megGnsscenterid, 0);
		int key = ChangeType.bytesTo4Int(encryptKey, 0);
		// 数据长度和接入码都正确，才进行处理
		if (blist.size() == k && gnsscenterid == LogicMark.GNSSCENTERID) {
			byte[] msgidByte = { blist.get(9), blist.get(10) };
			Byte encrypt = blist.get(18);
			int MSGID = ChangeType.bytesTo2Int(msgidByte, 0);
			blist = new ArrayList<Byte>(blist.subList(23, blist.size()-3));
			if(encrypt.intValue() == 1){
				byte[] tmp = new byte[blist.size()];
				for(int i=0;i<blist.size();i++){
					tmp[i] = blist.get(i);
				}
				tmp = Encrypt.en(key, tmp, tmp.length);
				blist.clear();
				for(int i=0;i<tmp.length;i++){
					blist.add(tmp[i]);
				}
			}
			if(blist.size() ==  0){
				return new MyMap(MSGID, (byte)-1);
			}else{
				return new MyMap(MSGID, blist.get(0));
			}
		}
		return null;
	}
	
}
