package com.ecar.eoc.service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.ecar.eoc.utils.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.ecar.eoc.common.BaseInfo;
import com.ecar.eoc.common.Constant;
import com.ecar.eoc.config.ConfigurationManager;
import com.ecar.eoc.entity.eoc.DefanceRecord;
import com.ecar.eoc.entity.eoc.MsgTerminal;
import com.ecar.eoc.entity.eoc.WechatBind;
import com.ecar.eoc.entity.vo.request.IgniteEventReqVO;
import com.ecar.eoc.mapper.eocdb.eoc.DefenceRecordMapper;
import com.ecar.eoc.mapper.eocdb.eoc.WechatBindMapper;
import com.ecar.eoc.utils.GpsFilter.GpsInfo;

@Service
public class RemindService {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private RedisTemplate<String,Object>  redisTemple;
	@Autowired
	private DefenceRecordMapper defenceRecordMapper;
//	@Autowired
//	private DefenceListMapper defenceListMapper;
	@Autowired
	private WechatBindMapper wechatBindMapper;
	@Autowired
	private TerminalService terminalService;
	
	/**
	 * 点火提醒
	 * @param requestVO
	 */
	public void startRemind(IgniteEventReqVO requestVO) {
		//数据不正确，不予启动提醒
		if(null==requestVO.getLat() || null==requestVO.getLon() 
				|| null==requestVO.getGpsTime()) {
			logger.warn("startRemind warn data can not be null,request:"+JSON.toJSONString(requestVO));
			return;
		}
		if(requestVO.getAcc()!=null && requestVO.getAcc().intValue()!=1) {
			logger.warn("startRemind warn acc off,not remind,request:"+JSON.toJSONString(requestVO));
			return;
		}
//		MsgTerminal m = new MsgTerminal();
//		m.setTerminalId(requestVO.getTerminalId());
//		m = terminalMapper.getLastMsgTerminal(m);
		//TODO 20191009
		MsgTerminal m = terminalService.getMsgTerminalById(requestVO.getTerminalId());
		if(null==m) {
			logger.warn("startRemind warn,device is not exist,request:"+JSON.toJSONString(requestVO));
			return;
		}
		//1、启动提醒灰度开关处理
//		Object obj = null;
//		DefenceList defenceItem = defenceListMapper.selectByPk(requestVO.getTerminalId());
//		boolean flag = false;	//推送走灰度名单 flag ： true 是；false 否
//		if(null!=defenceItem){
//			flag = true;
//		}
//		if("open".equals(ConfigurationManager.getProperty("eoc.startremind.product.switch","close"))
//				|| ("open".equals(ConfigurationManager.getProperty("eoc.startremind.push.switch","open")) && flag)){
//			obj = "0";
//		}else{
//			obj = redisTemple.opsForValue().get(Constant.DEVICETYPE_THIEVERY + requestVO.getTerminalId().toString());
//		}
//		if (null == obj){
//			logger.warn("startRemind warn terminalId:"+requestVO.getTerminalId()+", device is not defance,设备处于撤防状态!");
//			return;
//		}
		//增加只夜间原地设防功能
//		String flagStr = obj.toString().trim();
//		if(!("".equals(flagStr))){
//			if(Constant.SITU_FORTIFICATION_NO.endsWith(flagStr)){
//				logger.warn("startRemind warn terminalId:"+requestVO.getTerminalId()+", device is not defance,设备处于撤防状态!");
//				return;
//			}else if(Constant.SITU_FORTIFICATION_NIGHT.endsWith(flagStr)){
//				//只晚上原地设防，则非设防时间【可配置】，不提醒
//				String[] beginTime = ConfigurationManager.getProperty("DEFENCE_NIGHT_START", "22:00").split(":");
//				String[] endTime = ConfigurationManager.getProperty("DEFENCE_NIGHT_END", "07:00").split(":");
//				int localSeconds = DateUtil.getNowLocalDaySeconds();
//				if(localSeconds<(Integer.valueOf(beginTime[0])*3600+Integer.valueOf(beginTime[1])*60) && localSeconds>(Integer.valueOf(endTime[0])*3600+Integer.valueOf(endTime[1])*60)){
//					logger.warn("startRemind warn terminalId:" + requestVO.getTerminalId() + ",device is not defance,night defence.非设防时段 不予推送设防消息!");
//					return;
//				}
//			}
//		}
		
		//2、对点火的终端系统时间与gps时间进行判定
		long accOnTime = requestVO.getTimestamp().longValue();
		long gpsTime = requestVO.getGpsTime().longValue();
		long curTime = System.currentTimeMillis();
		boolean accTimeRight = true;	//点火设备时间是否可用
		
		Date nowTime = new Date();
		Long remindTime = null;
		//2、设备的系统时间判定:超过当前时间超过半年的，判定设备系统时间不可用,以gps时间为判定依据
		if(curTime-accOnTime > Constant.VALID_TIME_SCALE) {
			accTimeRight = false;	//设备点火时间不可用
		}else if(curTime-accOnTime > ConfigurationManager.getInteger("iot.remind.validtime.seconds", 3*3600)*1000l) {
			//点火指令系统时间超过当前时间3个小时，点火指令又有效
			DefanceRecord defance = new DefanceRecord();
			defance.setTerminalId(requestVO.getTerminalId());
			defance.setDeviceId(requestVO.getImei());
			defance.setOpenId("");
			defance.setPublicId("");
			defance.setCreateTime(nowTime);
			defance.setIsSuccess(Constant.DEFANCE_PUSH_FAIL);
			defance.setMsg("last acc on remind time is "+new Date(accOnTime)+",not within the time frame,so no remind");
			//记录原地设防 发送信息到数据库
			defenceRecordMapper.insert(defance);
			if(logger.isWarnEnabled()) {
				logger.warn("startRemind warn terminalId:" + requestVO.getTerminalId() + ",acc on remind time is "+new Date(accOnTime)+",not within the time frame,so no remind");
			}
			return;
		}
		//3、计算启动提醒时间
		if(!accTimeRight) {
			//设备时间无效情况下，gps时间无效，不予提醒
			if(curTime-gpsTime > Constant.VALID_TIME_SCALE) {
				DefanceRecord defance = new DefanceRecord();
				defance.setTerminalId(requestVO.getTerminalId());
				defance.setDeviceId(requestVO.getImei());
				defance.setOpenId("");
				defance.setPublicId("");
				defance.setCreateTime(nowTime);
				defance.setIsSuccess(Constant.DEFANCE_PUSH_FAIL);
				defance.setMsg("last acc on remind time is "+new Date(accOnTime)+",not within the time frame,so no remind");
				//记录原地设防 发送信息到数据库
				defenceRecordMapper.insert(defance);
				if(logger.isWarnEnabled()) {
					logger.warn("startRemind warn terminalId:" + requestVO.getTerminalId() + ",acc on remind time invalid,and gps time invalid,so no remind,request:"+JSON.toJSONString(requestVO));
				}
				return;
			}
			else if(curTime-gpsTime > ConfigurationManager.getInteger("iot.remind.validtime.seconds", 3*3600)*1000l) {
				//点火指令系统时间超过当前时间3个小时，点火指令又有效
				DefanceRecord defance = new DefanceRecord();
				defance.setTerminalId(requestVO.getTerminalId());
				defance.setDeviceId(requestVO.getImei());
				defance.setOpenId("");
				defance.setPublicId("");
				defance.setCreateTime(nowTime);
				defance.setIsSuccess(Constant.DEFANCE_PUSH_FAIL);
				defance.setMsg("last acc on remind time is invalid,and gps time "+new Date(accOnTime)+" not within the time frame,so no remind");
				//记录原地设防 发送信息到数据库
				defenceRecordMapper.insert(defance);
				if(logger.isWarnEnabled()) {
					logger.warn("startRemind warn terminalId:" + requestVO.getTerminalId() + ",acc on remind time invalid,gpsTime is "+new Date(gpsTime)+",not within the time frame,so no remind");
				}
				return;
			}
			remindTime = gpsTime; //以最新的gps时间为提醒时间（此处有风险，点火指令在获取最新gps之前，可能取到的gps时间，为历史的时间，可能不准或者被上个条件过滤）
		}else {
			//系统时间有效可用
			remindTime = accOnTime;
		}
		//4、判定 程序模拟点火 与 车机acc on点火提醒逻辑
		//提醒状态值：type_time (type:0 模拟点火,1 acc on点火;time 上次提醒linux时间戳)
		String recentRemind = (String) redisTemple.opsForValue().get("FIRING_REMIND_"+requestVO.getTerminalId());
		if(!StringUtils.isEmpty(recentRemind)){
			String[] firingArr = recentRemind.split(":");
			if(firingArr.length==2){
				//acc on 上一次提醒是acc on指令提醒
				if("1".equals(firingArr[0])){
					//一次acc指令提醒，终身acc指令提醒
					long lastRemindTime = Long.valueOf(firingArr[1]).longValue();
					//
					if(curTime- lastRemindTime < ConfigurationManager.getInteger("ACC_ON_REMIND_DURATION", 120)*1000l){
						DefanceRecord defance = new DefanceRecord();
						defance.setTerminalId(requestVO.getTerminalId());
						defance.setDeviceId(requestVO.getImei());
						defance.setOpenId("");
						defance.setPublicId("");
						defance.setCreateTime(nowTime);
						defance.setIsSuccess(Constant.DEFANCE_PUSH_FAIL);
						defance.setMsg("last acc on remind time is "+new Date(lastRemindTime)+",reminds too frequent,so no remind");
						//记录原地设防 发送信息到数据库
						defenceRecordMapper.insert(defance);
						logger.warn("startRemind warn terminalId:" + requestVO.getTerminalId() + ",reminds too frequent,so no remind");
						return;
					}
				}else{
					//acc点火指令提醒
					long lastRemindTime = Long.valueOf(firingArr[1]).longValue();
					if(curTime - lastRemindTime < ConfigurationManager.getInteger("DIFFER_REMIND_DURATION", 300)*1000l){
						DefanceRecord defance = new DefanceRecord();
						defance.setTerminalId(requestVO.getTerminalId());
						defance.setDeviceId(requestVO.getImei());
						defance.setOpenId("");
						defance.setPublicId("");
						defance.setCreateTime(nowTime);
						defance.setIsSuccess(Constant.DEFANCE_PUSH_FAIL);
						defance.setMsg("program imitate firing last time:"+new Date(lastRemindTime)+",acc on firing lose effect,reminds too frequent,so no remind");
						//记录原地设防 发送信息到数据库
						defenceRecordMapper.insert(defance);
						logger.warn("startRemind warn terminalId:" + requestVO.getTerminalId() + ",acc on firing lose effect,reminds too frequent,so no remind");
						return;
					}
				}
			}
		}
		
		//5、消息发送
		DefanceRecord defance = new DefanceRecord();
		defance.setTerminalId(m.getTerminalId());
		defance.setDeviceId(m.getDeviceId());
		defance.setCreateTime(nowTime);

		List<WechatBind> list = getWechatBind(m.getDeviceId());

		if(list==null || list.size()==0) {
			defance.setIsSuccess(Constant.DEFANCE_PUSH_FAIL);
			defance.setMsg("wechat bind relations is null");
			defenceRecordMapper.insert(defance);
			return;
		}
		//gps漂移筛选
		String lat = requestVO.getLat();
		String lon = requestVO.getLon();
		if(!StringUtils.isEmpty(requestVO.getGpsgroup())) {
			GpsInfo gpsInfo = new GpsFilter(requestVO.getGpsgroup()).filter();
			if(null!=gpsInfo) {
				lat = gpsInfo.getLat()+"";
				lon = gpsInfo.getLon()+"";
			}
		}
		
		for(WechatBind bind:list) {
			//不给非注册者发消息
			if(Constant.WEIXIN_BIND_IS_REGISTER_NO.equals(bind.getIsRegisterUser())) {
				continue;
			}
			defance.setOpenId(bind.getOpenId());
			defance.setPublicId(bind.getPublicId());
			String carNo = null;
			//1. 查询车辆信息
			try {
				String plateUrl = ConfigurationManager.getProperty("eoc.peccancy.plateNumber");
				StringBuffer params = new StringBuffer();
				params.append("openId=");
				params.append(bind.getOpenId());
				params.append("&publicId=");
				params.append(bind.getPublicId());
                params.append("&terminalId=");
                params.append(bind.getTerminalId());
				String plateResult = HttpUtil.sendGet(plateUrl, params.toString());
				if(!StringUtils.isEmpty(plateResult)){
					@SuppressWarnings("rawtypes")
					BaseInfo plateBaseInfo = JSON.parseObject(plateResult, BaseInfo.class);
					Object carData = plateBaseInfo.getData();
					if(carData!=null){
						JSONArray jsonArr = JSON.parseArray(plateBaseInfo.getData().toString());
						if(jsonArr.size()>0){
							carNo = jsonArr.getJSONObject(0).getString("carCode");
						}
					}
				}
			} catch (Exception e) {
				logger.error("sendMessageToWeChart get plateNumber error,params is "+m.getTerminalId()+",cause by ", e);
			}
//			if("open".equals(ConfigurationManager.getProperty("eoc.startremind.product.switch","close"))
//					|| ("open".equals(ConfigurationManager.getProperty("eoc.startremind.push.switch","open")) && flag)) {
				Date remindDate = new Date(remindTime);
				String time = DateUtil.format(remindDate, "yyyy-MM-dd HH:mm:ss");

			Date gpsDate = new Date(gpsTime);
			String gpsTimeStr = DateUtil.format(gpsDate, "yyyy-MM-dd HH:mm:ss");

				String pushUrl = ConfigurationManager.getProperty("eoc.startremind.push.url");
				StringBuffer params = new StringBuffer();
				params.append("terminalId=");
				params.append(requestVO.getTerminalId());
				params.append("&lat=");
				params.append(lat);
				params.append("&lon=");
				params.append(lon);
				params.append("&publicId=");
				params.append(bind.getPublicId());
				params.append("&openId=");
				params.append(bind.getOpenId());
				
				
				try {
					if(!StringUtils.isEmpty(carNo)) {
						params.append("&carNo=");
						params.append(URLEncoder.encode(carNo,"utf-8"));
					}
					
					params.append("&alarmTime=");
					params.append(URLEncoder.encode(time,"utf-8"));
					params.append("&gpsTime=");
					params.append(URLEncoder.encode(gpsTimeStr,"utf-8"));
					String content = HttpUtil.sendGet(pushUrl, params.toString());
					logger.info( "startRemind success terminalId:" + m.getTerminalId() + "的设备,设防功能:通知微信 消息发送成功!weixin result:"+content);
					
					defance.setIsSuccess(Constant.DEFANCE_PUSH_SUCCESS);
					if(!StringUtils.isEmpty(content) && content.length()>200){
						defance.setMsg(content.substring(0, 200));
					}else{
						defance.setMsg(content);
					}
					//记录原地设防 发送信息到数据库
					defenceRecordMapper.insert(defance);
					//缓存中记录启动提醒消息
					redisTemple.opsForValue().set("FIRING_REMIND_"+m.getTerminalId(), 1+":"+System.currentTimeMillis());
				} catch (Exception e) {
					defance.setIsSuccess(Constant.DEFANCE_PUSH_FAIL);
					if(!StringUtils.isEmpty(e.getMessage()) && e.getMessage().length()>200){
						defance.setMsg(e.getMessage().substring(0, 200));
					}else{
						defance.setMsg(e.getMessage());
					}
					//记录原地设防 发送信息到数据库
					defenceRecordMapper.insert(defance);
					logger.error( "startRemind error terminalId:" + m.getTerminalId() + "的设备,设防功能:通知微信失败", e);
				}
//			}else {
//				try {
//					String content = sendMsgDirectToWechat(bind, m, requestVO, remindTime,carNo);
//					logger.info( "startRemind success terminalId:" + m.getTerminalId() + "的设备,设防功能:通知微信 消息发送成功!weixin result:"+content);
//					
//					defance.setIsSuccess(Constant.DEFANCE_PUSH_SUCCESS);
//					if(!StringUtils.isEmpty(content) && content.length()>200){
//						defance.setMsg(content.substring(0, 200));
//					}else{
//						defance.setMsg(content);
//					}
//					//记录原地设防 发送信息到数据库
//					defenceRecordMapper.insert(defance);
//					//缓存中记录启动提醒消息
//					redisTemple.opsForValue().set("FIRING_REMIND_"+m.getTerminalId(), 1+":"+System.currentTimeMillis());
//				} catch (Exception e) {
//					defance.setIsSuccess(Constant.DEFANCE_PUSH_FAIL);
//					if(!StringUtils.isEmpty(e.getMessage()) && e.getMessage().length()>200){
//						defance.setMsg(e.getMessage().substring(0, 200));
//					}else{
//						defance.setMsg(e.getMessage());
//					}
//					//记录原地设防 发送信息到数据库
//					defenceRecordMapper.insert(defance);
//					logger.error( "startRemind error terminalId:" + m.getTerminalId() + "的设备,设防功能:通知微信失败", e);
//				}
//			}
		}
	}
	
