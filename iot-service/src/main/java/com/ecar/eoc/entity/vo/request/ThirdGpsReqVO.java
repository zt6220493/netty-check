package com.ecar.eoc.entity.vo.request;

import java.util.List;

public class ThirdGpsReqVO {

    private String type;

    private String imei;

    private Long timestamp;

    private List<GpsVO> gps;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public List<GpsVO> getGps() {
        return gps;
    }

    public void setGps(List<GpsVO> gps) {
        this.gps = gps;
    }
}

class GpsVO {

    private Double latitude;    // 纬度

    private Double longitude;   // 经度

    private Long timestamp;     // 时间戳

    private Double speed;       // 速度

    private Integer bearing;    // ⽅向 0 正北

    private Integer haccuracy;  // ⽔平GPS精度

    private Integer altitude;   // 海拔

    private Integer vaccuracy;  // 垂直海拔精度

    private Integer satellite;  // 卫星个数

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public Double getSpeed() {
        return speed;
    }

    public void setSpeed(Double speed) {
        this.speed = speed;
    }

    public Integer getBearing() {
        return bearing;
    }

    public void setBearing(Integer bearing) {
        this.bearing = bearing;
    }

    public Integer getHaccuracy() {
        return haccuracy;
    }

    public void setHaccuracy(Integer haccuracy) {
        this.haccuracy = haccuracy;
    }

    public Integer getAltitude() {
        return altitude;
    }

    public void setAltitude(Integer altitude) {
        this.altitude = altitude;
    }

    public Integer getVaccuracy() {
        return vaccuracy;
    }

    public void setVaccuracy(Integer vaccuracy) {
        this.vaccuracy = vaccuracy;
    }

    public Integer getSatellite() {
        return satellite;
    }

    public void setSatellite(Integer satellite) {
        this.satellite = satellite;
    }
}