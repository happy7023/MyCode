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
import org.detectionBusline.model.ElectronicFenceSpeedCXZS;
import org.geotools.geometry.jts.JTSFactoryFinder;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.io.ParseException;

public class ElectronicFence {

	private GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory( null );
	private Map<String, Polygon> mapElec = new HashMap<String, Polygon>();
	private Map<String, List<Float>> mapList = new HashMap<String, List<Float>>();
	private SqlSessionFactory sqlSessionFactory;
	
	public ElectronicFence(SqlSessionFactory sqlSessionFactory){
		try {
			mapElec = GetElec.getEleFence();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		this.sqlSessionFactory = sqlSessionFactory;
	}

	
	public void getElectronicFenceSpeed(List<BusGPS> buslist,String t_date,int t_hour){
		SqlSession session = sqlSessionFactory.openSession();
		GetStation get = session.getMapper(GetStation.class);
		for(BusGPS bus :buslist){
			Point p = createPoint(bus.getLongitude(),bus.getLatitude());
			for(Entry<String, Polygon> entry:mapElec.entrySet()){
				String key = entry.getKey();
				Polygon polygon = entry.getValue();
				if(polygon.contains(p)){
					if(!mapList.containsKey(key)){
						mapList.put(key, new ArrayList<Float>());
					}
					mapList.get(key).add(bus.getSpeed());
				}
			}
		}
		for(Entry<String, List<Float>> entry:mapList.entrySet()){
			String key = entry.getKey();
			List<Float> list = entry.getValue();
			double v =0;
			for(Float f : list){
				v = v +f;
			}
			v = v/list.size();
			ElectronicFenceSpeedCXZS elec = new ElectronicFenceSpeedCXZS();
			elec.setT_date(t_date);
			elec.setT_hour(t_hour);
			elec.setArea_name(key);
			elec.setSpeed(v);
			get.addElecFencce(elec);
			System.out.println(key+" 平均公交速度: "+v);
		}
		session.commit();
		session.close();
		mapList.clear();
	}
	
	public Point createPoint(double lng,double lat){
		Coordinate coord = new Coordinate(lng, lat);  
        Point point = geometryFactory.createPoint( coord );  
        return point;
	}
}
