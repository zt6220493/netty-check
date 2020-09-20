package com.ecar.eoc.entity.eoc;

import java.io.Serializable;
import java.util.Date;
import org.springframework.util.StringUtils;

public class MsgTerminal implements Serializable, Cloneable {

	private static final long serialVersionUID = 1L;

	/**
	 * 用户ID
	 */
	private Integer terminalId;

	/**
	 * 用户ID
	 */
	private Integer cid;

	/**
	 * 从TSP同步过来的ID
	 */
	private Integer asynTspId;

	/**
	 * 从TSP同步过来的用户类型
	 */
	private Integer asynTspType;

	/**
	 * 从TSP同步过来的设备Id
	 */
	private Integer asynTerminalId;

	/**
	 * 用户token
	 */
	private String token;

	/**
	 * token类型 1,IMSI(SIM卡序列码) 2,SIM(SIM码) 3,IMEI(手机设备码) 4,openUDID(IOS唯一设备标识码)
	 * 5,DIVICE TOKEN(APNS返回给IOS)
	 */
	private Integer tokenType;

	/**
	 * 终端ID
	 */
	private String deviceId;

	/**
	 * 终端ID类型
	 */
	private Integer deviceIdType;

	/**
	 * 终端类型名称
	 */
	private String deviceTypeName;

	/**
	 * 终端操作系统 1,Android 2,IOS 3,WindowsPhone
	 */
	private Integer terminalOS;

	/**
	 * 终端操作系统版本号
	 */
	private String osVersion;

	/**
	 * 设备手机号
	 */
	private String deviceMobile;

	/**
	 * 用户手机
	 */
	private String mobile;

	/**
	 * 车架号
	 */
	private String vin;

	/**
	 * 是否验证 1,未验证 2,已验证
	 */
	private Integer isAuth;

	/**
	 * 验证状态 1 未验证 2 验证成功 3 验证失败
	 */
	private Integer authState;

	/**
	 * IMEI号
	 */
	private String imsi;

	private String appKey;

	private Integer appKeyType;

	private Integer appType;

	private String isSendSms;

	/**
	 * 创建时间
	 */
	private Date createDate;

	/**
	 * 更新时间
	 */
	private Date updateDate;

	/**
	 * 设备状态(1:可用,2:停用)
	 */
	private Integer status;

	private Integer businessId;

	private String businessName;

	// 方案商名称
	private String programmeProviderName;

	/**
	 * 机种型号
	 */
	private String modelType;

	/**
	 * 生产订单号
	 */
	private String productionOrderNo;

	/**
	 * 整机流水号
	 */
	private String modelSerialNo;

	/**
	 * 所属车主
	 */
	private String carOwner;

	/**
	 * 备案人
	 */
	private String recordPerson;

	/**
	 * 绑定销售商
	 */
	private String bindingSeller;

	/**
	 * 查询条件
	 */
	private String searchMapping;

	/**
	 * 纬度
	 */
	private String lat;

	/**
	 * 经度
	 */
	private String lng;

	/**
	 * 设备注册区域
	 */
	private String area;

	private Integer authorStatus; // 绑定的SIM卡实名认证状态

	private String wifiMac;

	private Date startTime;

	private Date endTime;

	private Date expireDate;

	private Integer appRegistType;	//默认为0：翼卡在线注册；1：视客相伴注册

	public Integer getAppRegistType() {
		return appRegistType;
	}

	public void setAppRegistType(Integer appRegistType) {
		this.appRegistType = appRegistType;
	}

	public Date getExpireDate() {
		return expireDate;
	}

	public void setExpireDate(Date expireDate) {
		this.expireDate = expireDate;
	}

	public Integer getTerminalId() {
		return terminalId;
	}

	public void setTerminalId(Integer terminalId) {
		this.terminalId = terminalId;
	}

	public Integer getCid() {
		return cid;
	}

	public void setCid(Integer cid) {
		this.cid = cid;
	}

	public Integer getAsynTspId() {
		return asynTspId;
	}

	public void setAsynTspId(Integer asynTspId) {
		this.asynTspId = asynTspId;
	}

	public Integer getAsynTspType() {
		return asynTspType;
	}

	public void setAsynTspType(Integer asynTspType) {
		this.asynTspType = asynTspType;
	}

	public Integer getAsynTerminalId() {
		return asynTerminalId;
	}

