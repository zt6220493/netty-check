package com.ecar.eoc.service;

import java.io.IOException;

import com.ecar.eoc.vo.request.DeviceLogReq;

public interface DeviceLogService {

    void saveDeviceLog(DeviceLogReq requestVO);

    String showDeviceLog(String terminalId, String deviceId, String sessionId) throws IOException;
}
