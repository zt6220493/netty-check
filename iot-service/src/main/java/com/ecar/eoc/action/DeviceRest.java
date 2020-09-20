package com.ecar.eoc.action;

import com.ecar.eoc.entity.vo.request.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.ecar.eoc.common.BaseInfo;
import com.ecar.eoc.exception.BizException;
import com.ecar.eoc.service.RemindService;
import com.ecar.eoc.service.TerminalService;
import com.ecar.eoc.service.TripService;
import com.ecar.eoc.utils.CheckIpValid;
import javax.servlet.http.HttpServletRequest;

/**
 * 终端设备类
 * @author pom
 *
 */
@RestController
@RequestMapping("/device")
public class DeviceRest {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private TerminalService terminalService;
	@Autowired
	private TripService tripService;
	@Autowired
	private RemindService remindService;
	
	/**
	 * 3.2.3 终端IOT配置信息接口
	 * @param requestVO
	 * @return
	 */
	@RequestMapping("/conf/common")
	public BaseInfo<Object> commonConf(ConfReqVO requestVO){
		String requestStr = JSON.toJSONString(requestVO);
		BaseInfo<Object> result = new BaseInfo<Object>();
		
		try {
			if(null==requestVO.getTerminalId() || StringUtils.isEmpty(requestVO.getTimestamp())
					|| StringUtils.isEmpty(requestVO.getSign())) {
				throw new BizException("330011", "参数不能为空");
			}
			result = terminalService.commonConf(requestVO);
			logger.info("commonConf success,request:"+JSON.toJSONString(requestVO)
							+",response:"+JSON.toJSONString(result));
		} catch(BizException e1) {
			result.setSuccess("false");
			result.setErrorCode(e1.getCode());
			result.setErrorMessage(e1.getMessage());
			logger.error("commonConf error,request:{},result:{}",requestStr,e1);
		}catch (Exception e) {
			result.setSuccess("false");
			result.setErrorCode("500");
			result.setErrorMessage("服务器内部错误");
			logger.error("commonConf error,request:{},result:{}",requestStr,e);
		}
		return result;
	}
	
	/**
	 * 3.2.4 终端离线数据上传
	 * @param requestVO
	 * @return
	 */
	@RequestMapping("/upload/offline/gps")
	public BaseInfo<Object> uploadOffGps(UploadOffGpsReqVO requestVO){
		String requestStr = JSON.toJSONString(requestVO);
		BaseInfo<Object> result = new BaseInfo<Object>();
		
		try {
			if(null==requestVO.getTerminalId() || (StringUtils.isEmpty(requestVO.getUrl()) && StringUtils.isEmpty(requestVO.getErrorCode()))
					|| StringUtils.isEmpty(requestVO.getSign())) {
				throw new BizException("330011", "参数不能为空");
			}
			long startTime = System.currentTimeMillis();
			//do business
			result = tripService.uploadOffGps(requestVO);
			long endTime = System.currentTimeMillis();
			logger.info("uploadOffGps success,cost time "+(endTime-startTime)+" ms,requestparams:"+JSON.toJSONString(requestVO)
							+",responseparams:");
		} catch(BizException e1) {
			result.setSuccess("false");
			result.setErrorCode(e1.getCode());
			result.setErrorMessage(e1.getMessage());
			logger.error("uploadOffGps error,request:{},result:{}",requestStr,e1);
		}catch (Exception e) {
			result.setSuccess("false");
			result.setErrorCode("500");
			result.setErrorMessage("服务器内部错误");
			logger.error("uploadOffGps error,request:{},result:{}",requestStr,e);
		}
		return result;
	}
	
	/**
	 * 3.2.5 终端点火事件上传
	 * @param requestVO
	 * @return
	 */
	@RequestMapping("/upload/ignite")
public BaseInfo<Object> ignite(IgniteEventReqVO requestVO,HttpServletRequest request){
		String requestStr = JSON.toJSONString(requestVO);
		BaseInfo<Object> result = new BaseInfo<Object>();
		try {
			String ip =new CheckIpValid().getIpAddr2(request);
			logger.info("------->>>IP【" +ip +"】"+requestStr);
			if(null==requestVO.getTerminalId() || null==requestVO.getAcc() || null==requestVO.getType()
					|| StringUtils.isEmpty(requestVO.getSign())) {
				throw new BizException("330011", "参数不能为空");
			}
            if (requestVO == null || StringUtils.isEmpty(requestVO.getLat()) || StringUtils.isEmpty(requestVO.getLat())
                    || "0.0".equals(requestVO.getLat()) || "0.0".equals(requestVO.getLon())) {
                throw new BizException("330011", "经纬度不能为空");
            }
			long startTime = System.currentTimeMillis();
			remindService.startRemind(requestVO);
			long endTime = System.currentTimeMillis();
			logger.info("IP【" +ip +"】，ignite success,cost time "+(endTime-startTime)+" ms,requestparams:"+JSON.toJSONString(requestVO)
							+",responseparams:");
		} catch(BizException e1) {
			result.setSuccess("false");
			result.setErrorCode(e1.getCode());
			result.setErrorMessage(e1.getMessage());
			logger.error("ignite error,request:{},result:{}",requestStr,e1);
		}catch (Exception e) {
			result.setSuccess("false");
			result.setErrorCode("500");
			result.setErrorMessage("服务器内部错误");
			logger.error("ignite error,request:{},result:{}",requestStr,e);
		}
		return result;
	}
	
