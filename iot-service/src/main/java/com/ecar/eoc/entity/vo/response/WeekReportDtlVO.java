package com.ecar.eoc.entity.vo.response;

import java.io.Serializable;

public class WeekReportDtlVO implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Integer terminalId;
	private String score;
	private String scoreTag;
	private String startDate;
	private String endDate;
	private float mileage;
	private float mileageTrend;
	private String mileageDefeat;
	private float duration;
	private float durationTrend;
	private String durationDefeat;
	private int drivers;
	private int driversTrend;
	private float maxMileage;
	private float maxMileageTrend;
	private float maxSpeed;
	private float maxSpeedTrend;
	private Integer peccancy;
	private Integer peccancyTrend;
	private Integer acce;
	private Integer acceTrend;
	private Integer brake;
	private Integer brakeTrend;
	private Integer turn;
	private Integer turnTrend;
	private String mileageDurations;
	private String comboName;
	private String validate;
	private int onlineDays;
	private String carNum;
	
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
	public String getScoreTag() {
		return scoreTag;
	}
	public void setScoreTag(String scoreTag) {
		this.scoreTag = scoreTag;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public float getMileage() {
		return mileage;
	}
	public void setMileage(float mileage) {
		this.mileage = mileage;
	}
	public float getMileageTrend() {
		return mileageTrend;
	}
	public void setMileageTrend(float mileageTrend) {
		this.mileageTrend = mileageTrend;
	}
	public String getMileageDefeat() {
		return mileageDefeat;
	}
	public void setMileageDefeat(String mileageDefeat) {
		this.mileageDefeat = mileageDefeat;
	}
	public float getDuration() {
		return duration;
	}
	public void setDuration(float duration) {
		this.duration = duration;
	}
	public float getDurationTrend() {
		return durationTrend;
	}
	public void setDurationTrend(float durationTrend) {
		this.durationTrend = durationTrend;
	}
	public String getDurationDefeat() {
		return durationDefeat;
	}
	public void setDurationDefeat(String durationDefeat) {
		this.durationDefeat = durationDefeat;
	}
	public int getDrivers() {
		return drivers;
	}
	public void setDrivers(int drivers) {
		this.drivers = drivers;
	}
	public int getDriversTrend() {
		return driversTrend;
	}
	public void setDriversTrend(int driversTrend) {
		this.driversTrend = driversTrend;
	}
	public float getMaxMileage() {
		return maxMileage;
	}
	public void setMaxMileage(float maxMileage) {
		this.maxMileage = maxMileage;
	}
	public float getMaxMileageTrend() {
		return maxMileageTrend;
	}
	public void setMaxMileageTrend(float maxMileageTrend) {
		this.maxMileageTrend = maxMileageTrend;
	}
	public float getMaxSpeed() {
		return maxSpeed;
	}
	public void setMaxSpeed(float maxSpeed) {
		this.maxSpeed = maxSpeed;
	}
	public float getMaxSpeedTrend() {
		return maxSpeedTrend;
	}
	public void setMaxSpeedTrend(float maxSpeedTrend) {
		this.maxSpeedTrend = maxSpeedTrend;
	}
	public Integer getPeccancy() {
		return peccancy;
	}
	public void setPeccancy(Integer peccancy) {
		this.peccancy = peccancy;
	}
	public Integer getPeccancyTrend() {
		return peccancyTrend;
	}
	public void setPeccancyTrend(Integer peccancyTrend) {
		this.peccancyTrend = peccancyTrend;
	}
	public Integer getAcce() {
		return acce;
	}
	public void setAcce(Integer acce) {
		this.acce = acce;
	}
	public Integer getAcceTrend() {
		return acceTrend;
	}
	public void setAcceTrend(Integer acceTrend) {
		this.acceTrend = acceTrend;
	}
	public Integer getBrake() {
		return brake;
	}
	public void setBrake(Integer brake) {
		this.brake = brake;
	}
	public Integer getBrakeTrend() {
		return brakeTrend;
	}
	public void setBrakeTrend(Integer brakeTrend) {
		this.brakeTrend = brakeTrend;
	}
	public Integer getTurn() {
		return turn;
	}
	public void setTurn(Integer turn) {
		this.turn = turn;
	}
	public Integer getTurnTrend() {
		return turnTrend;
	}
	public void setTurnTrend(Integer turnTrend) {
		this.turnTrend = turnTrend;
	}
	public String getMileageDurations() {
		return mileageDurations;
	}
	public void setMileageDurations(String mileageDurations) {
		this.mileageDurations = mileageDurations;
	}
	public String getComboName() {
		return comboName;
	}
	public void setComboName(String comboName) {
		this.comboName = comboName;
	}
	public String getValidate() {
		return validate;
	}
	public void setValidate(String validate) {
		this.validate = validate;
	}
	public int getOnlineDays() {
		return onlineDays;
	}
	public void setOnlineDays(int onlineDays) {
		this.onlineDays = onlineDays;
	}
	public String getCarNum() {
		return carNum;
	}
	public void setCarNum(String carNum) {
		this.carNum = carNum;
	}

}
