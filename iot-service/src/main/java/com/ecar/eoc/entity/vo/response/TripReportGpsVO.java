package com.ecar.eoc.entity.vo.response;

import java.io.Serializable;
import java.util.Date;

public class TripReportGpsVO implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private double lon;
	private double lat;
	private Date time;
	private int gpsType;
	private double speed;
	
	public double getLon() {
		return lon;
	}
	public void setLon(double lon) {
		this.lon = lon;
	}
	public double getLat() {
		return lat;
	}
	public void setLat(double lat) {
		this.lat = lat;
	}
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}
	public int getGpsType() {
		return gpsType;
	}
	public void setGpsType(int gpsType) {
		this.gpsType = gpsType;
	}
	public double getSpeed() {
		return speed;
	}
	public void setSpeed(double speed) {
		this.speed = speed;
	}
}
