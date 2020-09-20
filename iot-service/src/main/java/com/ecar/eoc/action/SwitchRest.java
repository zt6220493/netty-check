package com.ecar.eoc.action;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.ecar.eoc.common.BaseInfo;
import com.ecar.eoc.common.Constant;
import com.ecar.eoc.entity.vo.request.CommonReqVO;
import com.ecar.eoc.entity.vo.request.PriventThieveryReqVO;
import com.ecar.eoc.entity.vo.response.PriventThieveryResVO;
import com.ecar.eoc.exception.BizException;
import com.ecar.eoc.service.TerminalService;
import com.ecar.eoc.utils.Md5Util;

/**
 * 开关控制类逻辑
 * @author pom
 */
@RestController
@RequestMapping("/wechat/switch")
public class SwitchRest {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private TerminalService terminalService;
	
	/**
	 * 设防与撤防 开关
	 * @param requestVO
	 * @return
	 */
	@RequestMapping("/priventThievery")
	public BaseInfo<PriventThieveryResVO> priventThievery(PriventThieveryReqVO requestVO){
		String requestStr = JSON.toJSONString(requestVO);
		BaseInfo<PriventThieveryResVO> result = new BaseInfo<PriventThieveryResVO>();
		try {
			if(requestVO==null) {
				throw new BizException("330011", "参数不能为空");
			}
			if(StringUtils.isEmpty(requestVO.getImei()) || null==requestVO.getStatus()
					|| StringUtils.isEmpty(requestVO.getPublicId())
					|| StringUtils.isEmpty(requestVO.getSign())) {
				throw new BizException("330011", "参数不能为空");
			}
			if(requestVO.getStatus().intValue()<0 ||
					requestVO.getStatus().intValue()>2) {
				throw new BizException("330012", "参数不合法");
			}
			if (!Md5Util.stringToMD5(requestVO.getOpenId() + Constant.WECHAT_KEY).equals(requestVO.getSign())){
				throw new BizException("102101", "非法访问!");
			}
			result =  terminalService.priventThievery(requestVO);
			logger.info("priventThievery success,request:{},result:{}",requestStr,JSON.toJSONString(result));
		}catch(BizException e1) {
			result.setSuccess("false");
			result.setErrorCode(e1.getCode());
			result.setErrorMessage(e1.getMessage());
			logger.error("priventThievery error,request:{},result:{}",requestStr,e1);
		}catch (Exception e) {
			result.setSuccess("false");
			result.setErrorCode("500");
			result.setErrorMessage("服务器内部错误");
			logger.error("priventThievery error,request:{},result:{}",requestStr,e);
		}
		return result;
	}
	
	/**
	 * 原地设防状态查询
	 * @return
	 */
	@RequestMapping("/priventThieveryStatus")
	public BaseInfo<PriventThieveryResVO> priventThieveryStatus(CommonReqVO requestVO) {
		String requestStr = JSON.toJSONString(requestVO);
		BaseInfo<PriventThieveryResVO> result = new BaseInfo<PriventThieveryResVO>();
		try {
			if(requestVO==null) {
				throw new BizException("330011", "参数不能为空");
			}
			if(StringUtils.isEmpty(requestVO.getImei())
					|| StringUtils.isEmpty(requestVO.getPublicId())
					|| StringUtils.isEmpty(requestVO.getSign())) {
				throw new BizException("330011", "参数不能为空");
			}
			if (!Md5Util.stringToMD5(requestVO.getOpenId() + Constant.WECHAT_KEY).equals(requestVO.getSign())){
				throw new BizException("102101", "非法访问!");
			}
			result =  terminalService.priventThieveryStatus(requestVO);
			logger.info("priventThieveryStatus success,request:{},result:{}",requestStr,JSON.toJSONString(result));
		}catch(BizException e1) {
			result.setSuccess("false");
			result.setErrorCode(e1.getCode());
			result.setErrorMessage(e1.getMessage());
			logger.error("priventThieveryStatus error,request:{},result:{}",requestStr,e1);
		}catch (Exception e) {
			result.setSuccess("false");
			result.setErrorCode("500");
			result.setErrorMessage("服务器内部错误");
			logger.error("priventThieveryStatus error,request:{},result:{}",requestStr,e);
		}
		return result;
	}
}
