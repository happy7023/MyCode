package com.xwp.jt809.tools;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.List;

import com.xwp.jt809.mina.model.LoginInfo;

public class ExplainConnect {

	/**
	 * 解析用户登录
	 * @param blist
	 * @return
	 * @throws IOException 
	 */
	public static byte explainLogin(List<Byte> blist,SocketAddress downaddress) throws IOException{
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
				LoginInfo loginInfo = new LoginInfo(blist);
				if (loginInfo.getUserId() != LogicMark.USERNAME) {
					System.out.println("不被允许的用户。");
					return LogicMark.USERNAME_ERROR;
				} else if (!loginInfo.getPassward().equals(LogicMark.PASSWORD)) {
					System.out.println("密码错误。");
					return LogicMark.PASSWORD_ERROR;
				} else {
					downaddress = new InetSocketAddress(loginInfo.getDownLinkIP(), loginInfo.getDownLinkPort());
					System.out.println("用户验证成功--用户名:"+loginInfo.getUserId()+" 密码:"+loginInfo.getPassward());
					return LogicMark.SUCCESS;
				}
			}
		}
		System.out.println("TcpServerThread--"+"接入码或者数据长度有问题.连接失败");
		return LogicMark.OTHER;
	} 
	/**
	 * 解析从链路应答
	 * @param blist
	 * @return
	 * @throws IOException 
	 */
	public static boolean explainFromLink(List<Byte> blist) throws IOException{
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
			if (MSGID == TabCode.DOWN_CONNECT_RSP) {
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
				try {
					if (blist.get(0).intValue() == 0x00) {
						System.out.println("从链路建立成功。");
						return true;
					} else if (blist.get(0).intValue() == 0x01) {
						System.out.println("从链路建立失败，VERIFY_CODE错误。");
						return false;
					} else if(blist.get(0).intValue() == 0x02){
						System.out.println("从链路建立失败，资源紧张，稍后重连。");
						return false;
					}else{
						System.out.println("从链路建立失败，其他啊原因。");
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return false;
	}
	
	public static List<Byte> getByteList(Byte b){
		List<Byte> target = new ArrayList<Byte>();
		target.add(b);
		target.addAll(ChangeType.intTo4Bytes(LogicMark.jym));
		return target;
	}
	
}
