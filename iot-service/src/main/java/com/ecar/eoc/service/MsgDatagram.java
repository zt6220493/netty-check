package com.ecar.eoc.service;

public class MsgDatagram {

    private static final long serialVersionUID = 1L;
    private int length;
    private String header;
    private String cmdId;
    private String terminalId;
    private String token;
    private String version;
    private long serialNo;
    private byte[] message;
    private String pushContent;
    private String pushType;
    private String appType;
    private byte checksum;
    private boolean resultChecksum;
    private String phoneNumber;
    private String deviceType;
    private Integer appId;
    private String appKey;

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getCmdId() {
        return cmdId;
    }

    public void setCmdId(String cmdId) {
        this.cmdId = cmdId;
    }

    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public long getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(long serialNo) {
        this.serialNo = serialNo;
    }

    public byte[] getMessage() {
        return message;
    }

    public void setMessage(byte[] message) {
        this.message = message;
    }

    public String getPushContent() {
        return pushContent;
    }

    public void setPushContent(String pushContent) {
        this.pushContent = pushContent;
    }

    public String getPushType() {
        return pushType;
    }

    public void setPushType(String pushType) {
        this.pushType = pushType;
    }

    public String getAppType() {
        return appType;
    }

    public void setAppType(String appType) {
        this.appType = appType;
    }

    public byte getChecksum() {
        return checksum;
    }

    public void setChecksum(byte checksum) {
        this.checksum = checksum;
    }

    public boolean isResultChecksum() {
        return resultChecksum;
    }

    public void setResultChecksum(boolean resultChecksum) {
        this.resultChecksum = resultChecksum;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public Integer getAppId() {
        return appId;
    }

    public void setAppId(Integer appId) {
        this.appId = appId;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }
}
