package com.xwp.jt809.mina.client.hostLink;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

public class ClientMinaJT809 {

	public static void main(String[] args) throws InterruptedException {
		// 创建客户端连接器.
		NioSocketConnector connector = new NioSocketConnector();
		connector.getFilterChain().addLast( "logger", new LoggingFilter() );
		connector.getFilterChain().addLast( "codec", new ProtocolCodecFilter( new ClientCodecFactory( Charset.forName( "UTF-8" )))); //设置编码过滤器
		connector.setHandler(new ClientHandler809());//设置事件处理器
		connector.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 60);
		ConnectFuture cf = connector.connect(
		new InetSocketAddress("127.0.0.1", 55555));//建立连接
		cf.awaitUninterruptibly();//等待连接创建完成
		IoBuffer buf = IoBuffer.allocate(200);
		
		IoSession session = cf.getSession();
		Thread.sleep(1000);
		while(true){
			byte[] b = {91,0,0,0,94,0,1,-104,80,18,0,0,50,102,89,1,0,0,0,0,0,-120,89,-43,-29,65,69,54,54,49,57,0,0,0,0,0,0,0,0,0 ,0, 0, 0, 0, 2, 18, 2, 0, 0, 0, 40, 0, 9, 11, 7, -33, 16, 0, 3, 7, 29, -75, 5, 1, -67, -18, -65, 0, 43, 0, 45, 0, 0, -51, -79, 0, -115, 0, 79, 0, 12, 0, -61, 0, 0, 0, 0, 0, 10, -82, 115, 18, -110, 93};
			buf.clear();
			buf.put(b);
			buf.flip();
			if(buf.hasRemaining()){
				session.write(buf);
			}
			Thread.sleep(2);
		}
	}
}
