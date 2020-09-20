/**
 * Project Name:eoc-sim
 * File Name:Asset.java
 * Package Name:com.ecar.eoc.entity.wayz
 * Date:2018年6月19日下午4:20:41
 * Copyright (c) 2018, E-CAR All Rights Reserved.
 *
*/

package com.ecar.eoc.entity.wayz;

import java.io.Serializable;

/**
 * ClassName:Asset <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Date:     2018年6月19日 下午4:20:41 <br/>
 * @author   zhongying	 
 */
public class Asset implements Serializable {

	/**
	 * serialVersionUID:TODO(用一句话描述这个变量表示什么).
	 */
	private static final long serialVersionUID = -3856364536300959098L;
	
	String asset = "1234567890123456";
	
	Location location = new Location();

	public String getAsset() {
		return asset;
	}

	public void setAsset(String asset) {
		this.asset = asset;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

}

