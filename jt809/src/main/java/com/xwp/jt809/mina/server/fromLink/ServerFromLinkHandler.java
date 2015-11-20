package com.xwp.jt809.mina.server.fromLink;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

import com.xwp.jt809.mina.model.MyMap;
import com.xwp.jt809.tools.ChangeType;
import com.xwp.jt809.tools.PackageMessage;
import com.xwp.jt809.tools.TabCode;
import com.xwp.jt809.tools.Transform;

public class ServerFromLinkHandler extends IoHandlerAdapter {

	public Map<IoSession,Integer> mapSession = new HashMap<IoSession,Integer>();
	
	@Override  
	public void exceptionCaught(IoSession session, Throwable cause)  
	    throws Exception {  
	   cause.printStackTrace();  
	}  
	
	@Override
	public void sessionOpened(IoSession session) throws Exception {
		PackageMessage pmsg = new PackageMessage();
		List<Byte> listB = pmsg.packagemsg(TabCode.DOWN_CONNECT_REQ,ChangeType.intTo4Bytes(0x1111));
		listB = Transform.change(listB);
		IoBuffer buf = IoBuffer.allocate(listB.size());
		for(Byte b :listB){
			buf.put(b);
		}
		buf.flip();
		session.write(buf);
		System.out.println("发送从链路连接请求...");
	}
	  
	@Override  
	public void messageReceived(IoSession session, Object message) throws Exception {
		if(message instanceof MyMap){
			MyMap myMap = (MyMap) message;
			switch (myMap.getName()) {
			case TabCode.DOWN_CONNECT_RSP:
				if(myMap.getValue() == 0x00){
					System.out.println("从链路建立成功。"+"  "+session.getId());
				}else{
					session.close(true);
					System.out.println("从链路建立失败，错误代码："+myMap.getValue());
				}
				break;			
			case TabCode.DOWN_LINKTEST_REQ:
				session.write(new MyMap(TabCode.DOWN_LINKTEST_RSP, (byte)-1));
				System.out.println("收到从链路心跳保持请求，并且回复"+"  "+session.getId());
				break;
			default:
				break;
			}
		}else{
			session.write(message);
		}
	} 
	  
	@Override  
	public void sessionIdle(IoSession session, IdleStatus status)throws Exception { 
	   	   
	}
	
}
