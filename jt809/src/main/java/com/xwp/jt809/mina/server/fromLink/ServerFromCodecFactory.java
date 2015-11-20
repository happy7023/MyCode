package com.xwp.jt809.mina.server.fromLink;

import java.nio.charset.Charset;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;


public class ServerFromCodecFactory implements ProtocolCodecFactory{
	
	private final ServerFromProtocalEncode encoder;  
    private final ServerFromProtocalDecode decoder; 
    
    public ServerFromCodecFactory(Charset charset){
    	this.encoder = new ServerFromProtocalEncode(charset);
    	this.decoder = new ServerFromProtocalDecode(charset);
    }

	public ProtocolDecoder getDecoder(IoSession arg0) throws Exception {
		return decoder;
	}

	public ProtocolEncoder getEncoder(IoSession arg0) throws Exception {
		return encoder;
	}

}
