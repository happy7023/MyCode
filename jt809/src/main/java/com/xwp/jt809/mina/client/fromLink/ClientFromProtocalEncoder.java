package com.xwp.jt809.mina.client.fromLink;

import java.nio.charset.Charset;
import java.util.List;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderAdapter;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

import com.xwp.jt809.mina.model.MyMap;
import com.xwp.jt809.tools.PackageMessage;
import com.xwp.jt809.tools.Transform;
public class ClientFromProtocalEncoder extends ProtocolEncoderAdapter {
	
	private Charset charset;
	private PackageMessage pckMsg = new PackageMessage();

	public ClientFromProtocalEncoder(Charset charset){
		this.charset = charset;
	}

	public void encode(IoSession session, Object object,ProtocolEncoderOutput out) throws Exception {
		IoBuffer buf;  
		if(object instanceof MyMap){
			MyMap m = (MyMap) object;
			List<Byte> list = pckMsg.packagemsg(m.getName(),m.getValue());
			list = Transform.change(list);
			buf = IoBuffer.allocate(list.size()); 
			for(Byte bt:list){
				buf.put(bt);
			}
			buf.flip();
			out.write(buf);
		}
		
	}
}
