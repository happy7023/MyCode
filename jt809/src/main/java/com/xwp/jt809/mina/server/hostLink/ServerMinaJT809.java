package com.xwp.jt809.mina.server.hostLink;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;

import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.logging.LogLevel;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

public class ServerMinaJT809 {

	private static final int PORT = 55555;
	  
	public static void main(String[] args) throws IOException {
		IoAcceptor acceptor = new NioSocketAcceptor();
		LoggingFilter log = new LoggingFilter();
		log.setSessionOpenedLogLevel(LogLevel.DEBUG);
        acceptor.getFilterChain().addLast( "logger", log );
        //JT/T 809协议编码器
        acceptor.getFilterChain().addLast( "codec", new ProtocolCodecFilter( new JT809MessageCodecFactory( Charset.forName( "GBK" ))));
        //JT/T 协议处理类
        acceptor.setHandler(  new JT809MessageHandler() );
 
        acceptor.getSessionConfig().setReadBufferSize(2048*5000);
        acceptor.getSessionConfig().setIdleTime( IdleStatus.BOTH_IDLE, 60 );
        acceptor.bind(new InetSocketAddress(PORT));
        System.out.println("端口："+PORT+"  server start...");
	}
}
