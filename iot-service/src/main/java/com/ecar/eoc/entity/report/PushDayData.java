package com.ecar.eoc.entity.report;

public class PushDayData {
	private String score;		//得分
	private String date;		//日期字符串(精确到日 yyyy-MM-dd)
	private Integer ts;			//日期时间出
	private String mileage;		//日总里程(公里)
	private String duration;	//日驾驶总时长(分钟)
	private String durationFMT; //日驾驶总时长(如23.5分钟，格式化后：00:23:30)
	private String avgSpeed;	//平均时速(km/h)  行驶里程(mileage)/行驶时长(duration/60)
	private String maxSpeed;
	private String acce;		//急加速次数 
	private String brake;		//急减速次数
	private String turn;		//急转弯次数	
	private String speeding;	//超速行驶次数
	private Integer terminalId; //设备id
	private String grade;		//级别
	private String dayEarnings;	//今日收益
	private String totalEarnings;//总收益
	private String bhv; // 驾驶行为分
	private String du;  // 驾驶时长分
	private String rou; // 驾驶路线分
	private String pro; // 驾驶习惯分
	private String mil; // 驾驶里程分
	private Integer ctrip;
	private String startLat;
	private String startLon;
	private String endLat;
	private String endLon;
	
	public String getScore() {
		return score;
	}
	public void setScore(String score) {
		this.score = score;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public Integer getTs() {
		return ts;
	}
	public void setTs(Integer ts) {
		this.ts = ts;
	}
	public String getMileage() {
		return mileage;
	}
	public void setMileage(String mileage) {
		this.mileage = mileage;
	}
	public String getDuration() {
		return duration;
	}
	public void setDuration(String duration) {
		this.duration = duration;
	}
	public String getDurationFMT() {
		return durationFMT;
	}
	public void setDurationFMT(String durationFMT) {
		this.durationFMT = durationFMT;
	}
	public String getAvgSpeed() {
		return avgSpeed;
	}
	public void setAvgSpeed(String avgSpeed) {
		this.avgSpeed = avgSpeed;
	}
	public String getMaxSpeed() {
		return maxSpeed;
	}
	public void setMaxSpeed(String maxSpeed) {
		this.maxSpeed = maxSpeed;
	}
	public String getAcce() {
		return acce;
	}
	public void setAcce(String acce) {
		this.acce = acce;
	}
	public String getBrake() {
		return brake;
	}
	public void setBrake(String brake) {
		this.brake = brake;
	}
	public String getTurn() {
		return turn;
	}
	public void setTurn(String turn) {
		this.turn = turn;
	}
	public String getSpeeding() {
		return speeding;
	}
	public void setSpeeding(String speeding) {
		this.speeding = speeding;
	}
	public Integer getTerminalId() {
		return terminalId;
	}
	public void setTerminalId(Integer terminalId) {
		this.terminalId = terminalId;
	}
	public String getGrade() {
		return grade;
	}
	public void setGrade(String grade) {
		this.grade = grade;
	}
	public String getDayEarnings() {
		return dayEarnings;
	}
	public void setDayEarnings(String dayEarnings) {
		this.dayEarnings = dayEarnings;
	}
	public String getTotalEarnings() {
		return totalEarnings;
	}
	public void setTotalEarnings(String totalEarnings) {
		this.totalEarnings = totalEarnings;
	}
	public String getBhv() {
		return bhv;
	}
	public void setBhv(String bhv) {
		this.bhv = bhv;
	}
	public String getDu() {
		return du;
	}
	public void setDu(String du) {
		this.du = du;
	}
	public String getRou() {
		return rou;
	}
	public void setRou(String rou) {
		this.rou = rou;
	}
	public String getPro() {
		return pro;
	}
	public void setPro(String pro) {
		this.pro = pro;
	}
	public String getMil() {
		return mil;
	}
	public void setMil(String mil) {
		this.mil = mil;
	}
	public Integer getCtrip() {
		return ctrip;
	}
	public void setCtrip(Integer ctrip) {
		this.ctrip = ctrip;
	}
	public String getStartLat() {
		return startLat;
	}
	public void setStartLat(String startLat) {
		this.startLat = startLat;
	}
	public String getStartLon() {
		return startLon;
	}
	public void setStartLon(String startLon) {
		this.startLon = startLon;
	}
	public String getEndLat() {
		return endLat;
	}
	public void setEndLat(String endLat) {
		this.endLat = endLat;
	}
	public String getEndLon() {
		return endLon;
	}
	public void setEndLon(String endLon) {
		this.endLon = endLon;
	}
	
	
}
