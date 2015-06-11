/**************************************************************************************** 
 Copyright © 2003-2012 ZTEsoft Corporation. All rights reserved. Reproduction or       <br>
 transmission in whole or in part, in any form or by any means, electronic, mechanical <br>
 or otherwise, is prohibited without the prior written consent of the copyright owner. <br>
 ****************************************************************************************/
package org.detectionBusline.model;

import java.io.Serializable;
import java.text.DecimalFormat;

import org.detectionBusline.bll.Transform;

public class LngLat implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8356483877796082830L;

	/**
	 * 经度
	 */
	private double lng;
	
	/**
	 * 纬度
	 */
	private double lat;

	public LngLat(double lng, double lat) {
		this.lng = lng;
		this.lat = lat;
	}
	
	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLng() {
		return lng;
	}

	public void setLng(double lng) {
		this.lng = lng;
	}

	public XY getXY() {
		return Transform.transToXY(this);
	}


	public String toString() {
		XY xy = getXY();
		return new DecimalFormat("X###").format(xy.getX()) + "-"
				+ new DecimalFormat("Y###").format(xy.getY()) + " "
				+ new DecimalFormat("LAT###.####").format(lat) + "-"
				+ new DecimalFormat("LNG###.####").format(lng);
	}
}
