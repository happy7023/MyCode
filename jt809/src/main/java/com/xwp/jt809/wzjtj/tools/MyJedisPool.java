package com.xwp.jt809.wzjtj.tools;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

public class MyJedisPool {
	// jedis池
	private static ShardedJedisPool pool;
	private static String host = "172.16.2.201";
	private static int port = 6379;
	
	private static int maxActive = 100;
	private static int maxIdle = 20;
	private static long maxWait = 1000;
	private static boolean OnBorrow = true;
	private static boolean OnReturn = true;
	
	public static final int EXPIRE_TIME = 86400;

	public static void initPool() {
		
		ResourceBundle bundle = ResourceBundle.getBundle("redis");
		if (bundle == null) {
			throw new IllegalArgumentException("[redis.properties] is not found!");
		}
		
		maxActive = Integer.valueOf(bundle.getString("redis.pool.maxActive"));
		maxIdle = Integer.valueOf(bundle.getString("redis.pool.maxIdle"));
		maxWait = Long.valueOf(bundle.getString("redis.pool.maxWait"));
		OnBorrow = Boolean.valueOf(bundle.getString("redis.pool.testOnBorrow"));
		OnReturn = Boolean.valueOf(bundle.getString("redis.pool.testOnReturn"));
		host = bundle.getString("redis.ip");
		port = Integer.parseInt(bundle.getString("redis.port"));
		
	    JedisPoolConfig config = new JedisPoolConfig();
	    config.setMaxActive(maxActive);
	    config.setMaxIdle(maxIdle);
	    config.setMaxWait(maxWait);
	    config.setTestOnBorrow(OnBorrow);
	    config.setTestOnReturn(OnReturn);
	    
	    List<JedisShardInfo> jdsInfoList =new ArrayList<JedisShardInfo>(1);
	    JedisShardInfo jedisinfo = new JedisShardInfo(host,port);
//	    jedisinfo.setPassword("370771");
	    jdsInfoList.add(jedisinfo);
	    
	    pool = new ShardedJedisPool(config, jdsInfoList);
	}
	
	    public ShardedJedis getJedis() {
	        if (pool == null)
	            initPool();
	        return pool.getResource();
	    }
	    
	    public void returnJedis(ShardedJedis jedis) {
	        if (jedis != null)
	            pool.returnResource(jedis);
	    }
	}