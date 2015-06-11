package org.detectionBusline.daos;

import java.util.List;

import org.detectionBusline.model.BusCXZS;
import org.detectionBusline.model.BusLineAveSpeed;
import org.detectionBusline.model.LineMessage;

public interface GetLineMessage {

	public List<LineMessage> getLineMessage();
	
	public void addBusLineAveSpeed(BusLineAveSpeed b);
	
	public void addBusLineAveSpeed1Hour(BusCXZS buscxzs);
	
	public void addBusCXZS(BusCXZS buscxzs);
	
}
