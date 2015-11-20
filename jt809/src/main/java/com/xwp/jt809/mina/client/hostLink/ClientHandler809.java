package com.xwp.jt809.mina.client.hostLink;

import java.util.List;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

import com.xwp.jt809.mina.model.LoginInfo;
import com.xwp.jt809.mina.model.MyMap;
import com.xwp.jt809.tools.PackageMessage;
import com.xwp.jt809.tools.TabCode;
import com.xwp.jt809.tools.Transform;

public class ClientHandler809 extends IoHandlerAdapter{
	
	@Override
	public void sessionOpened(IoSession session) throws Exception {
		LoginInfo info = new LoginInfo(3303001,"123456","127.0.0.1",55556);
		PackageMessage p = new PackageMessage();
		List<Byte> listB = p.packagemsg(TabCode.UP_CONNECT_REQ,info.getBytes());
		listB = Transform.change(listB);
		IoBuffer buffer = IoBuffer.allocate(listB.size());
		for(Byte b :listB){
			buffer.put(b);
		}
		buffer.flip();
		session.write(buffer);
		System.out.println("发送主链路登录请求消息:"+info.toString()+"  "+session.getId());
	}
	@Override
	public void messageReceived(IoSession session, Object message) throws Exception {
		if(message instanceof MyMap){
			MyMap myMap = (MyMap) message;
			switch (myMap.getName()) {
			case TabCode.UP_LINKTEST_REQ:
				session.write(new MyMap(TabCode.UP_LINKTEST_RSP,(byte)-1));
				System.out.println("收到主链路心跳保持请求，并且应答。"+"  "+session.getId());
//				sendMsg(session);
				break; 

			default:
				break;
			}
		}
	}
	@Override  
	public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
		
	} 
} 
