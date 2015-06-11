package org.detectionBusline.bll;

import java.util.Date;
import java.util.Timer;

public class TestMain {
	
	@SuppressWarnings("deprecation")
	public static void main(String[] args) throws InterruptedException {
				
		Date date = new Date();
		
		int hour = date.getHours();

		while(new Date().getHours() == hour){
			Thread.sleep(1000);
			System.out.println("Time not come ");
		}
		
		Timer t = new Timer();
		t.scheduleAtFixedRate(new BusAverSpeed(), 10, 60*60*1000);
		
	}
}