	public static void main(String[] args) throws UnsupportedEncodingException {
		StringBuffer params = new StringBuffer();
		params.append("terminalId=");
		params.append(87601);
		params.append("&lat=");
		params.append(22.517576);
		params.append("&lon=");
		params.append(110.208248);
		params.append("&publicId=");
		params.append("nvbvkx1446175997");
		params.append("&openId=");
		params.append("oYYxVs61SUQ07qZbxt1d0ogO8_SU");
		if(!StringUtils.isEmpty("粤B12323")) {
			params.append("&carNo=");
			params.append("粤B12323");
		}
		
		params.append("&alarmTime=");
		params.append(URLEncoder.encode("2019-05-20 14:59:11","utf-8"));
		String content = HttpUtil.sendGet("http://192.168.171.231:7033/bd-service/pushMessage/mobileReminder", params.toString());
		
		
		System.out.println("-------------------------------------------");
		System.out.println(content);
		System.out.println("-------------------------------------------");
	}
	
	/**
	 *  直接向微信发模板消息
	 * @param bind
	 * @param m
	 * @param requestVO
	 * @param remindTime
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	public String sendMsgDirectToWechat(WechatBind bind,MsgTerminal m,IgniteEventReqVO requestVO,Long remindTime,String carNo) throws Exception {
		String warnAddress = null;
		
		//2.解析启动地址信息
		try {
			warnAddress = AddressUtil.getBDAddressByGps(Double.valueOf(requestVO.getLat()), Double.valueOf(requestVO.getLon()),1);
		} catch (Exception e) {
			logger.error("sendMessageToWeChart get getAddressByGps error,params is "+m.getTerminalId()+",cause by ", e);
		}
		
		//3.调用微信推送消息
		String url = ConfigurationManager.getProperty("SEND_MESSAGE_TO_WECHAR_URL");
		StringBuffer paramBuf = new StringBuffer();
		paramBuf.append("g=home&m=Wxmq&openId=");
		paramBuf.append(bind.getOpenId().trim());
		paramBuf.append("&publicId=");
		paramBuf.append(bind.getPublicId().trim());
		if(!StringUtils.isEmpty(carNo)) {
			paramBuf.append("&carNo=");
			paramBuf.append(URLEncoder.encode(carNo,"utf-8"));
		}
		if(!StringUtils.isEmpty(warnAddress)) {
			paramBuf.append("&address=");
			paramBuf.append(URLEncoder.encode(warnAddress,"utf-8"));
		}
		paramBuf.append("&sign=");
		paramBuf.append(Md5Util.stringToMD5(bind.getOpenId().trim() + Constant.WECHAT_KEY));
		if(m.getDeviceIdType()!=null && m.getDeviceIdType().intValue()==0){
			//后视镜推后视镜消息模板
			paramBuf.append("&a=carAlarmNotice");
		}else if(m.getDeviceIdType()!=null && m.getDeviceIdType().intValue()==4){
			//大屏机推大屏机消息模板
			paramBuf.append("&a=dpjCarAlarmNotice");
		}else{
			//推其他消息模板
			paramBuf.append("&a=otherCarAlarmNotice");
		}
		String content = HttpUtil.sendGet(url, URLEncoder.encode(paramBuf.toString(),"utf-8"));
		return content;
	}

	public List<WechatBind> getWechatBind (String imei) {
		List<WechatBind> resultList = new ArrayList<>();
		if ("true".equals(ConfigurationManager.getProperty("USER-OPEN", "false"))) {
			String param = "deviceId=" + imei;
			String url = ConfigurationManager.getProperty("ECLOUD_WECHATBINDS_URL");
			String result = HttpUtil.sendGet(url, param, 5000);
			if(StringUtils.isEmpty(result)) {
				logger.error("RemindService | getWechatBind fail,param={}", param);
				return null;
			}
			JSONObject jsonObject = JSON.parseObject(result);
			if(jsonObject.containsKey("success") && "true".equals(jsonObject.getString("success"))) {
				List<JSONObject> list = JSON.parseObject(jsonObject.getString("data"), List.class);
				for (JSONObject object : list) {
					if (object == null) {
						continue;
					}
					WechatBind bind = new WechatBind();
					bind.setImei(object.getString("deviceId"));
					bind.setOpenId(object.getString("openId"));
					bind.setMobile(object.getString("mobile"));
					bind.setPublicId(object.getString("publicId"));
					bind.setCreateDate(object.getDate("createTime"));
					bind.setIsRegisterUser(object.getString("isOwner"));
					bind.setTerminalId(object.getInteger("terminalId"));
					resultList.add(bind);
				}
			}else {
				logger.error("RemindService | getWechatBind fail,param={},result={}", param, result);
			}
		} else {
			resultList = wechatBindMapper.selectByImei(imei);
		}
		return resultList;
	}
}
