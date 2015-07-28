package com.xwp.jt809.wzjtj.tcpServer;

import java.nio.*;
import java.nio.channels.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ReadThread implements Runnable{

	private BlockingQueue<Byte> bqueue = new LinkedBlockingQueue<Byte>();
	private SocketChannel hostLink;
	private SocketChannel fromLink;
	
	public ReadThread(SocketChannel hostLink,SocketChannel fromLink){
		this.hostLink = hostLink;
		this.fromLink = fromLink;
	}

	public void run(){
		try{
			new Thread(new WriteThread(bqueue,hostLink,fromLink)).start();			
			ByteBuffer echoBuffer = ByteBuffer.allocate(1);
			int a =0;
			int length = 0;
			while (true){
				echoBuffer.clear();
				if(null != hostLink){
					a = hostLink.read(echoBuffer);
				}else if(null != fromLink){
					a = fromLink.read(echoBuffer);
				}
				if (a == -1){
					break;
				}
				if (a > 0){
					byte[] b = echoBuffer.array();
					
					for(int i=0;i<b.length;i++){
						bqueue.add(b[i]);										
					}

//					if(length == 0){
//						for(int i=0;i<b.length;i++){
//							if(((Byte)b[i]).intValue() == 0x5b ){
//								bqueue.add(b[i]);
//								length = 1;
//							}else{
//								if(length == 1 ){
//									bqueue.add(b[i]);
//								}
//							}
//						}
//					}else{
//						for(int i=0;i<b.length;i++){
//							if(((Byte)b[i]).intValue() == 0x5d ){
//								bqueue.add(b[i]);
//								length = 0;
//								break;
//							}										
//							bqueue.add(b[i]);
//						}
//					}	
					echoBuffer.flip();
				}
			}
		}catch (Exception e){
			e.printStackTrace();
		}
	}
						
}
