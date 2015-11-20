package org.stormACK;

import org.stormACK.bolt.ReliableSplitSentenceBolt;
import org.stormACK.spout.ReliableSentenceSpout;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.generated.AlreadyAliveException;
import backtype.storm.generated.InvalidTopologyException;
import backtype.storm.topology.TopologyBuilder;

public class TopologyMain {

	public static void main(String[] args) {
		
		TopologyBuilder builder = new TopologyBuilder();	
		builder.setSpout("redis", new ReliableSentenceSpout(),1);
		builder.setBolt("count", new ReliableSplitSentenceBolt(),2).shuffleGrouping("redis");
	
		Config conf = new Config();
		conf.setNumWorkers(4);
		conf.setDebug(false);
		
		if(args.length == 0){
			LocalCluster cluster = new LocalCluster();
			cluster.submitTopology("testGPS",conf, builder.createTopology());			
		}else{
			try {
				StormSubmitter.submitTopology("COUNT", conf, builder.createTopology());
			} catch (AlreadyAliveException e) {
				e.printStackTrace();
			} catch (InvalidTopologyException e) {
				e.printStackTrace();
			}
		}

	}
}
