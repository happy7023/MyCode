package com.common.sparkTest;

import java.util.Arrays;
import java.util.Iterator;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.*;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;

import scala.Tuple2;

public class App {
	public static void main( String[] args )
    {
//        String readme="hdfs://hadoop1:9000/test/11.txt";
        String readme="D:/workspace_spcon/sparkTest/1111.txt";
        SparkConf conf=new SparkConf().setAppName("airy's first spark app");
        conf.setMaster("local");
        JavaSparkContext sc =new JavaSparkContext(conf);		//创建java的sparkContext对象
//        JavaStreamingContext ssc = new JavaStreamingContext(conf, new Duration(1000));	//伪实时数据处理，每秒执行
//        JavaStreamingContext ssc2 = new JavaStreamingContext(sc,new Duration(1000));	//伪实时数据处理，每秒执行,创建方式2
//        ssc2.socketTextStream("localhost", 9999);//从tcp套接字获取文本数据
        sc.textFile(readme);//从文件系统获取数据，如HDFS
//        ssc2.textFileStream(readme);//直接读取简单文本文件
        

        JavaRDD<String> logData=sc.textFile(readme).cache();
        JavaRDD<String> logWord=logData.flatMap(new FlatMapFunction<String, String>() {

			public Iterator<String> call(String arg0) throws Exception {
				
				return Arrays.asList(arg0.split(" ")).iterator();
			}
        });
        JavaPairRDD<String, Integer> jprdd = logWord.mapToPair(new PairFunction<String, String, Integer>() {

			public Tuple2<String, Integer> call(String arg0) throws Exception {
				return new Tuple2(arg0,1);
			}
		});
        JavaPairRDD<String, Integer> countrdd = jprdd.reduceByKey(new Function2<Integer, Integer, Integer>() {
			
			public Integer call(Integer paramT1, Integer paramT2) throws Exception {
				// TODO Auto-generated method stub
				return paramT1+paramT2;
			}
		});
        System.out.println(countrdd.collect());
//        countrdd.saveAsTextFile("hdfs://hadoop1:9000/test/11.out");
        
    }
}