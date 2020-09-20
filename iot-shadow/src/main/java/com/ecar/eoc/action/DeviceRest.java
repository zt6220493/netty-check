package com.ecar.eoc.action;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import com.alibaba.fastjson.JSON;
import com.ecar.eoc.common.BaseInfo;
import com.ecar.eoc.entity.mongo.DeviceStatus;
import com.ecar.eoc.exception.BizException;
import com.ecar.eoc.service.DeviceLogService;
import com.ecar.eoc.service.DeviceService;
import com.ecar.eoc.service.IotLog01MongoService;
import com.ecar.eoc.vo.request.DeviceLogReq;

@RestController
@RequestMapping("/device")
public class DeviceRest {
	private Logger log = LoggerFactory.getLogger(this.getClass());

	@Resource
	private DeviceLogService deviceLogService;
	@Resource
	private DeviceService deviceService;

	@Resource
    private IotLog01MongoService iotLog01MongoService;

    /**
     * 条件查询，返回 zip 文件内容
     * @param terminalId
     * @param deviceId
     * @param sessionId
     * @return
     */
    @RequestMapping("/showDeviceLog")
    public BaseInfo<Object> showDeviceLog(String terminalId, String deviceId, String sessionId) {
        BaseInfo<Object> result = new BaseInfo<Object>();
        try {
            if (terminalId == null || terminalId.equals("")) {
                log.error("showDeviceLog error, param is {}", terminalId);
                throw new BizException(110020, "参数不能为空");
            }
            String data = deviceLogService.showDeviceLog(terminalId, deviceId, sessionId);
            result.setData(data);
        } catch(BizException e1) {
            result.setErrorCode(e1.getCode() + "");
            result.setErrorMessage(e1.getMessage());
            log.error("DeviceRest uploadLog error,request:{},cause by:{}", terminalId, e1);
        } catch (Exception ex) {
            result.setErrorCode("500");
            result.setErrorMessage("服务器内部异常");
            log.error("DeviceRest uploadLog error,request:{},cause by:{}",terminalId, ex);
        }
        return result;
    }

    /**
     * 根据设备 id 查询一段时间的连接状态数据
     * @param terminalId
     * @param startTime
     * @param endTime
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getConnectionStatus", method = {RequestMethod.POST,RequestMethod.GET})
    public BaseInfo<Object> queryByTerminalIdAndTime(String terminalId, Long startTime, Long endTime) {
        BaseInfo<Object> result = new BaseInfo<Object>();
        try {
            if (terminalId == null || terminalId.equals("")) {
                log.error("showDeviceLog error, param is {}, {}, {}", terminalId, startTime, endTime);
                throw new BizException(110020, "参数不能为空");
            }
            Date start = new Date(startTime);
            Date end = new Date(endTime);
            List<DeviceStatus> data = iotLog01MongoService.queryByTerminalIdAndTime(terminalId, start, end);
            result.setData(data);
        } catch(BizException e1) {
            result.setErrorCode(e1.getCode() + "");
            result.setErrorMessage(e1.getMessage());
            log.error("DeviceRest uploadLog error,request:{},cause by:{}", terminalId, e1);
        } catch (Exception ex) {
            result.setErrorCode("500");
            result.setErrorMessage("服务器内部异常");
            log.error("DeviceRest uploadLog error,request:{},cause by:{}",terminalId, ex);
        }
        return result;
    }

    /**
     * 文件上传
     * @param requestVO
     * @return
     */
	@RequestMapping("/log/upload")
	public BaseInfo<Object> uploadLog(DeviceLogReq requestVO){
		String requestStr = JSON.toJSONString(requestVO);
		BaseInfo<Object> result = new BaseInfo<Object>();
		try {
			if(null==requestVO || null==requestVO.getTerminalId()) {
				log.error("saveDeviceLog error,params is {}",JSON.toJSONString(requestVO));
				throw new BizException(110020, "参数不能为空");
			}

			deviceLogService.saveDeviceLog(requestVO);
			log.info("DeviceRest uploadLog success,request:{},response:{}",requestStr,"");
		} catch(BizException e1) {
			result.setErrorCode(e1.getCode()+"");
			result.setErrorMessage(e1.getMessage());
			log.error("DeviceRest uploadLog error,request:{},cause by:{}",requestStr,e1);
		} catch (Exception e) {
			result.setErrorCode("500");
			result.setErrorMessage("服务器内部异常");
			log.error("DeviceRest uploadLog error,request:{},cause by:{}",requestStr,e);
		}
		return result;
	}
	
	/**
	 * 控制终端位置上传
	 * @param requestVO
	 * @return
	 */
	@RequestMapping("/control/gps/upload")
	public BaseInfo<Object> conTrolUploadLog(Integer terminalId){
		String requestStr = JSON.toJSONString(terminalId);
		BaseInfo<Object> result = new BaseInfo<Object>();
		try {
			if(null==terminalId) {
				log.error("saveDeviceLog error,params is {}",JSON.toJSONString(terminalId));
				throw new BizException(110020, "参数不能为空");
			}

			result = deviceService.conTrolUploadGps(terminalId);
			log.info("DeviceRest conTrolUploadLog success,request:{},response:{}",requestStr,"");
			return result;
		} catch(BizException e1) {
			result.setErrorCode(e1.getCode()+"");
			result.setErrorMessage(e1.getMessage());
			log.error("DeviceRest conTrolUploadLog error,request:{},cause by:{}",requestStr,e1);
		} catch (Exception e) {
			result.setErrorCode("500");
			result.setErrorMessage("服务器内部异常");
			log.error("DeviceRest conTrolUploadLog error,request:{},cause by:{}",requestStr,e);
		}
		return result;
	}
}
