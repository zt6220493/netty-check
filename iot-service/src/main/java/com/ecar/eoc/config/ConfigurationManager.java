package com.ecar.eoc.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Properties;

import org.springframework.boot.system.ApplicationHome;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;

public class ConfigurationManager {
	

	private static Properties prop = new Properties();
	
	/**
	 * 静态代码块
	 * 功能:一次读取，多次使用，节省内存
	 */
	static {
		try {
			prop.load(getPropFileInputStream("conf/system.properties"));
		} catch (Exception e) {
			e.printStackTrace();  
		}
	}
	
	/**
	 * 获取配置文件文件流，默认获取顺序：
	 * 1、先到运行目录查找该文件，若有文件停止查找；否则，走2步
	 * 2、到classpath目录查找配置文件，若能找到，停止；否则，抛配置文件找不到的异常
	 * @param fileRelativeName 相对路径文件名 
	 * @return
	 * @throws FileNotFoundException 
	 */
	public static FileInputStream getPropFileInputStream(String fileRelativeName) throws FileNotFoundException {
		//1、项目目录下查找配置文件
		ApplicationHome applicationHome = new ApplicationHome(ConfigurationManager.class);
		String route = applicationHome.getDir().getAbsolutePath();
		String FS = System.getProperty("file.separator");
		
		File file = new File(route+FS+fileRelativeName);
		if(file.exists()) {
			return new FileInputStream(file);
		}
		//2、classpath下查找配置文件
		File file2 = ResourceUtils.getFile("classpath:"+fileRelativeName);
		if(file2.exists()) {
			return new FileInputStream(file2);
		}else {
			throw new FileNotFoundException("配置文件不存在");
		}
	}
	
	/**
	 * 获取指定key对应的value
	 * 
	 * 第一次外界代码，调用ConfigurationManager类的getProperty静态方法时，JVM内部会发现
	 * ConfigurationManager类还不在JVM的内存中
	 * @param key 
	 * @return value
	 */
	public static String getProperty(String key,String defaultValue) {
		String value = prop.getProperty(key);
		if(StringUtils.isEmpty(value)) {
			return defaultValue;
		}
		return value;
	}
	
	public static String getProperty(String key) {
		return prop.getProperty(key);
	}
	
	/**
	 * 获取整数类型的配置项
	 * @param key
	 * @return value
	 */
	public static Integer getInteger(String key) {
		String value = getProperty(key);
		try {
			return Integer.valueOf(value);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public static Integer getInteger(String key,int defaultValue) {
		String value = getProperty(key);
		if(StringUtils.isEmpty(value)) {
			return defaultValue;
		}
		try {
			return Integer.valueOf(value);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	/**
	 * 获取布尔类型的配置项
	 * @param key
	 * @return value
	 */
	public static Boolean getBoolean(String key) {
		String value = getProperty(key);
		try {
			return Boolean.valueOf(value);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * 获取Long类型的配置项
	 * @param key
	 * @return
	 */
	public static Long getLong(String key) {
		String value = getProperty(key);
		try {
			return Long.valueOf(value);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0L;
	}
}
