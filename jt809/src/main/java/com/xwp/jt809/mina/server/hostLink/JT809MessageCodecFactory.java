package com.xwp.jt809.mina.server.hostLink;

import java.nio.charset.Charset;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;

public class JT809MessageCodecFactory implements ProtocolCodecFactory {
	
	private final JT809MessageProtocalEncoder encoder;  
    private final JT809MessageProtocalDecoder decoder;  
      
    public JT809MessageCodecFactory(Charset charset) {  
        encoder=new JT809MessageProtocalEncoder(charset);  
        decoder=new JT809MessageProtocalDecoder(charset);  
    }

	public ProtocolDecoder getDecoder(IoSession paramIoSession)
			throws Exception {
		return decoder;
	}

	public ProtocolEncoder getEncoder(IoSession paramIoSession)
			throws Exception {
		return encoder;
	}

}
