package com.xwp.jt809.mina.client.hostLink;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderAdapter;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

import com.xwp.jt809.mina.model.MyMap;
import com.xwp.jt809.tools.ChangeType;
import com.xwp.jt809.tools.LogicMark;
import com.xwp.jt809.tools.PackageMessage;
import com.xwp.jt809.tools.Transform;

public class ClientProtocalEncode extends ProtocolEncoderAdapter {

	private Charset charset;
	private PackageMessage pckMsg = new PackageMessage();
	
	public ClientProtocalEncode( Charset charset){
		this.setCharset(charset);
	}
	
	public void encode(IoSession session, Object object, ProtocolEncoderOutput out)throws Exception {
		IoBuffer buf ;  
		if(object instanceof MyMap){
			MyMap m = (MyMap) object;
			List<Byte> list ;
			if(m.getValue() == (byte)-1){
				list = pckMsg.packagemsg(m.getName(),m.getValue());
			}else{
				list = pckMsg.packagemsg(m.getName(),getByteList(m.getValue()));
			}
			list = Transform.change(list);
			buf = IoBuffer.allocate(list.size()); 
			for(Byte bt:list){
				buf.put(bt);
			}
			buf.flip();
			out.write(buf);
		}else{
			out.write(object);
		}
	}

	public Charset getCharset() {
		return charset;
	}

	public void setCharset(Charset charset) {
		this.charset = charset;
	}
	private static List<Byte> getByteList(Byte b){
		List<Byte> target = new ArrayList<Byte>();
		target.add(b);
		target.addAll(ChangeType.intTo4Bytes(LogicMark.jym));
		return target;
	}
}
