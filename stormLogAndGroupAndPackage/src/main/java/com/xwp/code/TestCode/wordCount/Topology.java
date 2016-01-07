package com.xwp.code.TestCode.wordCount;

import com.xwp.code.TestCode.wordCount.bolt.SplitMessage;
import com.xwp.code.TestCode.wordCount.bolt.WordCount;
import com.xwp.code.TestCode.wordCount.spout.WordSpout;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.tuple.Fields;

/**
 ****************** 日志测试*****************
 ****************** 分发测试*****************
 * <p>Title: Topology</p>
 * <p>Description: </p>
 * <p>Company: supconit</p> 
 * @author 伟平
 * @date 2016年1月7日 下午3:53:06
 */
public class Topology {

	public static void main(String[] args) {
		
		TopologyBuilder build = new TopologyBuilder();
		build.setSpout("spout", new WordSpout(),1);
		build.setBolt("message", new SplitMessage(),1).shuffleGrouping("spout");
		build.setBolt("count", new WordCount(),3).fieldsGrouping("message", new Fields("word"));
		
		Config conf = new Config();
		conf.setNumWorkers(4);
		conf.setMaxSpoutPending(10);
		conf.setDebug(false);
		
		LocalCluster cluster = new LocalCluster();
		cluster.submitTopology( "wordCcount",conf, build.createTopology());
	}
	
}
