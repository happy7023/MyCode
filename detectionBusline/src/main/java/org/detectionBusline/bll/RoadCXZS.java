package org.detectionBusline.bll;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.detectionBusline.daos.GetLineMessage;
import org.detectionBusline.model.BusCXZS;
import org.detectionBusline.model.BusGPS;
import org.detectionBusline.model.LngLat;

public class RoadCXZS {
	
	/**道路ID对应该道路上的GPS点*/
	private static Map<Long,List<BusGPS>> carmap = new HashMap<Long,List<BusGPS>>();
	
	private long roadId;
	
	private String tablename = "T_BUS_ROAD_SPEED_CXZS";

	private SqlSessionFactory sqlSessionFactory;
	
	public RoadCXZS(SqlSessionFactory sqlSessionFactory){
		this.sqlSessionFactory = sqlSessionFactory;
	}

	public void getRoadCXZS(List<BusGPS> buslist,String t_date,int t_hour){
		
		SqlSession session = sqlSessionFactory.openSession();
		GetLineMessage lineMessage = session.getMapper(GetLineMessage.class);
		
		for(BusGPS bus:buslist){
			roadId = LocateGPS.locate(new LngLat(bus.getLongitude(),bus.getLatitude()),bus.getAngle());
			if(carmap.containsKey(bus.getLineID())){
				carmap.get(roadId).add(bus);
			}else{
				List<BusGPS> list = new ArrayList<BusGPS>();
				list.add(bus);
				carmap.put(roadId, list);
			}
		}
		
		for(Entry<Long, List<BusGPS>> entry:carmap.entrySet()){
			long roadId = entry.getKey();
			List<BusGPS> tmplist = entry.getValue();
			
			double sum = 0;
			for(BusGPS bu:tmplist){
				sum = sum + bu.getSpeed();
			}
			BusCXZS buscxzs = new BusCXZS();
			buscxzs.setDate_yyyymmdd(t_date);
			buscxzs.setDate_hour(t_hour);
			buscxzs.setID((int) roadId);
			buscxzs.setSpeed(sum/tmplist.size());
			buscxzs.setSum(tmplist.size());
			buscxzs.setTablename(tablename);
			lineMessage.addBusCXZS(buscxzs);
		}
		session.commit();
		session.close();
	}
}
