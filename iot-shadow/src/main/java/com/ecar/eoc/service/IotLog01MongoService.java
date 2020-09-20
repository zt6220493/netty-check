package com.ecar.eoc.service;

import java.util.Date;
import java.util.List;

import com.ecar.eoc.entity.mongo.DeviceStatus;

public interface IotLog01MongoService {

    void save(DeviceStatus mongoDevice);
    
    void save(List<DeviceStatus> list,String collectionName);
    
    List<DeviceStatus> queryByTerminalIdAndTime(String terminalId, Date startTime, Date endTime);
}
