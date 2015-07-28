package com.xwp.jt809.wzjtj.tcpServer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

import com.xwp.jt809.wzjtj.tools.ChangeType;
import com.xwp.jt809.wzjtj.tools.DataExplain;
import com.xwp.jt809.wzjtj.tools.Encrypt;
import com.xwp.jt809.wzjtj.tools.LogicMark;
import com.xwp.jt809.wzjtj.tools.PackageMessage;
import com.xwp.jt809.wzjtj.tools.TabCode;
import com.xwp.jt809.wzjtj.tools.Transform;

public class WriteThread implements Runnable{
	
	private BlockingQueue<Byte> bqueue;
	PackageMessage p = new PackageMessage();
	private SocketChannel hostLink;
	private SocketChannel fromLink;
	ByteBuffer echoBuffer = ByteBuffer.allocate(1);
	ByteBuffer bufferWrite = ByteBuffer.allocate(100);
	PackageMessage pckMsg = new PackageMessage();
	DataExplain dataExplain = new DataExplain();
	
	public WriteThread(BlockingQueue<Byte> bqueue,SocketChannel hostLink,SocketChannel fromLink){
		this.bqueue = bqueue;
		this.hostLink = hostLink;
		this.fromLink = fromLink;
	}
	
	public void run(){
		Byte b = null;
		List<Byte> blist = new ArrayList<Byte>();
		byte[] megLength = new byte[4];
		byte[] megGnsscenterid = new byte[4];
		byte[] encryptKey = new byte[4];
		long start = System.currentTimeMillis();
		long end = System.currentTimeMillis();
		
		while (true) {
			try {
				b = bqueue.poll();
				if(null == b){
					end = System.currentTimeMillis();
					if((end -start) >= 3*60*1000){
						hostLink.close();
						return;
					}else{
						Thread.sleep(10);
						continue;
					}
				}
				if (b.intValue() == 0x5b) {
					blist.add(b);
					while (b.intValue() != 0x5d) {
						b = bqueue.take();
						blist.add(b);
					}
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
						blist = blist.subList(23, blist.size()-3);
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
						switch (MSGID) {
							case TabCode.UP_DISCONNECT_REQ:{
								byte[] userb = {blist.get(0),blist.get(1),blist.get(2),blist.get(3)};
								String userId = new String(userb);
								byte[] passwardb = new byte[8];
								for(int i=0;i<8;i++){
									passwardb[i] = blist.get(i+4);
								}
								int password = ChangeType.bytesTo4Int(passwardb, 0);
								sendMsg(pckMsg.packagemsg(TabCode.UP_DISCONNECT_RSP,LogicMark.SUCCESS));
								System.out.println("客户: "+userId+" 密码: "+password+" 主动断开连接.");
								hostLink.close();
								fromLink.close();
								blist.clear();
								break;
							}
							case TabCode.UP_LINKTEST_REQ:{
								sendMsg(pckMsg.packagemsg(TabCode.UP_LINKTEST_RSP,(byte)-1));
								start = System.currentTimeMillis();
								blist.clear();
								break;
							}
							case TabCode.UP_DISCONNECT_INFORM:{
								Byte errorCode = blist.get(0);
								if(errorCode.intValue() == 0x00){
									System.out.println("主链路断开，错误代码:"+0x00);
								}else{
									System.out.println("主链路断开,其他原因,错误代码:"+0x01);
								}
								blist.clear();
								break;
							}
							case TabCode.UP_CLOSELINK_INFORM:{
								Byte reasonCode = blist.get(0);
								if(reasonCode == 0x00){
									System.out.println("链路关闭，原因:网卡重启,错误代码:"+0x00);
								}else{
									System.out.println("链路关闭，原因:其他,错误代码:"+0x00);
								}
								blist.clear();
								break;
							}
							case TabCode.DOWN_CONNECT_RSP:{
								Byte result = blist.get(0);
								sendMsg(null);
								blist.clear();
								break;
							}
							case TabCode.DOWN_DISCONNECT_RSP:{
								System.out.println("从链路连接断开。");
								fromLink.close();
								blist.clear();
								break;
							}
							case TabCode.DOWN_LINKTEST_RSP:{
								sendMsg(pckMsg.packagemsg(TabCode.DOWN_LINKTEST_REQ,(byte)-1));
								blist.clear();
								break;
							}
							case TabCode.UP_EXG_MSG:{
								dataExplain.explainEXG(blist);
								blist.clear();
								break;
							}
							case TabCode.UP_PLATFORM_MSG:{
								dataExplain.explainPLATFORM(blist);
								blist.clear();
								break;
							}
							case TabCode.UP_WARN_MSG:{
								dataExplain.explainWARN(blist);
								blist.clear();
								break;
							}
							case TabCode.UP_CTRL_MSG:{
								dataExplain.explainCTRL(blist);
								blist.clear();
								break;
							}
							case TabCode.UP_BASE_MSG:{
								dataExplain.explainBASE(blist);
								blist.clear();
								break;
							}
							default: {
								System.out.println("数据未被正常解析,业务代码:"+gnsscenterid);
								blist.clear();
								break;  	
							}
						}
					}else{
						System.out.println("WriteThread--"+"数据未被正常解析,数据长度:"+blist.size());
						blist.clear();
						continue;
					}
				}
			}catch (Exception e){
				blist.clear();
				e.printStackTrace();
			}
		}
	}
	
	public void sendMsg(List<Byte> listByte){
		for(Byte b:listByte){
			echoBuffer.put(b);
		}
		try {
			if(null != fromLink){
				fromLink.write(echoBuffer);
			}else if(null != hostLink){
				hostLink.write(echoBuffer);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
