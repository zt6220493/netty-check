///**
// * Project Name:eoc-sim
// * File Name:RemoteClient.java
// * Package Name:com.ecar.eoc.remote
// * Date:2018年7月27日下午2:51:33
// * Copyright (c) 2018, E-CAR All Rights Reserved.
// *
//*/
//
//package com.ecar.eoc.remote;
//
//import org.springframework.cloud.openfeign.FeignClient;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//
//import com.ecar.eoc.remote.hystric.RemoteClientHystric;
//
///**
// * ClassName:RemoteClient <br/>
// * Function: 远程调用其他微服务. <br/>
// * Date:     2018年7月27日 下午2:51:33 <br/>
// * @author   zhongying	 
// */
//@FeignClient(value="eoc-sim2",fallback=RemoteClientHystric.class)
//public interface RemoteClient {
//
//	@RequestMapping(method = RequestMethod.GET, value = "/test/page")
//    String test();
//
//}
//
