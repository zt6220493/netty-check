/**
 * Project Name:eoc-sim
 * File Name:UserServiceImpl.java
 * Package Name:com.ecar.eoc.service.impl
 * Date:2018年6月12日下午2:30:22
 * Copyright (c) 2018, E-CAR All Rights Reserved.
 *
*/

package com.ecar.eoc.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.ecar.eoc.entity.user.User;
import com.ecar.eoc.mapper.eocdb.user.UserMapper;
import com.ecar.eoc.service.UserService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
 * ClassName:UserServiceImpl <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Date:     2018年6月12日 下午2:30:22 <br/>
 * @author   zhongying	 
 */
@Service
public class UserServiceImpl implements UserService {
	
	private Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private UserMapper userMapper;
	
	@Override
	public PageInfo<User> getPageInfo() {
		PageHelper.startPage(0, 20);
		PageInfo<User> user = new PageInfo<User>(userMapper.getAll());
		log.info("user:{}",user);
		return user;
	}

	@Override
	public Page<User> getPage() {
		PageHelper.startPage(0, 20);
		Page<User> list = (Page<User>)userMapper.getAll();
		log.info("pages:{},total:{}",list.getPages(),list.getTotal());
		log.info("list:{}",JSONObject.toJSON(list));
		return list;
	}

}

