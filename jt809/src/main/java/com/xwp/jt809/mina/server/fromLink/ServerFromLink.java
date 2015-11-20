package com.xwp.jt809.mina.server.fromLink;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;

import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

public class ServerFromLink implements Runnable{

	
	private String ip;
	private int port;

	public ServerFromLink(String ip,int port){
		this.ip = ip;
		this.port = port;
	}
	
	public void run(){
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		NioSocketConnector connector = new NioSocketConnector();
		connector.getFilterChain().addLast( "logger", new LoggingFilter() );
		connector.getFilterChain().addLast( "codec", new ProtocolCodecFilter( new ServerFromCodecFactory( Charset.forName( "UTF-8" )))); //设置编码过滤器
		connector.setHandler(new ServerFromLinkHandler());//设置事件处理器
		connector.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 60);
		ConnectFuture cf = connector.connect(
		new InetSocketAddress(ip, port));//建立连接
		cf.awaitUninterruptibly();//等待连接创建完成
		System.out.println("连接从链路..."+"   ip:"+ip+"   port:"+port);
	}
}
