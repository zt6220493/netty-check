/**
 * Project Name:eoc-sim
 * File Name:Location.java
 * Package Name:com.ecar.eoc.entity.wayz
 * Date:2018年6月19日下午3:44:24
 * Copyright (c) 2018, E-CAR All Rights Reserved.
 *
*/

package com.ecar.eoc.entity.wayz;

import java.io.Serializable;

/**
 * ClassName:Location <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Date:     2018年6月19日 下午3:44:24 <br/>
 * @author   zhongying	 
 */
public class Location implements Serializable {
	
	/**
	 * serialVersionUID:TODO(用一句话描述这个变量表示什么).
	 */
	private static final long serialVersionUID = 1L;
	long timestamp = System.currentTimeMillis();
	Position position = new Position();
	public long getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
	public Position getPosition() {
		return position;
	}
	public void setPosition(Position position) {
		this.position = position;
	}
	
}