	public void setAsynTerminalId(Integer asynTerminalId) {
		this.asynTerminalId = asynTerminalId;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Integer getTokenType() {
		return tokenType;
	}

	public void setTokenType(Integer tokenType) {
		this.tokenType = tokenType;
	}

	public Integer getTerminalOS() {
		return terminalOS;
	}

	public void setTerminalOS(Integer terminalOS) {
		this.terminalOS = terminalOS;
	}

	public String getOsVersion() {
		return osVersion;
	}

	public void setOsVersion(String osVersion) {
		this.osVersion = osVersion;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getDeviceMobile() {
		return deviceMobile;
	}

	public void setDeviceMobile(String deviceMobile) {
		this.deviceMobile = deviceMobile;
	}

	public String getVin() {
		return vin;
	}

	public void setVin(String vin) {
		this.vin = vin;
	}

	public Integer getIsAuth() {
		return isAuth;
	}

	public void setIsAuth(Integer isAuth) {
		this.isAuth = isAuth;
	}

	public Integer getAuthState() {
		return authState;
	}

	public void setAuthState(Integer authState) {
		this.authState = authState;
	}

	public String getImsi() {
		return imsi;
	}

	public void setImsi(String imsi) {
		this.imsi = imsi;
	}

	public String getAppKey() {
		return appKey;
	}

	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}

	public Integer getAppType() {
		return appType;
	}

	public void setAppType(Integer appType) {
		this.appType = appType;
	}

	public Integer getAppKeyType() {
		return appKeyType;
	}

	public void setAppKeyType(Integer appKeyType) {
		this.appKeyType = appKeyType;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public Integer getDeviceIdType() {
		return deviceIdType;
	}

	public void setDeviceIdType(Integer deviceIdType) {
		this.deviceIdType = deviceIdType;
	}

	public String getDeviceTypeName() {
		return deviceTypeName;
	}

	public void setDeviceTypeName(String deviceTypeName) {
		this.deviceTypeName = deviceTypeName;
	}

	public String getIsSendSms() {
		return isSendSms;
	}

	public void setIsSendSms(String isSendSms) {
		this.isSendSms = isSendSms;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public String getBusinessName() {
		return businessName;
	}

	public void setBusinessName(String businessName) {
		this.businessName = businessName;
	}

	public String getProgrammeProviderName() {
		return programmeProviderName;
	}

	public void setProgrammeProviderName(String programmeProviderName) {
		this.programmeProviderName = programmeProviderName;
	}

	public Integer getBusinessId() {
		return businessId;
	}

	public void setBusinessId(Integer businessId) {
		this.businessId = businessId;
	}

	public String getBindingSeller() {
		return bindingSeller;
	}

	public void setBindingSeller(String bindingSeller) {
		this.bindingSeller = bindingSeller;
	}

	public String getCarOwner() {
		return carOwner;
	}

	public void setCarOwner(String carOwner) {
		this.carOwner = carOwner;
	}

	public String getModelSerialNo() {
		return modelSerialNo;
	}

	public void setModelSerialNo(String modelSerialNo) {
		this.modelSerialNo = modelSerialNo;
	}

	public String getModelType() {
		return modelType;
	}

	public void setModelType(String modelType) {
		this.modelType = modelType;
	}

	public String getProductionOrderNo() {
		return productionOrderNo;
	}

	public void setProductionOrderNo(String productionOrderNo) {
		this.productionOrderNo = productionOrderNo;
	}

	public String getRecordPerson() {
		return recordPerson;
	}

	public void setRecordPerson(String recordPerson) {
		this.recordPerson = recordPerson;
	}

	public String getLat() {
		return lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}

	public String getLng() {
		return lng;
	}

	public void setLng(String lng) {
		this.lng = lng;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public Integer getAuthorStatus() {
		return authorStatus;
	}

	public void setAuthorStatus(Integer authorStatus) {
		this.authorStatus = authorStatus;
	}

	public String getWifiMac() {
		return wifiMac;
	}

	public void setWifiMac(String wifiMac) {
		this.wifiMac = wifiMac;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("MsgTerminal >> :");
		sb.append("[\n");
		if (!StringUtils.isEmpty(terminalId)) {
			sb.append(" terminalId : ");
			sb.append(terminalId);
			sb.append(",\n");
		}
		if (!StringUtils.isEmpty(cid)) {
			sb.append(" cid : ");
			sb.append(cid);
			sb.append(",\n");
		}
		if (!StringUtils.isEmpty(asynTspId)) {
			sb.append(" asynTspId : ");
			sb.append(asynTspId);
			sb.append(",\n");
		}
		if (!StringUtils.isEmpty(asynTerminalId)) {
			sb.append(" asynTerminalId : ");
			sb.append(asynTerminalId);
			sb.append(",\n");
		}
		if (!StringUtils.isEmpty(mobile)) {
			sb.append(" mobile : ");
			sb.append(mobile);
			sb.append(",\n");
		}
		if (!StringUtils.isEmpty(deviceMobile)) {
			sb.append(" deviceMobile : ");
			sb.append(deviceMobile);
			sb.append(",\n");
		}
		if (!StringUtils.isEmpty(token)) {
			sb.append(" token : ");
			sb.append(token);
			sb.append(",\n");
		}
		if (!StringUtils.isEmpty(tokenType)) {
			sb.append(" tokenType : ");
			sb.append(tokenType);
			sb.append(",\n");
		}
		if (!StringUtils.isEmpty(deviceId)) {
			sb.append(" deviceId : ");
			sb.append(deviceId);
			sb.append(",\n");
		}
		if (!StringUtils.isEmpty(imsi)) {
			sb.append(" imsi : ");
			sb.append(imsi);
			sb.append(",\n");
		}
		if (!StringUtils.isEmpty(deviceIdType)) {
			sb.append(" deviceIdType : ");
			sb.append(deviceIdType);
			sb.append(",\n");
		}
		if (!StringUtils.isEmpty(terminalOS)) {
			sb.append(" terminalOS : ");
			sb.append(terminalOS);
			sb.append(",\n");
		}
		if (!StringUtils.isEmpty(osVersion)) {
			sb.append(" osVersion : ");
			sb.append(osVersion);
			sb.append(",\n");
		}
		if (!StringUtils.isEmpty(isAuth)) {
			sb.append(" isAuth : ");
			sb.append(isAuth);
			sb.append(",\n");
		}
		if (!StringUtils.isEmpty(vin)) {
			sb.append(" vin : ");
			sb.append(vin);
			sb.append(",\n");
		}
		if (!StringUtils.isEmpty(authState)) {
			sb.append(" authState : ");
			sb.append(authState);
			sb.append(",\n");
		}
		if (!StringUtils.isEmpty(createDate)) {
			sb.append(" createDate : ");
			sb.append(createDate);
			sb.append(",\n");
		}
		if (!StringUtils.isEmpty(appKey)) {
			sb.append(" appKey : ");
			sb.append(appKey);
			sb.append(",\n");
		}
		if (!StringUtils.isEmpty(appKeyType)) {
			sb.append(" appKeyType : ");
			sb.append(appKeyType);
			sb.append(",\n");
		}
		if (!StringUtils.isEmpty(appType)) {
			sb.append(" appType : ");
			sb.append(appType);
			sb.append(",\n");
		}
		if (!StringUtils.isEmpty(isSendSms)) {
			sb.append(" isSendSms : ");
			sb.append(isSendSms);
			sb.append(",\n");
		}
		if (!StringUtils.isEmpty(status)) {
			sb.append(" status : ");
			sb.append(status);
			sb.append(",\n");
		}
		if (!StringUtils.isEmpty(updateDate)) {
			sb.append(" updateDate : ");
			sb.append(updateDate);
			sb.append(",\n");
		}
		if (!StringUtils.isEmpty(businessName)) {
			sb.append(" businessName : ");
			sb.append(businessName);
			sb.append(",\n");
		}
		if (!StringUtils.isEmpty(programmeProviderName)) {
			sb.append(" programmeProviderName : ");
			sb.append(programmeProviderName);
			sb.append(",\n");
		}
		if (!StringUtils.isEmpty(businessId)) {
			sb.append(" businessId : ");
			sb.append(businessId);
			sb.append(",\n");
		}
		if (!StringUtils.isEmpty(modelType)) {
			sb.append(" modelType : ");
			sb.append(modelType);
			sb.append(",\n");
		}
		if (!StringUtils.isEmpty(productionOrderNo)) {
			sb.append(" productionOrderNo : ");
			sb.append(productionOrderNo);
			sb.append(",\n");
		}
		if (!StringUtils.isEmpty(modelSerialNo)) {
			sb.append(" modelSerialNo : ");
			sb.append(modelSerialNo);
			sb.append(",\n");
		}
		if (!StringUtils.isEmpty(carOwner)) {
			sb.append(" carOwner : ");
			sb.append(carOwner);
			sb.append(",\n");
		}
		if (!StringUtils.isEmpty(recordPerson)) {
			sb.append(" recordPerson : ");
			sb.append(recordPerson);
			sb.append(",\n");
		}
		if (!StringUtils.isEmpty(bindingSeller)) {
			sb.append(" bindingSeller : ");
			sb.append(bindingSeller);
			sb.append(",\n");
		}
		if (!StringUtils.isEmpty(lat)) {
			sb.append(" lat : ");
			sb.append(lat);
			sb.append(",\n");
		}
		if (!StringUtils.isEmpty(lng)) {
			sb.append(" lng : ");
			sb.append(lng);
			sb.append(",\n");
		}
		if (!StringUtils.isEmpty(area)) {
			sb.append(" area : ");
			sb.append(area);
			sb.append(",\n");
		}
		if (!StringUtils.isEmpty(wifiMac)) {
			sb.append(" wifiMac : ");
			sb.append(wifiMac);
			sb.append(",\n");
		}
		sb.append("]");
		return sb.toString();
	}

	@Override
	public MsgTerminal clone() {
		MsgTerminal customer = null;
		try {
			customer = (MsgTerminal) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return customer;
	}

	public String getSearchMapping() {
		return searchMapping;
	}

	public void setSearchMapping(String searchMapping) {
		this.searchMapping = searchMapping;
	}
}