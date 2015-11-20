package com.xwp.jt809.mina.server.hostLink;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

import redis.clients.jedis.ShardedJedis;

import com.xwp.jt809.mina.model.LoginInfo;
import com.xwp.jt809.mina.model.MyMap;
import com.xwp.jt809.mina.model.VehicleLocation;
import com.xwp.jt809.mina.server.fromLink.ServerFromLink;
import com.xwp.jt809.tools.ChangeType;
import com.xwp.jt809.tools.JsonUtil;
import com.xwp.jt809.tools.LogicMark;
import com.xwp.jt809.tools.MyJedisPool;
import com.xwp.jt809.tools.PackageMessage;
import com.xwp.jt809.tools.TabCode;
import com.xwp.jt809.tools.Tools;

public class JT809MessageHandler extends IoHandlerAdapter {
	
	private MyJedisPool myJedisPool = new MyJedisPool();
	private ShardedJedis redis;
	private int count =0;
	private long firstOne = 0;	//每10000的第一个接收时间
	private long lastOne = 0;	//每10000的最后一个接收时间
	private final Calendar cal = Calendar.getInstance();
	//2、取得时间偏移量：    
    private final int zoneOffset = cal.get(Calendar.ZONE_OFFSET);   
    //3、取得夏令时差：    
    private final int dstOffset = cal.get(Calendar.DST_OFFSET);
    private PackageMessage pckMsg = new PackageMessage();
	
	@Override  
	public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
		session.close(true);
		cause.printStackTrace();  
	}  
	  
	@Override  
	public void messageReceived(IoSession session, Object message) throws Exception {
		if(message instanceof VehicleLocation){
			VehicleLocation vehi = (VehicleLocation) message;
			redis = myJedisPool.getJedis();
			redis.lpush("gps", JsonUtil.objectToJson(vehi));
			myJedisPool.returnJedis(redis);
			count++;
//			System.out.println(count+"接收到车辆GPS信息："+vehi.getExg().getVehivleNo()+"("+vehi.getLongi()+","+vehi.getLati()+")");
			if(count ==1){
				cal.setTime(new Date());
				cal.add(Calendar.MILLISECOND, -(zoneOffset + dstOffset));
				firstOne = cal.getTimeInMillis();
			}
			if(count>10000){
				cal.setTime(new Date());
				cal.add(Calendar.MILLISECOND, -(zoneOffset + dstOffset));
				lastOne = cal.getTimeInMillis();
				List<Byte> blist = new ArrayList<Byte>();
				blist = Tools.combine(blist, ChangeType.intTo4Bytes(count), 4);
				blist = Tools.combine(blist, ChangeType.longToBytes(firstOne), 8);
				blist = Tools.combine(blist, ChangeType.longToBytes(lastOne), 8);
				session.write(pckMsg.packagemsg(TabCode.DOWN_TOTAL_RECV_BACK_MSG, blist));
				System.out.println("session:"+session.getId()+"  接收到10000数据。"+"  系统时间："+System.currentTimeMillis());
				count = 0;
			}
		}else if(message instanceof LoginInfo){
			LoginInfo loginInfo = (LoginInfo) message;
			if (loginInfo.getUserId() != LogicMark.USERNAME) {
				session.write(new MyMap(TabCode.UP_CONNECT_RSP,LogicMark.USERNAME_ERROR));	//用户未注册	
				session.close(true);
			} else if (!loginInfo.getPassward().equals(LogicMark.PASSWORD)) {
				session.write(new MyMap(TabCode.UP_CONNECT_RSP,LogicMark.PASSWORD_ERROR));	//	
				session.close(true);
			} else {
				session.write(new MyMap(TabCode.UP_CONNECT_RSP,LogicMark.SUCCESS));
				System.out.println("用户验证成功--用户名:"+loginInfo.getUserId()+" 密码:"+loginInfo.getPassward()+"  "+session.getId());
				Thread t = new Thread(new ServerFromLink(loginInfo.getDownLinkIP(),loginInfo.getDownLinkPort()));
				t.start();
			}
		}else if(message instanceof MyMap){
			MyMap myMap = (MyMap) message;
			switch (myMap.getName()) {
			case TabCode.UP_DISCONNECT_RSP:	//主链路注销请求应答
				session.write(new MyMap(TabCode.UP_DISCONNECT_RSP,(byte)-1));
				break;
			
			case TabCode.UP_LINKTEST_RSP:		//心跳保持
				System.out.println("收到主链路心跳保持应答。"+"  "+session.getId());
				break;
				
			default:
				System.out.println("未被解析的消息："+myMap.getName()+"  "+session.getId());
				break;
			}
		}
	} 
	  
	@Override  
	public void sessionIdle(IoSession session, IdleStatus status)throws Exception {  
		session.write(new MyMap(TabCode.UP_LINKTEST_REQ,(byte) -1));
		System.out.println("服务器60秒未收到消息，主动发送主链路保持心跳 " + session.getIdleCount(status));
		if(session.getIdleCount(status) == 3){
			System.out.println("服务器180秒未收到消息断开连接 " + session.getIdleCount(status)+new Date());
			session.close(true);
		}  
	}
}
