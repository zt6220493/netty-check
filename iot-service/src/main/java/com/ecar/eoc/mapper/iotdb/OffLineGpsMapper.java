package com.ecar.eoc.mapper.iotdb;

import org.apache.ibatis.annotations.Mapper;

import com.ecar.eoc.entity.iot.OffLineGpsFailRecord;
import com.ecar.eoc.entity.iot.OffLineGpsRecord;

@Mapper
public interface OffLineGpsMapper {
	public void insertRecord(OffLineGpsRecord offLineGpsRecord);
	
	public void insertFailRecord(OffLineGpsFailRecord offLineGpsFailRecord);
	
	
}
