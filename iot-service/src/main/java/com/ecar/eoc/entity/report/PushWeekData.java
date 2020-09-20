package com.ecar.eoc.entity.report;

/**
 * 周推送数据
 * 推送时按照finishTs 最新完成统计进行推送
 * @author Percy Chuang
 */
public class PushWeekData {
	
	private Integer terminalId; //设备id
	private String score;		//周得分
	private String scores;		//当前周一至周日每日分数。以逗号分隔再以|分隔:如1#87,2#56,7#90表示周一87分
	private String travels;		//周行驶次数
	private String mileage;		//周行驶里程
	private String duration;	//周行驶时长
	private String durationFMT; //格式化后的时长 (如63.5分钟，格式化后：01:03)
    private String avgSpeed;	//平均时速(km/h)
    private String maxSpeed;	//最大速度(km/h)
    private String grade;	    //级别
    private String acce;		//急加速次数 
	private String brake;		//急减速次数
	private String turn;		//急转弯次数	
	private String speeding;	//超速行驶次数
    private String weekEarnings; //周收益
    private String totalEarnings;//总收益
    private String finishTime;	 //周报统计完成时间
    private String finishTs;	 //周报统计完成时间出
    private String startdate;	//日期字符串(精确到日 yyyy-MM-dd) 周开始时间
    private String enddate;		//日期字符串(精确到日 yyyy-MM-dd) 周结束时间
    
    private String flow;		//已用流量
    private String carNum;		//车牌号
    private Integer pec;		//违章次数
    private String maxMileage;	//最大里程
    
    private Integer delete;   //是否删除，非空为1：表示已删除，字段为空，表示未删除
    
	public Integer getTerminalId() {
		return terminalId;
	}
	public void setTerminalId(Integer terminalId) {
		this.terminalId = terminalId;
	}
	public String getScore() {
		return score;
	}
	public void setScore(String score) {
		this.score = score;
	}
	public String getScores() {
		return scores;
	}
	public void setScores(String scores) {
		this.scores = scores;
	}
	public String getTravels() {
		return travels;
	}
	public void setTravels(String travels) {
		this.travels = travels;
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
	public String getWeekEarnings() {
		return weekEarnings;
	}
	public void setWeekEarnings(String weekEarnings) {
		this.weekEarnings = weekEarnings;
	}
	public String getTotalEarnings() {
		return totalEarnings;
	}
	public void setTotalEarnings(String totalEarnings) {
		this.totalEarnings = totalEarnings;
	}
	public String getFinishTime() {
		return finishTime;
	}
	public void setFinishTime(String finishTime) {
		this.finishTime = finishTime;
	}
	public String getStartdate() {
		return startdate;
	}
	public void setStartdate(String startdate) {
		this.startdate = startdate;
	}
	public String getEnddate() {
		return enddate;
	}
	public void setEnddate(String enddate) {
		this.enddate = enddate;
	}
	public String getGrade() {
		return grade;
	}
	public void setGrade(String grade) {
		this.grade = grade;
	}
	public String getFinishTs() {
		return finishTs;
	}
	public void setFinishTs(String finishTs) {
		this.finishTs = finishTs;
	}
	public Integer getDelete() {
		return delete;
	}
	public void setDelete(Integer delete) {
		this.delete = delete;
	}
	public String getFlow() {
		return flow;
	}
	public void setFlow(String flow) {
		this.flow = flow;
	}
	public String getCarNum() {
		return carNum;
	}
	public void setCarNum(String carNum) {
		this.carNum = carNum;
	}
	public Integer getPec() {
		return pec;
	}
	public void setPec(Integer pec) {
		this.pec = pec;
	}
	public String getMaxMileage() {
		return maxMileage;
	}
	public void setMaxMileage(String maxMileage) {
		this.maxMileage = maxMileage;
	}
	public String getMaxSpeed() {
		return maxSpeed;
	}
	public void setMaxSpeed(String maxSpeed) {
		this.maxSpeed = maxSpeed;
	}
}
