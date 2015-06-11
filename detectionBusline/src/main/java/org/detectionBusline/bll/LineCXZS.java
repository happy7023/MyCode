package org.detectionBusline.bll;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.detectionBusline.daos.GetLineMessage;
import org.detectionBusline.model.BusCXZS;
import org.detectionBusline.model.BusGPS;
import org.detectionBusline.model.BusLineAveSpeed;
import org.detectionBusline.model.LineMessage;
import org.detectionBusline.model.LngLat;

public class LineCXZS {

	/**线路ID对应线路起止站点，属于基本信息，预先加载*/
	private final Map<Integer,LineMessage> carmap;
	/**车牌号对应车辆起止站点信息*/
	private Map<Integer,LineMessage> nummap=new HashMap<Integer,LineMessage>();
	
	private SqlSessionFactory sqlSessionFactory;
	
	public LineCXZS(SqlSessionFactory sqlSessionFactory){
		this.sqlSessionFactory = sqlSessionFactory;
		carmap = GetLineMessagers.getLine();
	}
	
	public void getLineCXZS(List<BusGPS> buslist,String t_date,int t_hour) throws CloneNotSupportedException{
		
		BusCXZS buscxzs;
		double sum_length = 0;
		long sum_time = 0;
		double v =0;
		//取最近一小时数据，按照车牌分组。排序。
		SqlSession session = sqlSessionFactory.openSession();
		GetLineMessage lineMessage = session.getMapper(GetLineMessage.class);
		
		//计算这一个小时内车辆状态是否发生变化。
		for(BusGPS bu:buslist){
			int numb = bu.getNumb();
			int lineID = bu.getLineID();
			double distance ;//计算两点间距离
			LngLat buLngLat =  new LngLat(bu.getLongitude(),bu.getLatitude());
			//算起点
			if(nummap.containsKey(numb)){
				LineMessage lineMe = nummap.get(numb);
				if(lineMe.isOnline() == false){
					if(nummap.get(numb).getMarkUpDown() == 1){
						distance = CalculateDistance.getDistance2(buLngLat, lineMe.getUpStartStation());	//坐标转换之后计算
						if(distance < 200){
							nummap.get(numb).setStart(bu.getTime().getTime());
							nummap.get(numb).setMarkUpDown(1);
							nummap.get(numb).setOnline(false);
							continue;
						}else{
							nummap.get(numb).setOnline(true);
						}
					}else{
						distance = CalculateDistance.getDistance2(buLngLat, lineMe.getDownStartStation());
						if(distance < 200){
							nummap.get(numb).setStart(bu.getTime().getTime());
							nummap.get(numb).setMarkUpDown(2);
							nummap.get(numb).setOnline(false);
							continue;
						}else{
							nummap.get(numb).setOnline(true);
						}
					}
				}
			}else{
				LineMessage lineMe;
				if(carmap.containsKey(lineID)){		
					lineMe = (LineMessage) carmap.get(lineID).clone();
				}else{
					System.out.println("线路:"+lineID+" 信息不完整，不能计算。");
					continue;
				} 
				distance = CalculateDistance.getDistance2(buLngLat, lineMe.getUpStartStation());	//坐标转换之后计算
				if(distance < 200){
					lineMe.setStart(bu.getTime().getTime());
					lineMe.setMarkUpDown(1);
					lineMe.setOnline(false);
					nummap.put(numb, lineMe);
					continue;
				}
				distance = CalculateDistance.getDistance2(buLngLat, lineMe.getDownStartStation());
				if(distance < 200){
					lineMe.setStart(bu.getTime().getTime());
					lineMe.setMarkUpDown(2);
					lineMe.setOnline(false);
					nummap.put(numb, lineMe);
					continue;
				}
			}	
			//算终点
			if(nummap.containsKey(numb) ){
				LineMessage lineMe = nummap.get(numb);
				if(lineMe.isOnline() == true){
					//判定是否停车
					if(lineMe.getMarkUpDown() ==1){
						distance = CalculateDistance.getDistance2(buLngLat, lineMe.getUpEndStation());
					}else{
						distance = CalculateDistance.getDistance2(buLngLat, lineMe.getDownEndStation());
					}
					if(distance < 200 & (bu.getTime().getTime() - lineMe.getStart()) > 10*60*1000L){
						lineMe.setEnd(bu.getTime().getTime());
						v = lineMe.getEnd()-lineMe.getStart();
						v=v/1000;
						v = v/60;
						v = v/60;
						v = (lineMe.getLength()-0.4)/v;
						//存储结果
						if(v>40 || v<10){
							v = 0;
						}else{
							sum_length = sum_length + lineMe.getLength()-0.4;
							sum_time = (long) (sum_time + (lineMe.getEnd()-lineMe.getStart()));
							
							BusLineAveSpeed b = new BusLineAveSpeed();
							b.setT_date(t_date);
							b.setT_hour(t_hour);
							b.setLineID(lineID);
							b.setNumb(numb);
							b.setSpeed(v);
							b.setStarttime(new Date(lineMe.getStart()));
							b.setEndtime(new Date(lineMe.getEnd()));
							b.setUpORdown(lineMe.getMarkUpDown());
							b.setCxzs((Math.min(v/30, 1)*10));
							lineMessage.addBusLineAveSpeed(b);
							System.out.println("车牌:"+bu.getNumb() +"始发时间:"+new Date(lineMe.getStart())+"到达终点:"+new Date(lineMe.getEnd())+" 速度"+v);
							v = 0;
						}
						nummap.remove(numb);
					}	
				}
			}
		}
		if(sum_time != 0 && sum_length != 0){
			buscxzs = new BusCXZS();
			v = sum_time/1000;
			v = v/3600;
			v = sum_length/v;
			
			buscxzs.setCxzs((Math.min(v/30, 1)*10));
			buscxzs.setDate_yyyymmdd(t_date);
			buscxzs.setDate_hour(t_hour);
			buscxzs.setSpeed(v);
			lineMessage.addBusLineAveSpeed1Hour(buscxzs);
			session.commit();
			session.close();
		}
	}
	
}
