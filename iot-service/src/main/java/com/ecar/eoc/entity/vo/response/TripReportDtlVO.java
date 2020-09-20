package com.ecar.eoc.entity.vo.response;

import java.io.Serializable;

public class TripReportDtlVO implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String score;		//驾驶得分
	private String scoreTag;	//评分标签
	private String mileage;		//驾驶里程
	private String startAdress;	//里程开始地点
	private String endAdress;	//里程结束地点
	private String totalTime;	//总时长
	private String startTime;	//行程开始时间
	private String endTime;		//行程结束时间
	private String maxSpeed;	//该段行程的最大速度
	private String avgSpeed;	//行程平均速度
	private int acceTimes;		//急加速次数
	private int deceTimes;		//急减速次数
	private int turnTimes;		//急转弯次数
	private int overSpeedTimes;	//超速次数
	private int tiredDrivingTimes;//疲劳驾驶次数
	private String rankingPercent;//排名超越百分比
	private int rankingTrend;	//排名趋势：0 表示下降 1表示上升
	private String rankingCity;	//排名城市
	private String congestionDura;//拥堵时长
	private int controlScore;		//操控得分
	private int economyScore;		//经济得分
	private int focusScore;			//专注得分
	private int roadScore;			//路况得分
	private int envirScore;			//环境得分
	
	public String getScore() {
		return score;
	}
	public void setScore(String score) {
		this.score = score;
	}
	public String getScoreTag() {
		return scoreTag;
	}
	public void setScoreTag(String scoreTag) {
		this.scoreTag = scoreTag;
	}
	public String getMileage() {
		return mileage;
	}
	public void setMileage(String mileage) {
		this.mileage = mileage;
	}
	public String getStartAdress() {
		return startAdress;
	}
	public void setStartAdress(String startAdress) {
		this.startAdress = startAdress;
	}
	public String getEndAdress() {
		return endAdress;
	}
	public void setEndAdress(String endAdress) {
		this.endAdress = endAdress;
	}
	public String getTotalTime() {
		return totalTime;
	}
	public void setTotalTime(String totalTime) {
		this.totalTime = totalTime;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getMaxSpeed() {
		return maxSpeed;
	}
	public void setMaxSpeed(String maxSpeed) {
		this.maxSpeed = maxSpeed;
	}
	public String getAvgSpeed() {
		return avgSpeed;
	}
	public void setAvgSpeed(String avgSpeed) {
		this.avgSpeed = avgSpeed;
	}
	public int getAcceTimes() {
		return acceTimes;
	}
	public void setAcceTimes(int acceTimes) {
		this.acceTimes = acceTimes;
	}
	public int getDeceTimes() {
		return deceTimes;
	}
	public void setDeceTimes(int deceTimes) {
		this.deceTimes = deceTimes;
	}
	public int getTurnTimes() {
		return turnTimes;
	}
	public void setTurnTimes(int turnTimes) {
		this.turnTimes = turnTimes;
	}
	public int getOverSpeedTimes() {
		return overSpeedTimes;
	}
	public void setOverSpeedTimes(int overSpeedTimes) {
		this.overSpeedTimes = overSpeedTimes;
	}
	public int getTiredDrivingTimes() {
		return tiredDrivingTimes;
	}
	public void setTiredDrivingTimes(int tiredDrivingTimes) {
		this.tiredDrivingTimes = tiredDrivingTimes;
	}
	public String getRankingPercent() {
		return rankingPercent;
	}
	public void setRankingPercent(String rankingPercent) {
		this.rankingPercent = rankingPercent;
	}
	public int getRankingTrend() {
		return rankingTrend;
	}
	public void setRankingTrend(int rankingTrend) {
		this.rankingTrend = rankingTrend;
	}
	public String getRankingCity() {
		return rankingCity;
	}
	public void setRankingCity(String rankingCity) {
		this.rankingCity = rankingCity;
	}
	public String getCongestionDura() {
		return congestionDura;
	}
	public void setCongestionDura(String congestionDura) {
		this.congestionDura = congestionDura;
	}
	public int getControlScore() {
		return controlScore;
	}
	public void setControlScore(int controlScore) {
		this.controlScore = controlScore;
	}
	public int getEconomyScore() {
		return economyScore;
	}
	public void setEconomyScore(int economyScore) {
		this.economyScore = economyScore;
	}
	public int getFocusScore() {
		return focusScore;
	}
	public void setFocusScore(int focusScore) {
		this.focusScore = focusScore;
	}
	public int getRoadScore() {
		return roadScore;
	}
	public void setRoadScore(int roadScore) {
		this.roadScore = roadScore;
	}
	public int getEnvirScore() {
		return envirScore;
	}
	public void setEnvirScore(int envirScore) {
		this.envirScore = envirScore;
	}

}
