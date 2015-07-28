package com.xwp.jt809.wzjtj.tcpClient;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.List;

import redis.clients.jedis.ShardedJedis;

import com.xwp.jt809.wzjtj.model.VehicleLocation;
import com.xwp.jt809.wzjtj.tools.JsonUtil;
import com.xwp.jt809.wzjtj.tools.MyJedisPool;
import com.xwp.jt809.wzjtj.tools.PackageMessage;
import com.xwp.jt809.wzjtj.tools.TabCode;
import com.xwp.jt809.wzjtj.tools.Transform;

public class ClientWriteThread implements Runnable{

	private SocketChannel hostLink;
	private SocketChannel fromLink;
	private PackageMessage p = new PackageMessage();
	private MyJedisPool myJedisPool = new MyJedisPool();
	private ShardedJedis jedis;
	private ByteBuffer buffer = ByteBuffer.allocate(100);

	public ClientWriteThread(SocketChannel fromLink, SocketChannel hostLink){
		this.fromLink = fromLink;
		this.hostLink = hostLink;
	}

	public void run() {
		while(true){
			try {
				jedis = myJedisPool.getJedis();
				String gpsJson = jedis.rpop("gps");
				myJedisPool.returnJedis(jedis);
				VehicleLocation vehiclelocation = JsonUtil.jsonToObject(gpsJson, VehicleLocation.class);
				List<Byte> listBytes = p.packagemsg(TabCode.UP_EXG_MSG,vehiclelocation.getBytes());
				listBytes = Transform.change(listBytes);
				buffer.clear();
				for(Byte b:listBytes){
					buffer.put(b);
				}
				buffer.flip();
				if(null != hostLink){
					hostLink.write(buffer);
				}else if(null != fromLink){
					fromLink.write(buffer);
				}
				System.out.println("客户端发送数据成功。");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
}
