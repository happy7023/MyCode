package com.xwp.jt809.mina.client.fromLink;

import java.util.Date;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

import com.xwp.jt809.mina.model.MyMap;
import com.xwp.jt809.tools.LogicMark;
import com.xwp.jt809.tools.TabCode;

public class ClientFromHandler extends IoHandlerAdapter {
	
	@Override  
	public void exceptionCaught(IoSession session, Throwable cause)  
	    throws Exception {
		session.close(true);
	   cause.printStackTrace();  
	}  
	  
	@Override  
	public void messageReceived(IoSession session, Object message) throws Exception {
		if(message instanceof Integer){
			if((Integer)message == 0x1111){
				session.write(new MyMap(TabCode.DOWN_CONNECT_RSP, LogicMark.SUCCESS));
				System.out.println("从链路建立完成..."+"  "+session.getId());
			}
		}else if(message instanceof MyMap){
			MyMap myMap = (MyMap) message;
			switch (myMap.getName()) {
			case TabCode.DOWN_LINKTEST_RSP:
				System.out.println("收到从链路心跳保持应答。"+"  "+session.getId());
				break;
			default:
				break;
			}
		}
	} 
	  
	@Override  
	public void sessionIdle(IoSession session, IdleStatus status)throws Exception {  
		session.write(new MyMap(TabCode.DOWN_LINKTEST_REQ, (byte) -1));
		System.out.println("从链路60秒未收到消息，主动发送从链路保持心跳" + session.getIdleCount(status)+"  "+session.getId());
		if(session.getIdleCount(status) == 3){
			System.out.println("从链路180秒未收到消息断开连接 " + session.getIdleCount(status)+new Date());
			session.close(true);
		}	   
	}
}
