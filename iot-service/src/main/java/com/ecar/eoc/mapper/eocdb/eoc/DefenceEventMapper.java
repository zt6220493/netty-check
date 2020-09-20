package com.ecar.eoc.mapper.eocdb.eoc;

import org.apache.ibatis.annotations.Mapper;

import com.ecar.eoc.entity.eoc.DefenceEvent;

@Mapper
public interface DefenceEventMapper {
	public void insert(DefenceEvent defenceEvent);
}
