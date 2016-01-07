package com.xwp.code.TestCode.wordCount.bolt;

import java.io.Reader;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Tuple;

public class WordCount extends BaseRichBolt {

	private static Map<String,Integer> mapCount;
	private static final Logger log = LoggerFactory.getLogger(WordCount.class);
	private SqlSessionFactory sqlSessionFactory;
	private Reader reader;
	private int taskid;
	
	public void execute(Tuple tuple) {
		String word = (String) tuple.getValueByField("word");
		if(mapCount.containsKey(word)){
			int c = mapCount.get(word)+1;
			mapCount.put(word,c);
		}else{
			mapCount.put(word,1);
		}
		System.out.println(taskid+"--单词："+word+" 统计个数："+mapCount.get(word));
	}

	public void prepare(Map arg0, TopologyContext arg1, OutputCollector arg2) {
		mapCount = new HashMap<String,Integer>();
		taskid = arg1.getThisTaskId();
		try{
			reader    = Resources.getResourceAsReader("DbConfiguration.xml");
			sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public void declareOutputFields(OutputFieldsDeclarer arg0) {
		
	}

}
