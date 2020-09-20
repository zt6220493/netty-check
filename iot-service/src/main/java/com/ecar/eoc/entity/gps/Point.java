package com.ecar.eoc.entity.gps;

/**
 * GPS点的经纬度
 * @author pom
 *
 */
public class Point {
	private double lon;// 经度
	private double lat;// 纬度
     
    public Point() {
    }
     
    public Point(double lon, double lat) {
        this.lon = lon;
        this.lat = lat;
    }
 
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Point) {
            Point bmapPoint = (Point) obj;
            return (bmapPoint.getLon() == lon && bmapPoint.getLat() == lat) ? true : false;
        } else {
            return false;
        }
    }
  
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

	@Override
    public String toString() {
        return "Point [lat=" + lat + ", lon=" + lon + "]";
    }
}
