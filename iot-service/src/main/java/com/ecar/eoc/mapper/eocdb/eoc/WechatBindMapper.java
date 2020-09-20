package com.ecar.eoc.mapper.eocdb.eoc;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.ecar.eoc.entity.eoc.WechatBind;

@Mapper
public interface WechatBindMapper {
	public List<WechatBind> selectByImei(String imei);
}
