package com.ecar.eoc.service.Impl;

import com.ecar.eoc.entity.log.DeviceLogFile;
import com.ecar.eoc.mapper.DeviceLogMapper;
import com.ecar.eoc.service.DeviceLogService;
import com.ecar.eoc.util.HttpUtil;
import com.ecar.eoc.vo.request.DeviceLogReq;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import com.ecar.eoc.common.DownloadUrl;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.io.IOException;


/**
 * Description: 设备日志操作服务
 * Date:        2019年3月5日 下午2:44:14
 * @author      chenjiahao
 */
@Service
public class DeviceLogServiceImpl implements DeviceLogService {

    @Resource
    private DeviceLogMapper deviceLogMapper;
    @Autowired
    private Environment env;	//配置文件

    /**
     * 保存设备终端日志
     * @param requestVO
     */
    public void saveDeviceLog(DeviceLogReq requestVO) {
        DeviceLogFile deviceLogFile = new DeviceLogFile();
        deviceLogFile.setTerminalId(requestVO.getTerminalId());
        deviceLogFile.setDeviceId(requestVO.getImei());
        deviceLogFile.setSessionId(requestVO.getSessionId());
        deviceLogFile.setUrl(requestVO.getUrls());
        deviceLogFile.setCreateTime(new Date());
        deviceLogMapper.insert(deviceLogFile);
        
        //将数据同步给TSP那边
        try {
			String url = env.getProperty("ecar.iot.tsp_deviceLog_sync_url");
			StringBuffer param  = new StringBuffer();
			param.append("sessionId="+requestVO.getSessionId());
			param.append("&terminalId="+requestVO.getTerminalId());
			param.append("&fileUrl="+requestVO.getUrls());
			HttpUtil.sendGet(url, param.toString());
		} catch (Exception e) {
			//do nothing
		}
    }

    /**
     * 查询设备日志
     * @param terminalId
     * @param deviceId
     * @param sessionId
     * @return
     */
    public List<DeviceLogFile> queryDeviceLog(String terminalId, String deviceId, String sessionId) {
        List<DeviceLogFile> deviceLogServiceList = null;
        deviceLogServiceList = deviceLogMapper.queryDeviceLogByCondition(terminalId, deviceId, sessionId);
        return deviceLogServiceList;
    }

    /**
     * 条件查询，返回 zip 文件内容
     *
     * @param terminalId
     * @param deviceId
     * @param sessionId
     * @return
     */
    public String showDeviceLog(String terminalId, String deviceId, String sessionId) throws IOException {
        List<DeviceLogFile> deviceLogServiceList = null;
        deviceLogServiceList = queryDeviceLog(terminalId, deviceId, sessionId);

        if (deviceLogServiceList == null) {
            return "查询设备日志为空";
        }

        StringBuffer result = new StringBuffer("");
        DownloadUrl downloadUrl = new DownloadUrl();
        // 获取 zip 文件的内容，多个文件进行拼接
        for (DeviceLogFile deviceLogFile : deviceLogServiceList) {
            result.append(downloadUrl.download(deviceLogFile.getUrl()));
        }
        return result.toString();
    }
}
