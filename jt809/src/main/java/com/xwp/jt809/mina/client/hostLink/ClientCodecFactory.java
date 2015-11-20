package com.xwp.jt809.mina.client.hostLink;

import java.nio.charset.Charset;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;


public class ClientCodecFactory implements ProtocolCodecFactory{
	
	private final ClientProtocalEncode encoder;  
    private final ClientProtocalDecode decoder; 
    
    public ClientCodecFactory(Charset charset){
    	this.encoder = new ClientProtocalEncode(charset);
    	this.decoder = new ClientProtocalDecode(charset);
    }

	public ProtocolDecoder getDecoder(IoSession arg0) throws Exception {
		return decoder;
	}

	public ProtocolEncoder getEncoder(IoSession arg0) throws Exception {
		return encoder;
	}

}
