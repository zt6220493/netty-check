package com.ecar.eoc.service;

import java.util.Date;

import com.ecar.eoc.exception.BizException;
import com.ecar.eoc.mapper.eocdb.eoc.TripMapper;
import com.ecar.eoc.utils.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.ecar.eoc.common.BaseInfo;
import com.ecar.eoc.entity.iot.OffLineGpsFailRecord;
import com.ecar.eoc.entity.iot.OffLineGpsRecord;
import com.ecar.eoc.entity.vo.request.UploadOffGpsReqVO;
import com.ecar.eoc.mapper.iotdb.OffLineGpsMapper;

@Service
public class TripService {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private OffLineGpsMapper offLineGpsMapper;
	@Autowired
    private TripMapper tripMapper;

	/**
	 * 终端离线数据上传
	 * 处理逻辑，只生成新的行程，不做行程合并的工作，该工作由另外的程序来做
	 * @param requestVO
	 * @return
	 */
	public BaseInfo<Object> uploadOffGps(UploadOffGpsReqVO requestVO){
		BaseInfo<Object> result = new BaseInfo<Object>();
		//既不能判定是正确上传信息，又不能判定为失败上传信息，不予处理
		if(StringUtils.isEmpty(requestVO.getUrl()) && StringUtils.isEmpty(requestVO.getErrorCode())) {
			logger.warn("uploadOffGps request data error.can not judge success or fail,params:"+JSON.toJSONString(requestVO));
			return result;
		}else if(StringUtils.isEmpty(requestVO.getUrl())) {
			//记录到离线gps上传失败记录表中
			OffLineGpsFailRecord failRecord = new OffLineGpsFailRecord();
			failRecord.setTerminalId(requestVO.getTerminalId());
			failRecord.setIccid(requestVO.getIccid());
			failRecord.setImei(requestVO.getImei());
			failRecord.setErrorCode(requestVO.getErrorCode());
			failRecord.setErrorMsg(requestVO.getErrorMsg());
			failRecord.setBkName(requestVO.getBkName());
			failRecord.setRegion(requestVO.getRegion());
			failRecord.setCreateTime(new Date());
			offLineGpsMapper.insertFailRecord(failRecord);
			return result;
		}
		//上传离线数据成功，暂时先存入离线数据记录表（此工程，不予处理离线数据轨迹计算）
		OffLineGpsRecord record = new OffLineGpsRecord();
		record.setTerminalId(requestVO.getTerminalId());
		record.setIccid(requestVO.getIccid());
		record.setImei(requestVO.getImei());
		record.setUrl(requestVO.getUrl());
		record.setBkName(requestVO.getBkName());
		record.setRegion(requestVO.getRegion());
		record.setStatus(0); 	//未处理 离线数据处理状态
		record.setCreateTime(new Date());
		offLineGpsMapper.insertRecord(record);
		return result;
	}

    /**
     * 获取用户总里程（取一年内的数据）
     * @param terminalId
     * @param startTime
     * @param endTime
     * @return
     */
    public BaseInfo<Object> getMileage(Integer terminalId, String startTime, String endTime){
        BaseInfo<Object> result = new BaseInfo<Object>();
        try {
            Date startDate = DateUtil.getCurrYearFirst();
            Date endDate = DateUtil.dateCompletion(DateUtil.getCurrYearLast());
            if (!StringUtils.isEmpty(startTime)) {
                startDate = DateUtil.toDate(startTime,"yyyy-MM-dd HH:mm:ss");
            }
            if (!StringUtils.isEmpty(endTime)) {
                endDate = DateUtil.toDate(endTime,"yyyy-MM-dd HH:mm:ss");
            }
            String tableName = "EOC_MESSAGE_TRIP_"+(terminalId%16);
            Double mileage = tripMapper.getTotalMileage(terminalId,startDate,endDate,tableName);
            mileage = mileage == null ? 0 : mileage;
            result.setData(mileage);
        } catch (Exception e) {
            logger.error("TripService getMileage error, params:{},cause:{}",terminalId,e);
            throw new BizException("获取数据失败");
        }
        return result;
    }
}
