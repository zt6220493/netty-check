package com.ecar.eoc.action;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.ecar.eoc.common.BaseInfo;
import com.ecar.eoc.common.Constant;
import com.ecar.eoc.entity.vo.request.DistributeRequestVO;
import com.ecar.eoc.entity.vo.response.DistributeResVO;
import com.ecar.eoc.exception.BizException;
import com.ecar.eoc.utils.Md5Util;

/**
 * IOT数据分发action
 * @author pom
 *
 */
@RestController
@RequestMapping("/service/distribute")
public class DistributeRest {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	/**
	 * 
	 * @param requestVO
	 * @return
	 */
	@RequestMapping("/getDistInfo")
	public BaseInfo<Object> getDistInfo(DistributeRequestVO requestVO){
		String requestStr = JSON.toJSONString(requestVO);
		BaseInfo<Object> result = new BaseInfo<Object>();
		
		try {
			if(null==requestVO.getTerminalId() || (StringUtils.isEmpty(requestVO.getIccid()) && StringUtils.isEmpty(requestVO.getImei()))
					|| null==requestVO.getTimestamp() || StringUtils.isEmpty(requestVO.getSign())) {
				throw new BizException("330011", "参数不能为空");
			}
			if (!Md5Util.stringToMD5(requestVO.getTimestamp().longValue() + Constant.SERVER_KEY).equals(requestVO.getSign())){
				throw new BizException("102101", "非法访问!");
			}
			long startTime = System.currentTimeMillis();
			//do business
			//暂时不去实现
			DistributeResVO resVO = new DistributeResVO();
			resVO.setTerminalId(requestVO.getTerminalId());
			resVO.setDistValue("tpdist001");
			result.setData(resVO);
			long endTime = System.currentTimeMillis();
			logger.info("getDistInfo success,cost time "+(endTime-startTime)+" ms,requestparams:"+JSON.toJSONString(requestVO)
							+",responseparams:");
		} catch(BizException e1) {
			result.setSuccess("false");
			result.setErrorCode(e1.getCode());
			result.setErrorMessage(e1.getMessage());
			logger.error("getDistInfo error,request:{},result:{}",requestStr,e1);
		}catch (Exception e) {
			result.setSuccess("false");
			result.setErrorCode("500");
			result.setErrorMessage("服务器内部错误");
			logger.error("getDistInfo error,request:{},result:{}",requestStr,e);
		}
		return result;
	}
}
