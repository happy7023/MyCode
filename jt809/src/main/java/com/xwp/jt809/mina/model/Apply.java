package com.xwp.jt809.mina.model;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Apply {

	private ExgMsg exg;
	
	private Date startTime;
	
	private Date endTime;
	
	private DateFormat df = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
	
	public Apply(List<Byte> blist,ExgMsg exg){
		this.exg = exg;
		byte[] startTimeBytes = new byte[8];
		byte[] endTimeBytes = new byte[8];
		for(int i=0;i<8;i++){
			startTimeBytes[i] = blist.get(i);
			endTimeBytes[i] = blist.get(i+8);
		}
		try {
			this.startTime = df.parse(new String(startTimeBytes));
			this.endTime = df.parse(new String(endTimeBytes));
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	public ExgMsg getExg() {
		return exg;
	}

	public void setExg(ExgMsg exg) {
		this.exg = exg;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}	
}
