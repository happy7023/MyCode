package com.xwp.code.TestCode.wordCount.bolt;

import java.util.Map;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

public class SplitMessage extends BaseRichBolt {

	private OutputCollector collector;

	public void execute(Tuple tuple) {
		String message = (String) tuple.getValueByField("message");
		String[] words = message.split(" ");
		for(String word:words){
			collector.emit(new Values(word));
		}
	}

	public void prepare(Map arg0, TopologyContext arg1, OutputCollector collector) {
		this.collector = collector;		
	}

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("word"));
	}

}
