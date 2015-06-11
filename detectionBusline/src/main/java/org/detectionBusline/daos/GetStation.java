package org.detectionBusline.daos;

import java.util.List;

import org.detectionBusline.model.EF;
import org.detectionBusline.model.ElectronicFenceSpeedCXZS;
import org.detectionBusline.model.Station;
import org.detectionBusline.model.StationFlow;


public interface GetStation {

	public List<Station> getStation();
	
	public void addSevenStationFlow(StationFlow flow);
	
	public List<EF> getEF();
	
	public void addElecFencce(ElectronicFenceSpeedCXZS elec);
}
