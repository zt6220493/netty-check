/**
 * Project Name:eoc-sim
 * File Name:LngLat.java
 * Package Name:com.ecar.eoc.entity.wayz
 * Date:2018年6月19日下午3:47:48
 * Copyright (c) 2018, E-CAR All Rights Reserved.
 *
*/

package com.ecar.eoc.entity.wayz;

import java.io.Serializable;

/**
 * ClassName:LngLat <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Date:     2018年6月19日 下午3:47:48 <br/>
 * @author   zhongying	 
 */
public class LngLat implements Serializable {

	/**
	 * serialVersionUID:TODO(用一句话描述这个变量表示什么).
	 */
	private static final long serialVersionUID = -4587895396304758323L;
	
	float longitude;
	float latitude;
	public float getLongitude() {
		return longitude;
	}
	public void setLongitude(float longitude) {
		this.longitude = longitude;
	}
	public float getLatitude() {
		return latitude;
	}
	public void setLatitude(float latitude) {
		this.latitude = latitude;
	}

}

