package com.ecar.eoc.entity.vo.request;

/**
 * 围栏
 */
public class FenceReqVO {

    private Integer id; //围栏ID

    private Integer terminalId; //设备ID

    private Double radius; //半径，单位米

    private Double lat; //纬度

    private Double lon; //纬度

    private Integer type; //报警类型：0 驶入报警，1 驶出报警，2 出入报警

    private Integer preStatus; //上次报警时状态：0：围栏内，1：围栏外

    private String callbackUrl; //围栏通知回调地址

    public String getCallbackUrl() {
        return callbackUrl;
    }

    public void setCallbackUrl(String callbackUrl) {
        this.callbackUrl = callbackUrl;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(Integer terminalId) {
        this.terminalId = terminalId;
    }

    public Double getRadius() {
        return radius;
    }

    public void setRadius(Double radius) {
        this.radius = radius;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getPreStatus() {
        return preStatus;
    }

    public void setPreStatus(Integer preStatus) {
        this.preStatus = preStatus;
    }
}
