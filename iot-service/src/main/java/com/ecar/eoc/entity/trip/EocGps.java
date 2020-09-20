package com.ecar.eoc.entity.trip;

import java.io.Serializable;
import java.util.Date;

public class EocGps implements Serializable,Comparable<EocGps>{
	private static final long serialVersionUID = 1L;
	private Integer terminalId; // 终端ID
	private Date time; // 采集时间
	private Double lon; // 经度
	private Double lat; // 纬度
	private Double height; // 海拔（单位：米）
	private Float speed; // 速度（单位：千米/小时）
	private Double direct; // 方向
	private Integer satelliteNum; // 卫星个数
	private Double acceleration; // 加速度
	private Double distance; // 距离上一个点的距离
	private Integer acc; // 点火熄火状态0 熄火，1 点火 
	private Integer online;	//【取值为10，表示该gps点是外部系统同步过来的最新位置点】
	private Integer sysTime;
	private String cmdId;	//GPS的协议类型
	private Integer move;//设备运动状态：0 未知,1 不动,2 动


	public Integer getTerminalId() {
		return terminalId;
	}

	public void setTerminalId(Integer terminalId) {
		this.terminalId = terminalId;
	}

	public Integer getAcc() {
		return acc;
	}

	public void setAcc(Integer acc) {
		this.acc = acc;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public Double getLon() {
		return lon;
	}

	public void setLon(Double lon) {
		this.lon = lon;
	}

	public Double getLat() {
		return lat;
	}

	public void setLat(Double lat) {
		this.lat = lat;
	}

	public Double getHeight() {
		return height;
	}

	public void setHeight(Double height) {
		this.height = height;
	}

	public Float getSpeed() {
		return speed;
	}

	public void setSpeed(Float speed) {
		this.speed = speed;
	}

	public Double getDirect() {
		return direct;
	}

	public void setDirect(Double direct) {
		this.direct = direct;
	}

	public Integer getSatelliteNum() {
		return satelliteNum;
	}

	public void setSatelliteNum(Integer satelliteNum) {
		this.satelliteNum = satelliteNum;
	}

	public Double getAcceleration() {
		return acceleration;
	}

	public void setAcceleration(Double acceleration) {
		this.acceleration = acceleration;
	}

	public Double getDistance() {
		return distance;
	}

	public void setDistance(Double distance) {
		this.distance = distance;
	}

	public Integer getSysTime() {
		return sysTime;
	}

	public void setSysTime(Integer sysTime) {
		this.sysTime = sysTime;
	}

	public Integer getOnline() {
		return online;
	}

	public void setOnline(Integer online) {
		this.online = online;
	}

	public String getCmdId() {
		return cmdId;
	}

	public void setCmdId(String cmdId) {
		this.cmdId = cmdId;
	}

	@Override
	public String toString() {
		return "EocGps [terminalId=" + terminalId + ", time=" + time + ", lon="
				+ lon + ", lat=" + lat + ", height=" + height + ", speed="
				+ speed + ", direct=" + direct + ", satelliteNum="
				+ satelliteNum + ", acceleration=" + acceleration
				+ ", distance=" + distance + ", acc=" + acc + ", online="
				+ online + ", sysTime=" + sysTime + ", cmdId=" + cmdId + "]";
	}

	public Integer getMove() {
		return move;
	}

	public void setMove(Integer move) {
		this.move = move;
	}

	@Override
	public int compareTo(EocGps o) {
		return this.getTime().compareTo(o.getTime());
	}
}
