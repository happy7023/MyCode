package com.xwp.jt809.mina.client.fromLink;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;

import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

public class ClientFromLink implements Runnable {

	public void run() {

		IoAcceptor acceptor = new NioSocketAcceptor();
        acceptor.getFilterChain().addLast( "logger", new LoggingFilter() );
        //JT/T 809协议编码器
        acceptor.getFilterChain().addLast( "codec", new ProtocolCodecFilter( new ClientFromCodecFactory( Charset.forName( "GBK" ))));
        //JT/T 协议处理类
        acceptor.setHandler(  new ClientFromHandler() );
 
        acceptor.getSessionConfig().setReadBufferSize(1024);
        acceptor.getSessionConfig().setIdleTime( IdleStatus.BOTH_IDLE, 60 );
        try {
			acceptor.bind(new InetSocketAddress(55556));
		} catch (IOException e) {
			e.printStackTrace();
		}
        System.out.println("从链路端口开启..."+System.currentTimeMillis());
	}

}
