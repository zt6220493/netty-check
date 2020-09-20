package com.ecar.eoc.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class GpsFilter {

	private List<GpsInfo> list;

	public GpsFilter() {
		list = new ArrayList<GpsInfo>();
	}
	//[{"provider":"gps","time":1560267788000,"elapsedRealtimeNanos":0,"altitude":0.0,"latitude":22.546051,"longitude":114.02597,"accuracy":1.0,"satelliteCount":0,"speed":0.0,"bearing":0.0}]
	public GpsFilter(String arrGps) {
		list = new ArrayList<GpsInfo>();
		JSONArray array = JSON.parseArray(arrGps);
		for(int i=0;i<array.size();i++){
			JSONObject json = array.getJSONObject(i);
			GpsInfo gps = new GpsInfo(json.getDoubleValue("longitude"),json.getDoubleValue("latitude"),
					json.getDoubleValue("speed"),json.getLongValue("time"));
			
			list.add(gps);
		}
		
	}
	
	
	private static final int MAX_DISTANCE = 1000;// 单位为米

	public GpsInfo filter() {

		if (list == null || list.size() < 1) {
			return null;
		}
		Collections.sort(list, new Comparator<GpsInfo>()
		{
			@Override
			public int compare(GpsInfo o1, GpsInfo o2) 
			{
				if(o2.getGpsTime()>o1.getGpsTime())
				{
					return 1;
				}
				return -1;
			}
		});
		if (list.size() < 3) {
			return getDataNormalGps(list);
		}
		GpsInfo returnGps = null;
		// 获取每一个点与其他点的距离
		for (int i = 0; i < list.size(); i++) {
			setDistance(i, list);
		}
		returnGps = list.get(0);

		// 寻找
		for (int i = 1; i < list.size(); i++) {
			GpsInfo tmpGps = list.get(i);
			if (tmpGps.getDistance() < returnGps.getDistance()) {
				returnGps = tmpGps;
			}
		}
		// System.out.println("找:" + testGps);
		if (returnGps.getIndex() != 0) {

			GpsInfo fristGps = list.get(0);
			if (getDistance(fristGps.getLat(), fristGps.getLon(), returnGps
					.getLat(), returnGps.getLon()) < MAX_DISTANCE) {
				returnGps = fristGps;
			}
		}
		return returnGps;

	}

	private void setDistance(int index, List<GpsInfo> list) {
		GpsInfo gps = list.get(index);
		gps.setIndex(index);
		for (int i = 0; i < list.size(); i++) {
			if (index != i) {
				GpsInfo nextGps = list.get(i);
				double pointDistance = getDistance(gps.getLat(), gps.getLon(),
						nextGps.getLat(), nextGps.getLon());
				Double distance = gps.getDistance();
				if (null == distance) 
				{
					distance = 0.00;
				}
				gps.setDistance(distance + pointDistance);
			}
		}
		// System.out.println("desc:" + gps.getDesc() + ",distance="
		// + gps.getDistance());
	}

	public static void main(String[] args) {
		System.out.println("结果:>>"
				+ new GpsFilter().addGpsInfo(109.794823, 27.86509, 18.0,
						System.currentTimeMillis(), null, "第一个点").

				addGpsInfo(109.794801, 27.865071, 18.0,
						System.currentTimeMillis(), null, "第二个点").

				addGpsInfo(109.92036, 27.982698, 18.0,
						System.currentTimeMillis(), null, "第三个点").

				addGpsInfo(109.921946, 27.984788, 18.0,
						System.currentTimeMillis(), null, "第四个点").

				addGpsInfo(109.794701, 27.864716, 18.0,
						System.currentTimeMillis(), null, "第五个点").filter());
	}

	public double getDistance(double lat1, double lng1, double lat2, double lng2) {
		if (lng1 == lng2 && lat1 == lat2) {
			return 0.0;
		}

		double radLat1 = rad(lat1);
		double radLat2 = rad(lat2);
		double a = radLat1 - radLat2;
		double b = rad(lng1) - rad(lng2);
		double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
				+ Math.cos(radLat1) * Math.cos(radLat2)
				* Math.pow(Math.sin(b / 2), 2)));
		s = s * 6378.137;
		s = Math.round(s * 10000d) / 10000d;
		s = s * 1000;
		return s;
	}

	private double rad(double d) {
		return d * Math.PI / 180.0;
	}

	/**
	 * 少于3个点,无法进行比较，则按时间取数据看起来比较正常的GPS点 正常的GPS点的定义:速度少于180KM/H
	 * 
	 * @param list
	 * @return
	 */
	private GpsInfo getDataNormalGps(List<GpsInfo> list) {

		for (GpsInfo gps : list) {
			if (ifNormalGps(gps)) {
				return gps;
			}
		}
		// 都没有,取最近的
		return list.get(0);
	}

	private boolean ifNormalGps(GpsInfo gps) {

		if (gps.getSpeed() < 180) {
			return true;
		}

		return false;
	}

	public class GpsInfo {

		private long gpsTime;
		private double lon;
		private double lat;
		private double speed;
		private Long sysTime;
		private Double distance;

		private int index;
		private String desc;

		public GpsInfo(){}
		public GpsInfo(double lon,double lat,double speed,long gpsTime){
			this.lon=lon;
			this.lat=lat;
			this.speed=speed;
			this.gpsTime=gpsTime;
		}
		
		public long getGpsTime() {
			return gpsTime;
		}

		public void setGpsTime(long gpsTime) {
			this.gpsTime = gpsTime;
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

		public double getSpeed() {
			return speed;
		}

		public void setSpeed(double speed) {
			this.speed = speed;
		}

		public Long getSysTime() {
			return sysTime;
		}

		public void setSysTime(Long sysTime) {
			this.sysTime = sysTime;
		}

		public Double getDistance() {
			return distance;
		}

		public void setDistance(Double distance) {
			this.distance = distance;
		}

		public int getIndex() {
			return index;
		}

		public void setIndex(int index) {
			this.index = index;
		}

		public String getDesc() {
			return desc;
		}

		public void setDesc(String desc) {
			this.desc = desc;
		}

		@Override
		public String toString() {
			return "GpsInfo [desc=" + desc + ", distance=" + distance
					+ ", gpsTime=" + gpsTime + ", index=" + index + ", lat="
					+ lat + ", lon=" + lon + ", speed=" + speed + ", sysTime="
					+ sysTime + "]";
		}

	}

	public GpsFilter addGpsInfo(double lon, double lat, double speed,
			long gpsTime, Long sysTime) {
		return addGpsInfo(lon, lat, speed, gpsTime, sysTime, null);
	}

	public GpsFilter addGpsInfo(double lon, double lat, double speed,
			long gpsTime, Long sysTime, String desc) {
		GpsFilter.GpsInfo info = new GpsFilter.GpsInfo();
		info.setLon(lon);
		info.setLat(lat);
		info.setSpeed(speed);
		info.setGpsTime(gpsTime);
		info.setSysTime(sysTime);
		info.setDesc(desc);
		this.list.add(info);
		return this;
	}

}
