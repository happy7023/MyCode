package org.detectionBusline.bll;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.detectionBusline.model.LngLat;

import com.supconit.honeycomb.gis.api.entities.RoadPath;
import com.supconit.honeycomb.gis.api.services.RoadService;
import com.supconit.honeycomb.gis.api.services.arcgis.RoadServiceImpl;
import com.supconit.lwlk.core.DatasourceNodeUtils;

public class LocateGPS {

	private static Map<Integer, DataSource> gisDatasources;
	private static RoadService	roadService;
	private static ConfigParser configParser = new ConfigParser();
	
	static {
		gisDatasources = buildGisDatasources(configParser.getGisDatasources());
		if (null == gisDatasources || gisDatasources.isEmpty()){
			throw new NullPointerException("no gis ds setted.");
		}
		roadService = new RoadServiceImpl(gisDatasources.entrySet().iterator().next().getValue(),"roadpaths");

	}
	
	public static long locate(LngLat lnglat, double direction){
		RoadPath roadPath = roadService.getRoadPath(lnglat.getLng(), lnglat.getLat(), direction);
		if (null != roadPath) {
			long road = roadPath.getId();
			return road;
		}
		return 0;
	}
	
	private static Map<Integer, DataSource> buildGisDatasources(List<Map<String, String>> gisDatasources) {
		if (null == gisDatasources || gisDatasources.isEmpty())
			return null;
		Map<Integer, DataSource> result = new HashMap<Integer, DataSource>();
		int count = 0;
		for (Map<String, String> map : gisDatasources) {
			DataSource ds = DatasourceNodeUtils.buildDatasourceNode(map);
			result.put(count++, ds);
		}
		return result;
	}
}
