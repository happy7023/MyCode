package org.detectionBusline.bll;

import java.io.Reader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.detectionBusline.daos.GetStation;
import org.detectionBusline.model.EF;
import org.geotools.geometry.jts.JTSFactoryFinder;

import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;

public class GetElec {

	private static GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory( null );
	private static WKTReader read = new WKTReader( geometryFactory ); 
	
	private static SqlSessionFactory sqlSessionFactory;
    private static Reader reader;

    static{
        try{
            reader    = Resources.getResourceAsReader("Configuration.xml");
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader,"gis_ds1");
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public static Map<String, Polygon> getEleFence() throws ParseException{
    	Map<String, Polygon> mapElec = new HashMap<String, Polygon>();
    	SqlSession session = sqlSessionFactory.openSession();
    	GetStation getstation = session.getMapper(GetStation.class);
    	List<EF> list = getstation.getEF();
    	for(EF ef:list){
    		String key = ef.getV_areaname();
    		String value = ef.getWkt();
    		Polygon polygon = (Polygon) read.read(value);
    		mapElec.put(key, polygon);
    	}
    	session.close();
    	return mapElec;
    }
    
    public static void main(String[] args) throws ParseException {
    	getEleFence();
	}
}
