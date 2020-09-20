package com.ecar.eoc.mapper.eocdb.eoc;

import org.apache.ibatis.annotations.Mapper;
import com.ecar.eoc.entity.eoc.DefenceList;

@Mapper
public interface DefenceListMapper {
	public DefenceList selectByPk(Integer id);
}
