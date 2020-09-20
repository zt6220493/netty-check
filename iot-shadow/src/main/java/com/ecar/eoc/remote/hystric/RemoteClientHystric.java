///**
// * Project Name:eoc-sim
// * File Name:RemoteClientHystric.java
// * Package Name:com.ecar.eoc.remote.hystric
// * Date:2018年8月15日上午11:00:03
// * Copyright (c) 2018, E-CAR All Rights Reserved.
// *
//*/
//
//package com.ecar.eoc.remote.hystric;
//
//import org.springframework.stereotype.Component;
//
//import com.ecar.eoc.remote.RemoteClient;
//
///**
// * ClassName:RemoteClientHystric <br/>
// * Function: 跨服务调用的熔断器实现. <br/>
// * Date:     2018年8月15日 上午11:00:03 <br/>
// * @author   zhongying	 
// */
//@Component
//public class RemoteClientHystric implements RemoteClient {
//
//	@Override
//	public String test() {
//
//		//如果远程服务无法访问，就会进行熔断，执行这里的方法
//		return "hello hystric";
//	}
//
//}
//
