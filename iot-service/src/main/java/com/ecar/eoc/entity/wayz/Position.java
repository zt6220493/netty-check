/**
 * Project Name:eoc-sim
 * File Name:Position.java
 * Package Name:com.ecar.eoc.entity.wayz
 * Date:2018年6月19日下午3:45:41
 * Copyright (c) 2018, E-CAR All Rights Reserved.
 *
*/

package com.ecar.eoc.entity.wayz;

import java.io.Serializable;

/**
 * ClassName:Position <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Date:     2018年6月19日 下午3:45:41 <br/>
 * @author   zhongying	 
 */
public class Position implements Serializable {

	/**
	 * serialVersionUID:TODO(用一句话描述这个变量表示什么).
	 */
	private static final long serialVersionUID = 8716887612459147933L;
	
	
	long timestamp = System.currentTimeMillis();
	LngLat point = new LngLat();
	String source = "bdmap";
	float accuracy = 2.3f;
	float velocity = 64.5f;
	int heading = 45;
	public long getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
	public LngLat getPoint() {
		return point;
	}
	public void setPoint(LngLat point) {
		this.point = point;
	}
	public float getAccuracy() {
		return accuracy;
	}
	public void setAccuracy(float accuracy) {
		this.accuracy = accuracy;
	}
	public float getVelocity() {
		return velocity;
	}
	public void setVelocity(float velocity) {
		this.velocity = velocity;
	}
	public int getHeading() {
		return heading;
	}
	public void setHeading(int heading) {
		this.heading = heading;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}

}

