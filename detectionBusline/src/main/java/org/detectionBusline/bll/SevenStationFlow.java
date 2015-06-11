package org.detectionBusline.bll;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.detectionBusline.daos.GetStation;
import org.detectionBusline.model.BusGPS;
import org.detectionBusline.model.LngLat;
import org.detectionBusline.model.StationFlow;


public class SevenStationFlow {

	private static Map<String, LngLat> mapStation = new HashMap<String, LngLat>();
	
	private Map<String, Integer> mapCount = new HashMap<String, Integer>();
	/**一个小时内车牌对应车辆集合*/
	private Map<Integer,List<BusGPS>> carlist = new HashMap<Integer,List<BusGPS>>();
	
	private int distance =0;

	private SqlSessionFactory sqlSessionFactory;
	
	public SevenStationFlow(SqlSessionFactory sqlSessionFactory){
		this.sqlSessionFactory = sqlSessionFactory;
		mapStation.put("客运中心站", new LngLat(120.2757505,30.3122520));
		mapStation.put("杭州南站", new LngLat(120.1828817,30.2370643));
		mapStation.put("杭州西站", new LngLat(120.0870088,30.2634143));
		mapStation.put("杭州北站", new LngLat(120.1086381,30.3194618));
		mapStation.put("城战火车站", new LngLat(120.1774070,30.2465260));
		mapStation.put("火车东站", new LngLat(120.2068600,30.2923070));
		mapStation.put("萧山机场", new LngLat(120.4319560,30.2376910));
	}
	
	public void getSevenStationFlow(List<BusGPS> buslist,String t_date,int t_hour){
		
		
		SqlSession session =  sqlSessionFactory.openSession();
		GetStation get = (GetStation) session.getMapper(GetStation.class);
		for(BusGPS bus:buslist){
			if(!carlist.containsKey(bus.getNumb())){
				carlist.put(bus.getNumb(), new ArrayList<BusGPS>());
			}
			carlist.get(bus.getNumb()).add(bus);
		}
		
		for(Entry<Integer,List<BusGPS>> entry:carlist.entrySet()){
			List<BusGPS> tmp = entry.getValue();
			for(BusGPS bus:tmp){
				String stationame = "客运中心站";
				distance = CalculateDistance.getDistance(mapStation.get(stationame), new LngLat(bus.getLongitude(),bus.getLatitude()));
				if(distance < 100){
					if(mapCount.containsKey(stationame)){
						mapCount.put(stationame, mapCount.get(stationame)+1);
					}else{
						mapCount.put(stationame, 1);
					}
					break;
				}
			}
			for(BusGPS bus:tmp){
				String stationame = "杭州南站";
				distance = CalculateDistance.getDistance(mapStation.get(stationame), new LngLat(bus.getLongitude(),bus.getLatitude()));
				if(distance < 100){
					if(mapCount.containsKey(stationame)){
						mapCount.put(stationame, mapCount.get(stationame)+1);
					}else{
						mapCount.put(stationame, 1);
					}
					break;
				}
			}
			for(BusGPS bus:tmp){
				String stationame = "杭州西站";
				distance = CalculateDistance.getDistance(mapStation.get(stationame), new LngLat(bus.getLongitude(),bus.getLatitude()));
				if(distance < 100){
					if(mapCount.containsKey(stationame)){
						mapCount.put(stationame, mapCount.get(stationame)+1);
					}else{
						mapCount.put(stationame, 1);
					}
					break;
				}
			}
			for(BusGPS bus:tmp){
				String stationame = "杭州北站";
				distance = CalculateDistance.getDistance(mapStation.get(stationame), new LngLat(bus.getLongitude(),bus.getLatitude()));
				if(distance < 100){
					if(mapCount.containsKey(stationame)){
						mapCount.put(stationame, mapCount.get(stationame)+1);
					}else{
						mapCount.put(stationame, 1);
					}
					break;
				}
			}
			for(BusGPS bus:tmp){
				String stationame = "城战火车站";
				distance = CalculateDistance.getDistance(mapStation.get(stationame), new LngLat(bus.getLongitude(),bus.getLatitude()));
				if(distance < 100){
					if(mapCount.containsKey(stationame)){
						mapCount.put(stationame, mapCount.get(stationame)+1);
					}else{
						mapCount.put(stationame, 1);
					}
					break;
				}
			}
			for(BusGPS bus:tmp){
				String stationame = "火车东站";
				distance = CalculateDistance.getDistance(mapStation.get(stationame), new LngLat(bus.getLongitude(),bus.getLatitude()));
				if(distance < 100){
					if(mapCount.containsKey(stationame)){
						mapCount.put(stationame, mapCount.get(stationame)+1);
					}else{
						mapCount.put(stationame, 1);
					}
					break;
				}
			}
			for(BusGPS bus:tmp){
				String stationame = "萧山机场";
				distance = CalculateDistance.getDistance(mapStation.get(stationame), new LngLat(bus.getLongitude(),bus.getLatitude()));
				if(distance < 100){
					if(mapCount.containsKey(stationame)){
						mapCount.put(stationame, mapCount.get(stationame)+1);
					}else{
						mapCount.put(stationame, 1);
					}
					break;
				}
			}
		}
		for(Entry<String, Integer> cc:mapCount.entrySet()){
			StationFlow stationFlow = new StationFlow();
			stationFlow.setT_date(t_date);
			stationFlow.setT_hour(t_hour);
			stationFlow.setStation_name(cc.getKey());
			stationFlow.setSum_shifts(cc.getValue());
			get.addSevenStationFlow(stationFlow);
			
			System.out.println(stationFlow.getStation_name()+ ":" + stationFlow.getSum_shifts());
		}
		session.commit();
		session.close();
		carlist.clear();
		mapCount.clear();
	}
	
}
