package org.detectionBusline.bll;

import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.detectionBusline.daos.GetLineMessage;
import org.detectionBusline.daos.GetStation;
import org.detectionBusline.model.LineMessage;
import org.detectionBusline.model.LngLat;
import org.detectionBusline.model.Station;

public class GetLineMessagers {

	private static SqlSessionFactory sqlSessionFactory;
    private static Reader reader;

    static{
        try{
            reader    = Resources.getResourceAsReader("Configuration.xml");
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public static Map<Integer,LineMessage> getLine(){
    	//所有起始站集合，路线为key
    	Map<Integer ,LineMessage> mapLine = new HashMap<Integer ,LineMessage>();
    	Map<Integer ,LngLat> mapStation = new HashMap<Integer ,LngLat>();
    	List<LineMessage> linelist = new ArrayList<LineMessage>();
    	List<Station> stationlist = new ArrayList<Station>();
		SqlSession session = sqlSessionFactory.openSession();
		try {
        	GetLineMessage getLine = session.getMapper(GetLineMessage.class);
        	linelist = getLine.getLineMessage();
        	GetStation getstation = session.getMapper(GetStation.class);
        	stationlist = getstation.getStation();
        } finally {
        	session.close();
        }
		for(Station s:stationlist){
			mapStation.put(s.getId(), new LngLat(s.getLongi(),s.getLati()));
		}
		for(LineMessage line:linelist){
			if(mapStation.containsKey(line.getUpstartstationid()) & 
					mapStation.containsKey(line.getUpendstationid())&
					mapStation.containsKey(line.getDownstartstationid())&
					mapStation.containsKey(line.getDownendstationid())){
				line.setUpStartStation(mapStation.get(line.getUpstartstationid()));
				line.setUpEndStation(mapStation.get(line.getUpendstationid()));
				line.setDownStartStation(mapStation.get(line.getDownstartstationid()));
				line.setDownEndStation(mapStation.get(line.getDownendstationid()));
				mapLine.put(line.getID(), line);
			}
		}
		return mapLine;
    }
}
