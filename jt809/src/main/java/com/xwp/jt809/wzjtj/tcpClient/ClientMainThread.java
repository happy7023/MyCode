package com.xwp.jt809.wzjtj.tcpClient;

import java.io.IOException;
import java.net.*;
import java.nio.*;
import java.nio.channels.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.xwp.jt809.wzjtj.model.LoginInfo;
import com.xwp.jt809.wzjtj.tools.ChangeType;
import com.xwp.jt809.wzjtj.tools.Encrypt;
import com.xwp.jt809.wzjtj.tools.LogicMark;
import com.xwp.jt809.wzjtj.tools.PackageMessage;
import com.xwp.jt809.wzjtj.tools.TabCode;
import com.xwp.jt809.wzjtj.tools.Transform;

public class ClientMainThread {
	
	static SocketChannel current;
	static SocketChannel hostLink;
	static SocketChannel fromLink;
	static PackageMessage p = new PackageMessage();
	static int verify ;
	
	public static void main(String[] args) throws InterruptedException {
		while(true){
			if(null == hostLink){
				try{
					SocketAddress address = new InetSocketAddress("localhost", 55555);
					hostLink = SocketChannel.open(address);
					hostLink.configureBlocking(false);		
					ByteBuffer buffer = ByteBuffer.allocate(100);
					boolean f = true;
					LoginInfo info = new LoginInfo(3303001,"123456","127.0.0.1",55556);
					List<Byte> listB = p.packagemsg(TabCode.UP_CONNECT_REQ,info.getBytes());
					listB = Transform.change(listB);
					buffer.clear();		
					for(Byte b :listB){
						buffer.put(b);
					}
					buffer.flip();
					hostLink.write(buffer);
					current = hostLink;
					
					List<Byte> list = new ArrayList<Byte>();
					ByteBuffer bufferbyte = ByteBuffer.allocate(1);
					int length = 0;
					int a;
					while (f){
						bufferbyte.clear();
						a = current.read(bufferbyte);
						if (a == -1){
							break;
						}
						if (a > 0){
							byte[] b = bufferbyte.array();
							
							if(length == 0){
								for(int i=0;i<b.length;i++){
									if(((Byte)b[i]).intValue() == 0x5b ){
										list.add(b[i]);
										length = 1;
									}else{
										if(length == 1 ){
											list.add(b[i]);
										}
									}
								}
							}else{
								for(int i=0;i<b.length;i++){
									if(((Byte)b[i]).intValue() == 0x5d ){
										list.add(b[i]);
										length = 2;
										break;
									}										
									list.add(b[i]);
								}
								if(length == 2){
									if(list.size() == 30){
										if(explainLogin(list)){
											list = p.packagemsg(TabCode.DOWN_CONNECT_RSP,(byte)0x00);
											list = Transform.change(list);
											buffer.clear();		
											for(Byte bb :list){
												buffer.put(bb);
											}
											buffer.flip();
											hostLink.write(buffer);
											System.out.println("从链路建立成功。");
											new Thread(new ClientWriteThread(fromLink,hostLink)).start();
										}else{
											hostLink.close();
										}
										
									}else if(list.size() == 31){
										if(explainLoginRsp(list)){
											System.out.println("主链路建立成功.");
											Selector selector = Selector.open();
											ServerSocketChannel ssc = ServerSocketChannel.open();
											ssc.configureBlocking(false);
											ServerSocket ss = ssc.socket();
											address = new InetSocketAddress(55556);
											ss.bind(address);
											ssc.register(selector, SelectionKey.OP_ACCEPT);
											System.out.println("从链路端口注册完毕!");
											selector.select();
											Set<SelectionKey> selectionKeys = selector.selectedKeys();
											Iterator<SelectionKey> iter = selectionKeys.iterator();											
											while (iter.hasNext()){
												SelectionKey key = iter.next();
												if ((key.readyOps() & SelectionKey.OP_ACCEPT) == SelectionKey.OP_ACCEPT){
													ServerSocketChannel subssc = (ServerSocketChannel) key.channel();
													fromLink = subssc.accept();
													fromLink.configureBlocking(false);
													fromLink.register(selector, SelectionKey.OP_READ);
													current = fromLink;
													iter.remove();
													System.out.println("有新连接:" + fromLink);
													break;
												}
											}
										}else{
											hostLink.close();
											f = false;
										}
										list.clear();
									} 
									length = 0;
								}
							}
							buffer.flip();
						}
					}
				}catch (Exception e){
					e.printStackTrace();
				}
			}else{
				Thread.sleep(1000);
			}
		}
	}
	
	/**
	 * 解析从链路请求
	 * @param blist
	 * @return
	 * @throws IOException
	 */
	private static boolean explainLogin(List<Byte> blist) throws IOException{
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
			if (MSGID == TabCode.DOWN_CONNECT_REQ) {
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
				byte[] VerifyCodeB = new byte[4];
				for(int i = 0;i<blist.size();i++){
					VerifyCodeB[i] = blist.get(i);
				}
				if(ChangeType.bytesTo4Int(VerifyCodeB, 0) == 0x1111){
					return true;
				}
			}
		}
		System.out.println("未知别的消息.");
		return false;
	}
	/**
	 * 解析主链路应答
	 * @param blist
	 * @return
	 * @throws IOException
	 */
	private static boolean explainLoginRsp(List<Byte> blist) throws IOException{
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
			if (MSGID == TabCode.UP_CONNECT_RSP) {
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
				byte b = blist.get(0);
				byte[] verifyBytes = {blist.get(1),blist.get(2),blist.get(3),blist.get(4)};
				if(b == 0x00){
					verify = ChangeType.bytesTo4Int(verifyBytes, 0);
					return true;
				}
			}
		}
		System.out.println("未知别的消息.数据长度:"+k);
		return false;
	}
}
