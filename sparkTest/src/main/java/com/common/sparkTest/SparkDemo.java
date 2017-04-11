package com.common.sparkTest;

import java.io.File;  
import java.util.Arrays;  
  
import java.util.Iterator;

import org.apache.commons.io.FileUtils;  
import org.apache.spark.SparkConf;  
import org.apache.spark.api.java.JavaPairRDD;  
import org.apache.spark.api.java.JavaRDD;  
import org.apache.spark.api.java.JavaSparkContext;  
import org.apache.spark.api.java.function.FlatMapFunction;  
import org.apache.spark.api.java.function.Function;  
import org.apache.spark.api.java.function.Function2;  
import org.apache.spark.api.java.function.PairFunction;  
  






import scala.Tuple2;  
  
public class SparkDemo {  
    private static JavaSparkContext javaSparkContext = null;  
  
    public static void init(String appName, String master) {  
        SparkConf conf = new SparkConf().setAppName(appName).setMaster(master);  
        javaSparkContext = new JavaSparkContext(conf);  
    }  

  
    public static void main(String[] args) {
    	
    	String path = "D:\\workspace_spcon\\sparkTest\\1111.txt";
    	
    	JavaSparkContext jsc = new JavaSparkContext("local","testSpark");
    	JavaRDD<String> jrdd = jsc.textFile(path);
    	JavaRDD<String> jrdd2 = jrdd.filter(new Function<String, Boolean>() {

			public Boolean call(String s) throws Exception {
				return s.contains("222");
			}
		});
    	System.out.println(jrdd2.collect());
    	System.out.println(jrdd2.count());
      }  
}  