package com.ecar.eoc.mapper.eocdb.eoc;

import org.apache.ibatis.annotations.Mapper;

import com.ecar.eoc.entity.eoc.MsgTerminal;

@Mapper
public interface TerminalMapper {
	public MsgTerminal getLastMsgTerminal(MsgTerminal msgTerminal);
}
