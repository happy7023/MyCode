package com.xwp.jt809.wzjtj.tcpClient;

import java.nio.channels.SocketChannel;

public class ClientReadThread implements Runnable {

	private SocketChannel server;
	private SocketChannel client;

	public ClientReadThread(SocketChannel client, SocketChannel server){
		this.server = server;
		this.client = client;
	}

	public void run() {
		
	}
	
}
