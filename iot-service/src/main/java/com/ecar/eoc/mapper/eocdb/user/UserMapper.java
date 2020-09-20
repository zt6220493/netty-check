/**
 * Project Name:eoc-sim
 * File Name:UserMapper.java
 * Package Name:com.ecar.eoc.mapper.user
 * Date:2018年5月31日下午2:29:39
 * Copyright (c) 2018, E-CAR All Rights Reserved.
 *
*/

package com.ecar.eoc.mapper.eocdb.user;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.ecar.eoc.entity.user.User;

/**
 * ClassName:UserMapper <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Date:     2018年5月31日 下午2:29:39 <br/>
 * @author   zhongying	 
 */
@Mapper
public interface UserMapper {
	public List<User> getAll();
}

