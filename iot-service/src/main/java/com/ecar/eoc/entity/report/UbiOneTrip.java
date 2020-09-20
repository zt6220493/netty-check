package com.ecar.eoc.entity.report;

public class UbiOneTrip {
    private Integer terminalId; //设备id  //加索引
    private String publicId;
    private String openId;
    private String imei;
    private String iccid;
    private String tripCode;    //相当于单号   唯一索引
    private String score;
    private Float mileage;
    private Double startLon;   //行程开始gps经度位置
    private Double startLat;   //行程开始gps纬度位置
    private Double endLon;    //行程结束gps经度位置
    private Double endLat;    //行程结束gps纬度位置
    private String startAdress;  //来源于地理位置模块
    private String endAdress;    //同上
    private int totalTime;       //总时长 存秒，页面转为 min
    private String startTime;    //2017/07/07 4:12 加索引
    private String endTime;      //里程结束时间    加索引
    private Float maxSpeed;     //最大速度 km/h
    private Float avgSpeed;     //平均速度 km/h
    private int acceTimes;       //急加速次数
    private int deceTimes;         //急减速次数
    private int turnTimes;         //急转弯次数
    private int overSpeedTimes;    //超速次数
    private int tiredDrivingTimes; //疲劳驾驶次数
    private int ranking;           //排名得分，用作实时排名
    private int preRanking;        //上一次排名(超越的数量百分比乘以100)
    private int cityCode;          //排名城市code 来源地理位置 默认为0  索引
    private String rankingCity;    //排名城市   来源地理位置 默认为null
    private String events;         //事件JSON串，注意TYPE设置和数组转换。[{}]
    //add at 2017-8-23 by pom
    private int congestionDura;	//拥堵时长，单位:min,计算公式：里程数除以40所得时长减去驾驶时长
    private int controlScore;		//操控得分
    private int economyScore;		//经济得分
    private int focusScore;			//专注得分
    private int roadScore;			//路况得分
    private int envirScore;			//环境得分
    //add at 2018-8-10 by pom
    private Integer delete;		//是否删除，非空为1：表示已删除，字段为空，表示未删除
    
    public Integer getTerminalId() {
        return terminalId;
    }
    public void setTerminalId(Integer terminalId) {
        this.terminalId = terminalId;
    }
    public String getPublicId() {
        return publicId;
    }
    public void setPublicId(String publicId) {
        this.publicId = publicId;
    }
    public String getOpenId() {
        return openId;
    }
    public void setOpenId(String openId) {
        this.openId = openId;
    }
    public String getImei() {
		return imei;
	}
	public void setImei(String imei) {
		this.imei = imei;
	}
	public String getIccid() {
		return iccid;
	}
	public void setIccid(String iccid) {
		this.iccid = iccid;
	}
	public String getTripCode() {
        return tripCode;
    }
    public void setTripCode(String tripCode) {
        this.tripCode = tripCode;
    }
    public String getScore() {
        return score;
    }
    public void setScore(String score) {
        this.score = score;
    }
    public Float getMileage() {
        return mileage;
    }
    public void setMileage(Float mileage) {
        this.mileage = mileage;
    }
    public Double getStartLon() {
        return startLon;
    }
    public void setStartLon(Double startLon) {
        this.startLon = startLon;
    }
    public Double getStartLat() {
        return startLat;
    }
    public void setStartLat(Double startLat) {
        this.startLat = startLat;
    }
    public Double getEndLon() {
        return endLon;
    }
    public void setEndLon(Double endLon) {
        this.endLon = endLon;
    }
    public Double getEndLat() {
        return endLat;
    }
    public void setEndLat(Double endLat) {
        this.endLat = endLat;
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
    public int getTotalTime() {
        return totalTime;
    }
    public void setTotalTime(int totalTime) {
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
    public Float getMaxSpeed() {
        return maxSpeed;
    }
    public void setMaxSpeed(Float maxSpeed) {
        this.maxSpeed = maxSpeed;
    }
    public Float getAvgSpeed() {
        return avgSpeed;
    }
    public void setAvgSpeed(Float avgSpeed) {
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
	public int getRanking() {
        return ranking;
    }
    public void setRanking(int ranking) {
        this.ranking = ranking;
    }
    public int getPreRanking() {
        return preRanking;
    }
    public void setPreRanking(int preRanking) {
        this.preRanking = preRanking;
    }
    public int getCityCode() {
        return cityCode;
    }
    public void setCityCode(int cityCode) {
        this.cityCode = cityCode;
    }
    public String getRankingCity() {
        return rankingCity;
    }
    public void setRankingCity(String rankingCity) {
        this.rankingCity = rankingCity;
    }
    public String getEvents() {
        return events;
    }
    public void setEvents(String events) {
        this.events = events;
    }
	public int getCongestionDura() {
		return congestionDura;
	}
	public void setCongestionDura(int congestionDura) {
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
	public Integer getDelete() {
		return delete;
	}
	public void setDelete(Integer delete) {
		this.delete = delete;
	}
}
