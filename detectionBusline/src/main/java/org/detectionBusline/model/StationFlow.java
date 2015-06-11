package org.detectionBusline.model;

public class StationFlow {
	/**日期*/
	private String t_date;
	/**小时*/
	private int t_hour;
	/**场站名*/
	private String station_name;
	/**班次数*/
	private int sum_shifts;

	public String getT_date() {
		return t_date;
	}

	public void setT_date(String t_date) {
		this.t_date = t_date;
	}

	public int getT_hour() {
		return t_hour;
	}

	public void setT_hour(int t_hour) {
		this.t_hour = t_hour;
	}

	public String getStation_name() {
		return station_name;
	}

	public void setStation_name(String station_name) {
		this.station_name = station_name;
	}

	public int getSum_shifts() {
		return sum_shifts;
	}

	public void setSum_shifts(int sum_shifts) {
		this.sum_shifts = sum_shifts;
	}
	
}
