package com.ecar.eoc.utils;

import java.util.HashSet;
import java.util.Set;

import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ecar.eoc.entity.gps.Point;
import com.ecar.eoc.entity.vo.GaodeAddrVO;

public class GaoDeMapUtil {
	
	public final static String url = "http://restapi.amap.com/v3/geocode/regeo";
	public static final Set<String> citySet = new HashSet<String>();
	static{
		citySet.add("北京市");
		citySet.add("天津市");
		citySet.add("上海市");
		citySet.add("重庆市");
	}
	/**
	 * 根据经纬度获取高德地图地址信息
	 * lon经度在前，lat纬度在后
	 * eg:lon=113.935895,lat=22.546003
	 * @param lat
	 * @param lon
	 * @return
	 */
	public static GaodeAddrVO getAddrByGps(double lat,double lon){
		String param = "key=d6199a259a4ec1e72025d97fe430f3c5&radius=1000&extensions=all&batch=false&roadlevel=0&location="+lon+","+lat;
		String content = null;
		content = HttpUtil.sendGet(url, param);
		JSONObject obj = JSON.parseObject(content);
		String status = obj.getString("status");
		if(StringUtils.isEmpty(status) || "0".equals(status)){
			return null;
		}
		GaodeAddrVO gdAddr = new GaodeAddrVO();
		gdAddr.setAddress(obj.getJSONObject("regeocode").getString("formatted_address"));
		gdAddr.setAdcode(obj.getJSONObject("regeocode").getJSONObject("addressComponent").getString("adcode"));
		gdAddr.setCitycode(obj.getJSONObject("regeocode").getJSONObject("addressComponent").getString("citycode"));
		gdAddr.setDistrict(obj.getJSONObject("regeocode").getJSONObject("addressComponent").getString("district"));
		gdAddr.setProvince(obj.getJSONObject("regeocode").getJSONObject("addressComponent").getString("province"));
		gdAddr.setTownship(obj.getJSONObject("regeocode").getJSONObject("addressComponent").getString("township"));
		gdAddr.setTowncode(obj.getJSONObject("regeocode").getJSONObject("addressComponent").getString("towncode"));
		gdAddr.setCity(obj.getJSONObject("regeocode").getJSONObject("addressComponent").getString("city"));
		return gdAddr;
	}
	
	public static String getAddrStrByGps(double lat,double lon){
		Point point = CoordinateConversion.wgs_gcj_encrypts(lat, lon);
		GaodeAddrVO gdAddr = getAddrByGps(point.getLat(), point.getLon());
		if(null==gdAddr){
			return "";
		}else{
			return gdAddr.getAddress();
		}
	}
	
	/**
	 * 根据城市名和区域adcode，获取城市code
	 * @param cityName
	 * @param adCode
	 * @return
	 */
	public static String getCityCode(String cityName,String adCode){
		String cityCode = "";
		if(citySet.contains(cityName)){
			cityCode = adCode.substring(0, 2);
		}else{
			cityCode = adCode.substring(0, 4);
		}
		return cityCode;
	}
	
	public static void main(String[] args){
		System.out.println(getAddrByGps(22.546003, 113.935895));
	}
}
