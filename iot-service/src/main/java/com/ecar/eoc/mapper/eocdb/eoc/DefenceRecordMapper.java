package com.ecar.eoc.mapper.eocdb.eoc;

import org.apache.ibatis.annotations.Mapper;
import com.ecar.eoc.entity.eoc.DefanceRecord;

@Mapper
public interface DefenceRecordMapper {
	public void insert(DefanceRecord defanceRecord);
}
