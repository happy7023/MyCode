/**************************************************************************************** 
 Copyright © 2003-2012 ZTEsoft Corporation. All rights reserved. Reproduction or       <br>
 transmission in whole or in part, in any form or by any means, electronic, mechanical <br>
 or otherwise, is prohibited without the prior written consent of the copyright owner. <br>
 ****************************************************************************************/
package org.detectionBusline.model;

import java.io.Serializable;


public class XY implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7860644197941837567L;

	/**
	 * x坐标
	 */
    private double x;
    
    /**
     * y坐标
     */
    private double y;
    
    public XY(double x, double y) {
    	this.x = x;
    	this.y = y;
    }

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}
}
