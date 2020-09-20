package com.ecar.eoc.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;


/**
 * 
*类描述：检查IP的有效性（IP过滤）
*@author: jinqiu.chen
*@date: Dec 29, 2015 3:09:48 PM
*@version 1.0
 */
public class CheckIpValid {
	private  Logger logger = LoggerFactory.getLogger(this.getClass());
	
	public static final String SPLIT = ";";
	
	
	public static String getIpAddr(HttpServletRequest request)
	{
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
		{
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
		{
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
		{
			ip = request.getRemoteAddr();
		}
		return ip;
	}
	
	
	
	public  String getIpAddr2(HttpServletRequest request)
	{
		String client_ip = request.getHeader("X-FORWARDED-FOR");
		//logger.info("client_ip:x-forwarded-for,request:"+client_ip);
	    if(client_ip == null || client_ip.length() == 0 || "unknown".equalsIgnoreCase(client_ip)) {
	        client_ip = request.getHeader("Proxy-Client-IP");
	    	//logger.info("Proxy-Client-IP,request:"+client_ip);
	    }
	    if(client_ip == null || client_ip.length() == 0 || "unknown".equalsIgnoreCase(client_ip)) {
	    	//logger.info("WL-Proxy-Client-IP,request:"+client_ip);
	        client_ip = request.getHeader("WL-Proxy-Client-IP");
	    }
	    if(client_ip == null || client_ip.length() == 0 || "unknown".equalsIgnoreCase(client_ip)) {
	    	//logger.info("getRemoteAddr,request:"+client_ip);
	        client_ip = request.getRemoteAddr();
	        if(client_ip.equals("127.0.0.1") || client_ip.equals("0:0:0:0:0:0:0:1")){
	            //根据网卡取本机配置的IP
	            InetAddress inet = null;
	            try {
	                inet = InetAddress.getLocalHost();
	            } catch (UnknownHostException e) {
	                e.printStackTrace();
	            }
	            client_ip = inet.getHostAddress();
	        }  
	    }  
	    //对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割  
	    if(client_ip != null && client_ip.length() > 15){ //"***.***.***.***".length() = 15  
	        if(client_ip.indexOf(",") > 0){
	            client_ip = client_ip.substring(0,client_ip.indexOf(","));
	        }
	    }
	    
	   /* try {
			String server_ip = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		return client_ip;
	}
}
