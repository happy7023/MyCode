package com.xwp.jt809.mina.client.fromLink;

import java.nio.charset.Charset;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;

public class ClientFromCodecFactory implements ProtocolCodecFactory {
	
	private final ClientFromProtocalEncoder encoder;  
    private final ClientFromProtocalDecoder decoder;  
      
    public ClientFromCodecFactory(Charset charset) {  
        encoder=new ClientFromProtocalEncoder(charset);  
        decoder=new ClientFromProtocalDecoder(charset);  
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
