package org.detectionBusline.bll;

import org.detectionBusline.model.LngLat;
import org.detectionBusline.model.XY;

public class CalculateDistance {

	public static int getDistance(LngLat xy1,LngLat xy2){
		
		XY xyPlane1= Transform.transToXY(xy1);
		XY xyPlane2= Transform.transToXY(xy2);
		
		int distance = (int) Math.sqrt(Math.pow(xyPlane1.getX()-xyPlane2.getX(),2)+
				Math.pow(xyPlane1.getX()-xyPlane2.getX(),2));
		return distance;
	}
	
	public static int getDistance2(LngLat xy1,LngLat xy2){
		
		double dist_angle = Math.sin(xy1.getLat()*Math.PI/180)*Math.sin(xy2.getLat()*Math.PI/180)+
							Math.cos(xy1.getLat()*Math.PI/180)*Math.cos(xy2.getLat()*Math.PI/180)
							*Math.cos((xy1.getLng()-xy2.getLng())*Math.PI/180);
		int distance = (int) (6371000*Math.acos(dist_angle));
		return distance;
	}
}
