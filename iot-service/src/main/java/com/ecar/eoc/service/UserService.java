/**
 * Project Name:eoc-sim
 * File Name:UserService.java
 * Package Name:com.ecar.eoc.service
 * Date:2018年6月12日上午11:24:00
 * Copyright (c) 2018, E-CAR All Rights Reserved.
 *
*/

package com.ecar.eoc.service;

import com.ecar.eoc.entity.user.User;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

/**
 * ClassName:UserService <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Date:     2018年6月12日 上午11:24:00 <br/>
 * @author   zhongying	 
 */
public interface UserService {
	
	/**
	 * 分页查询示例,获取分页数据和分页属性
	 * getPageInfo:(这里用一句话描述这个方法的作用). <br/>
	 * TODO(这里描述这个方法适用条件 – 可选).<br/>
	 * TODO(这里描述这个方法的执行流程 – 可选).<br/>
	 * TODO(这里描述这个方法的使用方法 – 可选).<br/>
	 * TODO(这里描述这个方法的注意事项 – 可选).<br/>
	 *
	 * @author zhongying
	 * @return
	 */
	public PageInfo<User> getPageInfo();
	
	/**
	 * 分页查询示例，获取分页对象
	 * getPage:(这里用一句话描述这个方法的作用). <br/>
	 * TODO(这里描述这个方法适用条件 – 可选).<br/>
	 * TODO(这里描述这个方法的执行流程 – 可选).<br/>
	 * TODO(这里描述这个方法的使用方法 – 可选).<br/>
	 * TODO(这里描述这个方法的注意事项 – 可选).<br/>
	 *
	 * @author zhongying
	 * @return
	 */
	public Page<User> getPage();
	

}

