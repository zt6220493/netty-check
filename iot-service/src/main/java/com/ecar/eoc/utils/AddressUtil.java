package com.ecar.eoc.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ecar.eoc.config.ConfigurationManager;
import com.ecar.eoc.entity.vo.BaiduAddrVO;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * 统一地理位置解析
 */
public class AddressUtil {

	public static final Set<String> citySet = new HashSet<String>();

//	public static String allUrl = "http://100.119.167.107:5276/lbs/addr/all?";
	public static String allUrl = ConfigurationManager.getProperty("address.all.url","http://192.168.171.231:5276/lbs/addr/all?");
	public static String baseUrl = ConfigurationManager.getProperty("address.base.url","http://192.168.171.231:5276/lbs/addr/base?");
//	public static String baseUrl = "http://100.119.167.107:5276/lbs/addr/base?";
	public static int validDiatance = 120;
	
	static{
		citySet.add("北京市");
		citySet.add("天津市");
		citySet.add("上海市");
		citySet.add("重庆市");
	}
	
	/**
	 * 获取百度地址
	 * @param lat 纬度
	 * @param lon 经度
	 * @param mapType 地图类型 0表示GPS原始数据，1表示转换成百度地图坐标后的数据
	 * @return
	 */
	public static String getBDAddressByGps(double lat, double lon, Integer mapType) {
		//坐标类型，1（wgs84ll即GPS经纬度），2（gcj02ll即国测局经纬度坐标），3（bd09ll即百度经纬度坐标），4（bd09mc即百度米制坐标）
		String coordtype="";
		if (mapType==null||mapType==0) {
			coordtype="wgs84";
		} else if (mapType==1) {
			coordtype="bd_09";
		}
		String content = null;
        try {
			//http://192.168.171.231:5276/lbs/addr/all?lat=39.1524&lng=118.725&appKey=YRr7e6LaaClgufoV8G3WhG2&priorMap=bd_map&coordType=wgs84
			StringBuffer param = new StringBuffer("lat=").append(lat).append("&lng=").append(lon).append("&priorMap=bd_map&coordType=").append(coordtype);
			content = HttpUtil.sendGet(allUrl, param.toString());

			int validDiatance =150;
        	
			if(StringUtils.isEmpty(content)){
				throw new Exception("调用百度api获取gps地址失败!");
			}
			JSONObject jsonResult = JSON.parseObject(content).getJSONObject("data");
			String formatted_address = jsonResult.getString("formatAddress");
			JSONArray jsonArray = jsonResult.getJSONArray("poiRegions");
			if (jsonArray != null && !jsonArray.isEmpty()) {
				JSONObject jo = (JSONObject) jsonArray.get(0);
				for(int i = 0; i < jsonArray.size(); i++){
					if(Integer.valueOf(((JSONObject)jsonArray.get(i)).getString("distance"))
							.compareTo(Integer.valueOf(jo.getString("distance")))<0){
						jo = (JSONObject) jsonArray.get(i);
					}
				}
				if(Integer.valueOf(jo.getString("distance")).intValue()<validDiatance){
					formatted_address +=jo.getString("name");
				}
			}
            if(StringUtils.isEmpty(formatted_address)) {
            	return null;
            }
            return formatted_address;
		}catch (Exception e){
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 查询百度接口，得到逆地址信息(获取城市，城市编码，以及地址详情)
	 * @param lat
	 * @param lon
	 * @return
	 */
	public static BaiduAddrVO getBaiduAddrByGps(Double lat, Double lon){
		//http://192.168.171.231:5276/lbs/addr/all?lat=39.1524&lng=118.725&appKey=YRr7e6LaaClgufoV8G3WhG2&priorMap=bd_map&coordType=wgs84
		StringBuffer param = new StringBuffer("lat=").append(lat).append("&lng=").append(lon).append("&priorMap=bd_map&coordType=wgs84");
		String content = null;
		try {
			content = HttpUtil.sendGet(allUrl, param.toString());
			if (StringUtils.isEmpty(content)) {
				throw new Exception(new Date()+"百度接口查询失败！");
			}
			JSONObject obj = JSON.parseObject(content);
			JSONObject jsonResult = obj.getJSONObject("data");
			BaiduAddrVO baiduAddrVO = new BaiduAddrVO();
			String city = jsonResult.getString("city");
			String adcode = jsonResult.getString("adcode");
			if(citySet.contains(city)){
				baiduAddrVO.setCityCode(adcode.substring(0, 2));
			}else{
				baiduAddrVO.setCityCode(adcode.substring(0, 4));
			}
			baiduAddrVO.setAdcode(adcode);
			baiduAddrVO.setCity(city);
			String formatted_address = jsonResult.getString("formatAddress");
			JSONArray jsonArray = jsonResult.getJSONArray("poiRegions");
			if (jsonArray != null && !jsonArray.isEmpty()) {
				JSONObject jo = (JSONObject) jsonArray.get(0);
				for(int i = 0; i < jsonArray.size(); i++){
					if(Integer.valueOf(((JSONObject)jsonArray.get(i)).getString("distance"))
							.compareTo(Integer.valueOf(jo.getString("distance")))<0){
						jo = (JSONObject) jsonArray.get(i);
					}
				}
				if(Integer.valueOf(jo.getString("distance")).intValue()<validDiatance){
					formatted_address +=jo.getString("name");
				}
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

	public static void main(String[] args){
		System.out.println(AddressUtil.getBaiduAddrByGps(22.546803,113.935875));
//		System.out.println(getBDAddressByGps(22.546762,113.936245,null));;
	}
}