	/**
	 * 外部系统与服务端交互gps同步至IOT
	 * @param requestVO
	 * @return
	 */
	@RequestMapping("/gpsLocation/sync")
	public BaseInfo<Object> locationSync(LocationSyncReqVO requestVO){
		String requestStr = JSON.toJSONString(requestVO);
		BaseInfo<Object> result = new BaseInfo<Object>();
		
		try {
			if(null==requestVO.getTerminalId() || null==requestVO.getType()
					|| StringUtils.isEmpty(requestVO.getIccid())
					|| StringUtils.isEmpty(requestVO.getImei())) {
				throw new BizException("330011", "参数不能为空");
			}
			long startTime = System.currentTimeMillis();
			result = terminalService.locationSync(requestVO);
			long endTime = System.currentTimeMillis();
			logger.info("locationSync success,cost time "+(endTime-startTime)+" ms,requestparams:"+JSON.toJSONString(requestVO)
							+",response:"+JSON.toJSONString(result));
		} catch(BizException e1) {
			result.setSuccess("false");
			result.setErrorCode(e1.getCode());
			result.setErrorMessage(e1.getMessage());
			logger.error("locationSync error,request:{},response:{},cause by:{}",requestStr,JSON.toJSONString(result),e1);
		}catch (Exception e) {
			result.setSuccess("false");
			result.setErrorCode("500");
			result.setErrorMessage("服务器内部错误");
			logger.error("locationSync error,request:{},response:{},cause by:{}",requestStr,JSON.toJSONString(result),e);
		}
		return result;
	}

	/**
	 * 第三方gps同步至IOT
	 * @param requestVO
	 * @return
	 */
	@RequestMapping("/gps/sync")
	public BaseInfo<Object> thirdGpsSync(@RequestBody ThirdGpsReqVO requestVO){
		String requestStr = JSON.toJSONString(requestVO);
		BaseInfo<Object> result = new BaseInfo<Object>();

		try {
			if(null == requestVO || StringUtils.isEmpty(requestVO.getImei()) || requestVO.getGps().isEmpty()) {
				throw new BizException("330011", "参数不能为空");
			}
			long startTime = System.currentTimeMillis();
			result = terminalService.thirdGpsSync(requestVO);
			long endTime = System.currentTimeMillis();
			logger.info("thirdGpsSync success,cost time "+(endTime-startTime)+" ms,requestparams:"+JSON.toJSONString(requestVO)
					+",response:"+JSON.toJSONString(result));
		} catch(BizException e1) {
			result.setSuccess("false");
			result.setErrorCode(e1.getCode());
			result.setErrorMessage(e1.getMessage());
			logger.error("thirdGpsSync error,request:{},response:{},cause by:{}",requestStr,JSON.toJSONString(result),e1);
		}catch (Exception e) {
			result.setSuccess("false");
			result.setErrorCode("500");
			result.setErrorMessage("服务器内部错误");
			logger.error("thirdGpsSync error,request:{},response:{},cause by:{}",requestStr,JSON.toJSONString(result),e);
		}
		return result;
	}

    /**
     * 围栏信息同步至IOT（默认接收的坐标系为wgs84）
     * @param requestVO
     * @param coordType 坐标系类型：默认wgs84 wgs84：GPS 坐标 bd_map：百度坐标系 amap 高德坐标系(谷歌国内使用的是火星坐标系，跟高德相同)
     * @return
     */
    @RequestMapping("/fence/sync")
    public BaseInfo<Object> fenceSync(FenceReqVO requestVO, String coordType){
        String requestStr = JSON.toJSONString(requestVO);
        BaseInfo<Object> result = new BaseInfo<>();

        try {
            if(null == requestVO || requestVO.getTerminalId() == null || requestVO.getId() == null) {
                throw new BizException("330011", "参数不能为空");
            }
            long startTime = System.currentTimeMillis();
            terminalService.fenceSync(requestVO,coordType);
            long endTime = System.currentTimeMillis();
            result.setSuccess("true");
            logger.info("fenceSync success,cost time "+(endTime-startTime)+" ms,requestparams:"+JSON.toJSONString(requestVO)
                    +",response:"+JSON.toJSONString(result));
        } catch(BizException e1) {
            result.setSuccess("false");
            result.setErrorCode(e1.getCode());
            result.setErrorMessage(e1.getMessage());
            logger.error("fenceSync error,request:{},response:{},cause by:{}",requestStr,JSON.toJSONString(result),e1);
        }catch (Exception e) {
            result.setSuccess("false");
            result.setErrorCode("500");
            result.setErrorMessage("服务器内部错误");
            logger.error("fenceSync error,request:{},response:{},cause by:{}",requestStr,JSON.toJSONString(result),e);
        }
        return result;
    }

    /**
     * 删除设备围栏信息
     * @return
     */
    @RequestMapping("/fence/delete")
    public BaseInfo<Object> fenceDelete(Integer terminalId, Integer fenceId){
        BaseInfo<Object> result = new BaseInfo<Object>();

        try {
            if(null == terminalId) {
                throw new BizException("330011", "参数不能为空");
            }
            long startTime = System.currentTimeMillis();
            terminalService.fenceDelete(terminalId, fenceId);
            long endTime = System.currentTimeMillis();
            result.setSuccess("true");
            logger.info("fenceDelete success,cost time "+(endTime-startTime)+" ms,requestparams:"+terminalId
                    +",response:"+JSON.toJSONString(result));
        } catch(BizException e1) {
            result.setSuccess("false");
            result.setErrorCode(e1.getCode());
            result.setErrorMessage(e1.getMessage());
            logger.error("fenceDelete error,request:{},response:{},cause by:{}",terminalId,JSON.toJSONString(result),e1);
        }catch (Exception e) {
            result.setSuccess("false");
            result.setErrorCode("500");
            result.setErrorMessage("服务器内部错误");
            logger.error("fenceDelete error,request:{},response:{},cause by:{}",terminalId,JSON.toJSONString(result),e);
        }
        return result;
    }
}
