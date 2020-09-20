package com.ecar.eoc.entity.vo.response;

import java.io.Serializable;
import com.ecar.eoc.entity.vo.response.conf.ConfServer;
import com.ecar.eoc.entity.vo.response.conf.OffUploadConf;
import com.ecar.eoc.entity.vo.response.conf.AccOn;
import com.ecar.eoc.entity.vo.response.conf.UploadConf;

/**
 * 终端IOT配置信息
 * @author pom
 *
 */
public class ConfResVO implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private ConfServer server;		//TCP服务器对象配置
	private UploadConf nomalUpload;	//正常gps上传对象
	private UploadConf acUpload;	//gps加速上传对象
	private OffUploadConf offUpload;//离线数据上传对象
	private Long thresholdTime;		//gps在线离线判定阈值，linux毫秒时间戳
	private Integer expireDuration;	//认证失效时长（断网多长时间需要重新调用该接口），单位小时
	private AccOn accOn; 			//启动提醒对象
	private Integer collectPeriod;	//采样频率 n秒采集1个GPS点
	private String token;	//连接token指定
	
	public ConfServer getServer() {
		return server;
	}
	public void setServer(ConfServer server) {
		this.server = server;
	}
	public UploadConf getNomalUpload() {
		return nomalUpload;
	}
	public void setNomalUpload(UploadConf nomalUpload) {
		this.nomalUpload = nomalUpload;
	}
	public UploadConf getAcUpload() {
		return acUpload;
	}
	public void setAcUpload(UploadConf acUpload) {
		this.acUpload = acUpload;
	}
	public OffUploadConf getOffUpload() {
		return offUpload;
	}
	public void setOffUpload(OffUploadConf offUpload) {
		this.offUpload = offUpload;
	}
	public Long getThresholdTime() {
		return thresholdTime;
	}
	public void setThresholdTime(Long thresholdTime) {
		this.thresholdTime = thresholdTime;
	}
	public Integer getExpireDuration() {
		return expireDuration;
	}
	public void setExpireDuration(Integer expireDuration) {
		this.expireDuration = expireDuration;
	}
	public AccOn getAccOn() {
		return accOn;
	}
	public void setAccOn(AccOn accOn) {
		this.accOn = accOn;
	}
	public Integer getCollectPeriod() {
		return collectPeriod;
	}
	public void setCollectPeriod(Integer collectPeriod) {
		this.collectPeriod = collectPeriod;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}

}
