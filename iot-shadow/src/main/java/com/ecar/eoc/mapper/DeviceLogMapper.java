package com.ecar.eoc.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.ecar.eoc.entity.log.DeviceLogFile;


/**
 * Description: 设备日志 dao 接口
 * Date:        2019年3月5日 下午2:44:34
 * @author      chenjiahao
 */
@Mapper
public interface DeviceLogMapper {
	public void insert(DeviceLogFile deviceLogFile);

	List<DeviceLogFile> queryDeviceLogByCondition(@Param("terminalId") String terminalId,
                                                 @Param("deviceId") String deviceId,
                                                 @Param("sessionId") String sessionId);
}
