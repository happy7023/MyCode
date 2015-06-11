package org.detectionBusline.bll;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jodd.util.StringUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConfigParser {
	private transient static final Logger	logger	= LoggerFactory.getLogger(ConfigParser.class);

	private int								threadNum;

	private List<Map<String, String>>		gisDatasources;
	private List<Map<String, String>>		redises;
	private Map<String, String>				businessDatasource;

	private boolean							parsed	= false;

	private String							nodeId;
	
	//private Map<String, String> csAlarmJyfwDicMap = new HashMap<String, String>();   //报警经营范围编码  Xiangliang Kong 2013-01-29
	private Map<String, String> cllxDicMap = new HashMap<String, String>(); ;   	   //车辆类型编码  Xiangliang Kong 2013-03-11
	
	private static final Pattern			PATTERN	= Pattern.compile("^(\\w+?)\\[(\\d+)\\]\\.(\\w+)$");

	private void parse() throws IOException {
		
		
		Properties properties = new Properties();
		String configPath = System.getProperty("reduce.config");
		if (null != configPath && configPath.trim().length() > 0) {
			try {
				properties.load(new BufferedInputStream(new FileInputStream(new File(configPath))));
			} catch (Exception e) {
			}
		}
		if (properties.isEmpty()) {
			InputStream is = ConfigParser.class.getResourceAsStream("/config.properties");
			properties.load(is);
		}
		// TODO more path search.
		if (properties.isEmpty())
			throw new IOException("not found config.properties");
		

		String threadNum = (String) properties.get("thread_num");
		if (StringUtil.isNotBlank(threadNum))
			this.threadNum = Integer.valueOf(threadNum);

		if (StringUtil.isNotBlank(nodeId))
			this.nodeId = properties.getProperty("node_id");
		
		//初始化车辆类型cllx的数据字典设置   Xiangliang Kong  2013-03-11
		String dicCllxBanche = (String) properties.get("dic_cllx_banche");
		String dicCllxBaoche = (String) properties.get("dic_cllx_baoche");
		String dicCllxWei = (String) properties.get("dic_cllx_wei");
		String dicCllxTaxi = (String) properties.get("dic_cllx_taxi");
		String dicCllxGong = (String) properties.get("dic_cllx_gong");
		String dicCllxPthc = (String) properties.get("dic_cllx_pthc");
		String dicCllxKygj = (String) properties.get("dic_cllx_kygj");
		this.cllxDicMap.put("banche", dicCllxBanche);
		this.cllxDicMap.put("baoche", dicCllxBaoche);
		this.cllxDicMap.put("wei", dicCllxWei);
		this.cllxDicMap.put("taxi", dicCllxTaxi);
		this.cllxDicMap.put("gong", dicCllxGong);
		this.cllxDicMap.put("pthc", dicCllxPthc);
		this.cllxDicMap.put("kygj", dicCllxKygj);
			
		//初始化经营范围Jyfw的数据字典设置   Xiangliang Kong  2013-01-29
/*		String dicJyfw = (String) properties.get("dic_jyfw");
		String dicBanche = (String) properties.get("dic_banche");
		String dicBaoche = (String) properties.get("dic_baoche");
		String dicWeixian = (String) properties.get("dic_weixian");
		if (StringUtil.isNotBlank(dicJyfw))
			this.csAlarmJyfwDicMap.put("jyfw", dicJyfw);
		if (StringUtil.isNotBlank(dicBanche))
			this.csAlarmJyfwDicMap.put("banche", dicBanche);
		if (StringUtil.isNotBlank(dicBaoche))
			this.csAlarmJyfwDicMap.put("baoche", dicBaoche);
		if (StringUtil.isNotBlank(dicWeixian))
			this.csAlarmJyfwDicMap.put("weixian", dicWeixian);*/

		Map<Integer, Map<String, String>> gisDatasources = new HashMap<Integer, Map<String, String>>();
		Map<Integer, Map<String, String>> redises = new HashMap<Integer, Map<String, String>>();

		for (Entry<Object, Object> entry : properties.entrySet()) {
			String key = (String) entry.getKey();
			String val = (String) entry.getValue();
			if (null == key || key.length() == 0)
				continue;
			Matcher matcher = PATTERN.matcher(key);
			if (matcher.matches()) {
				if ("gis_ds".equals(matcher.group(1))) {
					Integer idx = Integer.valueOf(matcher.group(2));
					Map<String, String> map = gisDatasources.get(idx);
					if (null == map) {
						map = new HashMap<String, String>();
						gisDatasources.put(idx, map);
					}
					map.put(matcher.group(3), val.trim());
				} else if ("redis".equals(matcher.group(1))) {
					Integer idx = Integer.valueOf(matcher.group(2));
					Map<String, String> map = redises.get(idx);
					if (null == map) {
						map = new HashMap<String, String>();
						redises.put(idx, map);
					}
					map.put(matcher.group(3), val.trim());
				} else if ("business_ds".equals(matcher.group(1))) {
					if (null == businessDatasource)
						businessDatasource = new HashMap<String, String>();
					businessDatasource.put(matcher.group(3), val.trim());
				}
			}
		}

		if (!gisDatasources.isEmpty()) {
			this.gisDatasources = new ArrayList<Map<String, String>>(gisDatasources.values());
		}
		if (!redises.isEmpty()) {
			this.redises = new ArrayList<Map<String, String>>(redises.values());
		}
		this.parsed = true;
	}

	public List<Map<String, String>> getGisDatasources() {
		if (!parsed) {
			try {
				parse();
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
			}
		}
		return gisDatasources;
	}

	public List<Map<String, String>> getRedises() {
		if (!parsed) {
			try {
				parse();
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
			}
		}
		return redises;
	}

	public Map<String, String> getBusinessDatasource() {
		if (!parsed) {
			try {
				parse();
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
			}
		}
		return businessDatasource;
	}

	public int getThreadNum() {
		if (!this.parsed)
			try {
				parse();
			} catch (IOException e) {
			}
		return threadNum;
	}

	public String getNodeId() {
		if (!this.parsed)
			try {
				parse();
			} catch (IOException e) {
			}
		return nodeId;
	}

	public Map<String, String> getCllxDicMap() {
		if (!this.parsed)
			try {
				parse();
			} catch (IOException e) {
			}
		return cllxDicMap;
	}
	
	
}
