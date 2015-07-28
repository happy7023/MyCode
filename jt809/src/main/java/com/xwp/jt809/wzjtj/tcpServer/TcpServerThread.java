package com.xwp.jt809.wzjtj.tcpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
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
/**
 * 程序入口
 * @author 伟平
 *
 */
public class TcpServerThread {

	/**用于链路建立过程中数据接收*/
	private static List<Byte> listLogin = new ArrayList<Byte>();
	private static SocketChannel fromLink;
	private static SocketChannel hostLink;
	private static SocketChannel current;
	/**数据打包工具*/
	private static PackageMessage pmsg = new PackageMessage();
	private static SocketAddress downaddress;
	/**数据发送容器*/
	private static ByteBuffer bytebuffer = ByteBuffer.allocate(100);

	public static void main(String[] args) {
		try{
			Selector selector = Selector.open();
			ServerSocketChannel ssc = ServerSocketChannel.open();
			ssc.configureBlocking(false);
			ServerSocket ss = ssc.socket();
			InetSocketAddress address = new InetSocketAddress(55555);
			ss.bind(address);
			ssc.register(selector, SelectionKey.OP_ACCEPT);
			System.out.println("端口注册完毕!");
			
			while (true){
				try{
					selector.select();
					Set<SelectionKey> selectionKeys = selector.selectedKeys();
					Iterator<SelectionKey> iter = selectionKeys.iterator();
					ByteBuffer echoBuffer = ByteBuffer.allocate(1);
					
					while (iter.hasNext()){
						SelectionKey key = iter.next();
						if ((key.readyOps() & SelectionKey.OP_ACCEPT) == SelectionKey.OP_ACCEPT){
							ServerSocketChannel subssc = (ServerSocketChannel) key.channel();
							hostLink = subssc.accept();
							hostLink.configureBlocking(false);
							hostLink.register(selector, SelectionKey.OP_READ);
							current = hostLink;
							iter.remove();
							System.out.println("有新连接:" + hostLink);
						}else if ((key.readyOps() & SelectionKey.OP_READ) == SelectionKey.OP_READ){
							//验证用户登陆信息
							hostLink = (SocketChannel) key.channel();
							int length = 0;
							boolean userBoolean = true;
							int a;
							while (userBoolean){
								echoBuffer.clear();
								a = current.read(echoBuffer);
								if (a == -1){
									break;
								}
								if (a > 0){
									byte[] b = echoBuffer.array();
									
									if(length == 0){
										for(int i=0;i<b.length;i++){
											if(((Byte)b[i]).intValue() == 0x5b ){
												listLogin.add(b[i]);
												length = 1;
											}else{
												if(length == 1 ){
													listLogin.add(b[i]);
												}
											}
										}
									}else{
										for(int i=0;i<b.length;i++){
											if(((Byte)b[i]).intValue() == 0x5d ){
												listLogin.add(b[i]);
												length = 2;
												break;
											}										
											listLogin.add(b[i]);
										}
										if(length == 2){
											if(listLogin.size() == 72){
												if(explainLogin(listLogin)){
													fromLink = SocketChannel.open(downaddress);
													fromLink.configureBlocking(false);
													listLogin = pmsg.packagemsg(TabCode.DOWN_CONNECT_REQ,ChangeType.intTo4Bytes(0x1111));
													
													bytebuffer.clear();
													for(Byte bt:listLogin){
														bytebuffer.put(bt);
													}
													bytebuffer.flip();
													fromLink.write(bytebuffer);	//发送建立从链路请求
												}else {
													hostLink.close();
													userBoolean = false;
												}
											}else if(listLogin.size() == 27){
												if(explainFromLink(listLogin)){
													new Thread(new ReadThread(hostLink,fromLink)).start();
													System.out.println("读写线程成功启动.");
												}
												userBoolean = false;
												while(hostLink != null){
													Thread.sleep(1000);
												}
											}else{
												System.out.println("TcpServerThread--"+"数据未被正常解析，数据长度："+listLogin.size());
											}
											length = 0;
											listLogin.clear();
										}
									}
									echoBuffer.flip();
								}
							}
							iter.remove();
						}
					}
				}catch (Exception e){
					e.printStackTrace();
					hostLink.close();
					continue;
				}
			}
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	/**
	 * 解析用户登录
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
			if (MSGID == TabCode.UP_CONNECT_REQ) {
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
				LoginInfo loginInfo = new LoginInfo(blist);
				try {
					if (loginInfo.getUserId() != LogicMark.USERNAME) {
						List<Byte> list = pmsg.packagemsg(TabCode.UP_CONNECT_RSP,getByteList(LogicMark.USERNAME_ERROR));
						list = Transform.change(list);
						bytebuffer.clear();
						for(Byte bt:list){
							bytebuffer.put(bt);
						}
						bytebuffer.flip();
						hostLink.write(bytebuffer);	//用户未注册	
						hostLink.close();
						return false;
					} else if (!loginInfo.getPassward().equals(LogicMark.PASSWORD)) {
						List<Byte> list = pmsg.packagemsg(TabCode.UP_CONNECT_RSP,getByteList(LogicMark.PASSWORD_ERROR));
						bytebuffer.clear();
						for(Byte bt:list){
							bytebuffer.put(bt);
						}
						bytebuffer.flip();
						hostLink.write(bytebuffer);	//密码错误
						hostLink.close();
						return false;
					} else {
						List<Byte> list = pmsg.packagemsg(TabCode.UP_CONNECT_RSP,getByteList(LogicMark.SUCCESS));
						list = Transform.change(list);
						bytebuffer.clear();
						for(Byte bt:list){
							bytebuffer.put(bt);
						}
						System.out.println("用户验证成功--用户名:"+loginInfo.getUserId()+" 密码:"+loginInfo.getPassward());
						bytebuffer.flip();
						hostLink.write(bytebuffer);	//连接成功
						setDownaddress(new InetSocketAddress(loginInfo.getDownLinkIP(), loginInfo.getDownLinkPort()));
						return true;
					}
				} catch (IOException e) {
					e.printStackTrace();
					List<Byte> list = pmsg.packagemsg(TabCode.UP_CONNECT_RSP,getByteList(LogicMark.OTHER));
					bytebuffer.clear();
					for(Byte bt:list){
						bytebuffer.put(bt);
					}
					bytebuffer.flip();
					hostLink.write(bytebuffer);	//其他原因	
					hostLink.close();
					return false;
				}
			}
		}
		System.out.println("TcpServerThread--"+"接入码或者数据长度有问题.连接失败");
		return false;
	} 
	/**
	 * 解析从链路应答
	 * @param blist
	 * @return
	 * @throws IOException 
	 */
	private static boolean explainFromLink(List<Byte> blist) throws IOException{
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
	
	private static List<Byte> getByteList(Byte b){
		List<Byte> target = new ArrayList<Byte>();
		target.add(b);
		target.addAll(ChangeType.intTo4Bytes(LogicMark.jym));
		return target;
	}
	public static SocketAddress getDownaddress() {
		return downaddress;
	}
	public static void setDownaddress(SocketAddress downaddress) {
		TcpServerThread.downaddress = downaddress;
	}
}
