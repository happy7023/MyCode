package com.xwp.code.TestCode.wordCount.spout;

import java.util.Map;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;

public class WordSpout extends BaseRichSpout {
	
	private static final long serialVersionUID = 1L;
	private static final String[] message = new String[]{"you are stupid","dog like hot","hot is hot"};
	private static final Logger log = LoggerFactory.getLogger(WordSpout.class);
	private Random rand = new Random();
	private SpoutOutputCollector collector;
	
	public void nextTuple() {
		int a = rand.nextInt(3);
		collector.emit(new Values(message[a]));
		log.info("send message:"+message[a]);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void open(Map arg0, TopologyContext arg1, SpoutOutputCollector collector) {
		this.collector = collector;
	}

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("message"));
	}

}
