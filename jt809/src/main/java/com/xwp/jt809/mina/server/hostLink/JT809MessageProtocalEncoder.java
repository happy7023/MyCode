package com.xwp.jt809.mina.server.hostLink;

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

public class JT809MessageProtocalEncoder extends ProtocolEncoderAdapter {
	
	private Charset charset;
	private PackageMessage pckMsg = new PackageMessage();

	public JT809MessageProtocalEncoder(Charset charset){
		this.charset = charset;
	}

	public void encode(IoSession session, Object object,ProtocolEncoderOutput out) throws Exception {
		IoBuffer buf;
		List<Byte> list = null;
		if(object instanceof MyMap){
			MyMap m = (MyMap) object;		
			if(m.getValue() == (byte)-1){
				list = pckMsg.packagemsg(m.getName(),m.getValue());
			}else{
				list = pckMsg.packagemsg(m.getName(),getByteList(m.getValue()));
			}
			
		}else if(object instanceof List){
			list = (List<Byte>) object;
		}else{
			System.out.println("位置别的发送消息。");
			return;
		}
		list = Transform.change(list);
		buf = IoBuffer.allocate(list.size());  
		for(Byte bt:list){
			buf.put(bt);
		}
		buf.flip();
		out.write(buf);
		
	}

	private static List<Byte> getByteList(Byte b){
		List<Byte> target = new ArrayList<Byte>();
		target.add(b);
		target.addAll(ChangeType.intTo4Bytes(LogicMark.jym));
		return target;
	}
	
}
