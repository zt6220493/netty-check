package com.ecar.eoc.utils;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ecar.eoc.entity.vo.BaiduAddrVO;


public class BaiduMapUtil {
	
	public static final Set<String> citySet = new HashSet<String>();
	
	public static String url = "http://api.map.baidu.com/geocoder/v2/";
	public static int validDiatance = 120;
	
	static{
		citySet.add("北京市");
		citySet.add("天津市");
		citySet.add("上海市");
		citySet.add("重庆市");
	}
	
	/**
	 * 查询百度接口，得到逆地址信息(获取城市，城市编码，以及地址详情)
	 * @param lat
	 * @param lon
	 * @return
	 */
	public static BaiduAddrVO getBaiduAddrByGps(Double lat,Double lon){
		String param = "ak=32b2e856ad8b2ff21ae784f778c696fe&callback=renderReverse&output=json&pois=1&coordtype=wgs84ll&location="+lat+","+lon;
		String content = null;
		try {
			content = HttpUtil.sendGet(url, param);
			JSONObject obj = JSON.parseObject(content.substring(content.indexOf("(")+1, content.length()-1));
			JSONObject jsonResult = obj.getJSONObject("result");
			JSONObject addressComponent = jsonResult.getJSONObject("addressComponent");
			BaiduAddrVO baiduAddrVO = new BaiduAddrVO();
			String city = addressComponent.getString("city");
			String adcode = addressComponent.getString("adcode");
			if(citySet.contains(city)){
				baiduAddrVO.setCityCode(adcode.substring(0, 2));
			}else{
				baiduAddrVO.setCityCode(adcode.substring(0, 4));
			}
			baiduAddrVO.setAdcode(adcode);
			baiduAddrVO.setCity(addressComponent.getString("city"));
			String formatted_address = addressComponent.getString("city")+addressComponent.getString("district")
									+addressComponent.getString("street");
			JSONArray jsonArray = jsonResult.getJSONArray("pois");
			if(jsonArray.isEmpty() ){
				if(StringUtils.isEmpty(formatted_address)){
					throw new Exception(new Date()+"百度地址接口改变，请重新梳理代码逻辑！");
				}
				baiduAddrVO.setAddressName(formatted_address);
				return baiduAddrVO;
			}
			JSONObject jo = (JSONObject) jsonArray.get(0);
			for(int i=0;i<jsonArray.size();i++){
				if(Integer.valueOf(((JSONObject)jsonArray.get(i)).getString("distance"))
						.compareTo(Integer.valueOf(jo.getString("distance")))<0){
					jo = (JSONObject) jsonArray.get(i);
				}
			}
			if(Integer.valueOf(jo.getString("distance")).intValue()<validDiatance){
				formatted_address +=jo.getString("name"); 
			}else{
				formatted_address +=addressComponent.getString("street_number");
			}
			
            if(StringUtils.isEmpty(formatted_address))
            {
            	return null;
            }
            baiduAddrVO.setAddressName(formatted_address);
            return baiduAddrVO;
		} catch (Exception e) {
			
		}
		
		return null;
	}
	
	/**
	 * @param lat 纬度
	 * @param lon 经度
	 * @param mapType 地图类型 0表示GPS原始数据，1表示转换成百度地图坐标后的数据
	 * @return
	 */
	public static String getAddressByGps(double lat, double lon,Integer mapType) {
		//坐标类型，1（wgs84ll即GPS经纬度），2（gcj02ll即国测局经纬度坐标），3（bd09ll即百度经纬度坐标），4（bd09mc即百度米制坐标）
		String coordtype="";
		if (mapType==null||mapType==0) {
			coordtype="wgs84ll";
		} else if (mapType==1) {
			coordtype="bd09ll";
		}
		String location=lat+","+lon;
        try {
			String content = HttpUtil.sendGet("http://api.map.baidu.com/geocoder/v2/?ak=32b2e856ad8b2ff21ae784f778c696fe&output=json&pois=1&coordtype="+coordtype+"&location="+location, null);
			int validDiatance =150;
        	
			if(StringUtils.isEmpty(content)){
				throw new Exception("调用百度api获取gps地址失败!");
			}
			JSONObject jsonResult = JSON.parseObject(content).getJSONObject("result");
			JSONObject addressComponent = jsonResult.getJSONObject("addressComponent");
			
			String formatted_address = addressComponent.getString("city")+addressComponent.getString("district")
									+addressComponent.getString("street");
			JSONArray jsonArray = jsonResult.getJSONArray("pois");
			if(jsonArray.isEmpty() ){
				if(StringUtils.isEmpty(formatted_address)){
					throw new Exception(new Date()+"百度地址接口改变，请重新梳理代码逻辑！");
				}
				return formatted_address;
			}
			JSONObject jo = (JSONObject) jsonArray.get(0);
			for(int i=0;i<jsonArray.size();i++){
				if(Integer.valueOf(((JSONObject)jsonArray.get(i)).getString("distance"))
						.compareTo(Integer.valueOf(jo.getString("distance")))<0){
					jo = (JSONObject) jsonArray.get(i);
				}
			}
			if(Integer.valueOf(jo.getString("distance")).intValue()<validDiatance){
				formatted_address +=jo.getString("name"); 
			}else{
				formatted_address +=addressComponent.getString("street_number");
			}
			
            if(StringUtils.isEmpty(formatted_address))
            {
            	return null;
            }
            return formatted_address;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static String getAddressGps(String address){
		String addr = "&address="+address;
		String url = "http://api.map.baidu.com/geocoder/v2/?output=json&ak=32b2e856ad8b2ff21ae784f778c696fe";
		try {
			String content = HttpUtil.sendGet(url+addr,null);
			JSONObject obj = JSON.parseObject(content);
			String gpsStr = obj.getJSONObject("result").getJSONObject("location").getDoubleValue("lat")+
					","+obj.getJSONObject("result").getJSONObject("location").getDoubleValue("lng");
			return gpsStr;
		} catch (Exception e) {
			
		}
		return null;
	}
	
	public static String getPCS(String lat,String lng){
		String gps = "&location="+lat+","+lng;
		String url = "http://api.map.baidu.com/geocoder/v2/?output=json&pois=1&ak=32b2e856ad8b2ff21ae784f778c696fe";
		try {
			String content = HttpUtil.sendGet(url+gps,null);
			JSONObject obj = JSON.parseObject(content);
			String str = obj.getJSONObject("result").getJSONObject("addressComponent").getString("province")+","
					+obj.getJSONObject("result").getJSONObject("addressComponent").getString("city")+","+
					obj.getJSONObject("result").getJSONObject("addressComponent").getString("district");
			return str;
		} catch (Exception e) {
			
		}
		return null;
	}
	
	public static void main(String[] args){
		System.out.println(BaiduMapUtil.getAddressByGps(22.54688,113.935632,null));
	}
}
