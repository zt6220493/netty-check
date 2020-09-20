package com.ecar.eoc.mapper.eocdb.eoc;

import java.util.Date;

import org.apache.ibatis.annotations.Mapper;

import com.ecar.eoc.entity.eoc.EocTripSpy;

@Mapper
public interface TripMapper {
	EocTripSpy getMaxMileageTrip(Integer terminalId,Date startTime,Date endTime,String tableName);

	/**
	 * 获取最新的行程信息
	 * @param terminalId
	 * @return
	 */
	EocTripSpy getNewestTrip(Integer terminalId, String tableName);

    Double getTotalMileage(Integer terminalId, Date startTime, Date endTime, String tableName);
}
