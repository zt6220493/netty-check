package com.ecar.eoc.mapper.simdb;

import org.apache.ibatis.annotations.Mapper;

import com.ecar.eoc.entity.eoc.SimInfo;

@Mapper
public interface SimMapper {
	public SimInfo findSimInfo(String iccid);
}
