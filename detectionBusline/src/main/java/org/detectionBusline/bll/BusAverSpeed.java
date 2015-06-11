package org.detectionBusline.bll;

import java.io.Reader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimerTask;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.detectionBusline.daos.BusGPSDAO;
import org.detectionBusline.model.BusGPS;

public class BusAverSpeed extends TimerTask{
	
	/**GPS数据集合*/
	private List<BusGPS> buslist;
	private DateFormat df = new SimpleDateFormat("yyyyMMdd");
	private DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");
	private DateFormat df2 = new SimpleDateFormat("HH");
	private Date date;
	private LineCXZS linecxzs;
	private RoadCXZS roadcxzs;
	private SevenStationFlow sevenStationFlow;
	private ElectronicFence elecFence;
	private SqlSessionFactory sqlSessionFactory;
    private Reader reader;

    
    public BusAverSpeed(){
    	 try{
             reader    = Resources.getResourceAsReader("Configuration.xml");
             sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
         }catch(Exception e){
             e.printStackTrace();
         }
    	 linecxzs = new LineCXZS(sqlSessionFactory);
    	 roadcxzs = new RoadCXZS(sqlSessionFactory);
    	 sevenStationFlow = new SevenStationFlow(sqlSessionFactory);
    	 elecFence = new ElectronicFence(sqlSessionFactory);
    }
	
	public void run() {
		System.out.println(new Date());
		SqlSession session = sqlSessionFactory.openSession();
		BusGPSDAO getgps = session.getMapper(BusGPSDAO.class);
		date = new Date();
		String tablename = df.format(date);
		tablename = "T_BUSGPSLOG_"+tablename;
		buslist = getgps.getBusGPS(tablename);
		session.close();	
		//切换到实际计算的小时
		int t_hour = Integer.parseInt(df2.format(date))-1;
		if(t_hour < 0){
			 Calendar rightNow = Calendar.getInstance();
			 rightNow.setTime(date);
			 rightNow.add(Calendar.HOUR_OF_DAY, -1);
			 date=rightNow.getTime();
			 t_hour = 23;
		}
		String t_date = df1.format(date);
		try {
			linecxzs.getLineCXZS(buslist,t_date,t_hour);
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		roadcxzs.getRoadCXZS(buslist,t_date,t_hour);
		sevenStationFlow.getSevenStationFlow( buslist,t_date,t_hour);
		elecFence.getElectronicFenceSpeed(buslist, t_date, t_hour);
		buslist.clear();
	}
}
