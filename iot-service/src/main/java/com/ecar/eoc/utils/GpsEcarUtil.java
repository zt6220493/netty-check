package com.ecar.eoc.utils;

import java.util.ArrayList;
import java.util.List;
import com.ecar.eoc.entity.trip.EocGps;

public class GpsEcarUtil {
	
	public final static double PI = 3.14159265354;

	private final static double D2R = 0.017453;

	private final static double a2 = 6378137.0;

	private final static double e2 = 0.006739496742337;
	
	private static final int AVG_SPEED_CORRECT_GPS = 140;

	public static Double getDistance(EocGps pt1, EocGps pt2)
	{
		if (pt1.getLon() == pt2.getLon() && pt1.getLat() == pt2.getLat())
		{
			return 0.0;
		}
		else
		{
			double fdLambda = (pt1.getLon() - pt2.getLon()) * D2R;
			double fdPhi = (pt1.getLat() - pt2.getLat()) * D2R;
			double fPhimean = ((pt1.getLat() + pt2.getLat()) / 2.0) * D2R;
			double fTemp = 1 - e2 * (Math.pow(Math.sin(fPhimean), 2));
			double fRho = (a2 * (1 - e2)) / Math.pow(fTemp, 1.5);
			double fNu = a2
					/ (Math.sqrt(1 - e2
							* (Math.sin(fPhimean) * Math.sin(fPhimean))));
			double fz = Math.sqrt(Math.pow(Math.sin(fdPhi / 2.0), 2)
					+ Math.cos(pt2.getLat() * D2R)
					* Math.cos(pt1.getLat() * D2R)
					* Math.pow(Math.sin(fdLambda / 2.0), 2));
			fz = 2 * Math.asin(fz);
			double fAlpha = Math.cos(pt2.getLat() * D2R) * Math.sin(fdLambda)
					* 1 / Math.sin(fz);
			fAlpha = Math.asin(fAlpha);
			double fR = (fRho * fNu)
					/ ((fRho * Math.pow(Math.sin(fAlpha), 2)) + (fNu * Math
							.pow(Math.cos(fAlpha), 2)));

			double temp = (fz * fR) / 1000;
			if (Double.isNaN(temp))
			{
				return 0.0;
			}
			else
			{
				return temp;
			}
		}
	}
	
	public static EocGps getCorrectGps(List<EocGps> list){
		if(null==list || list.size()==0){
			return null;
		}
		//最后一个点若为同步最新点，直接返回不需要进行计算
		Integer online = list.get(list.size()-1).getOnline();
		if(online!=null && online.intValue()==10) {
			return list.get(list.size()-1);
		}
		
		//只有一个点或者两个点，则考虑不需要进行去噪运算
		if(list.size()<=2 ){
			return list.get(0);
		}
		List<List<EocGps>> boList = new ArrayList<List<EocGps>>();
		for(int i=0;i<list.size();i++){
			if(boList.size()==0){
				List<EocGps> gpsList = new ArrayList<EocGps>();
				gpsList.add(list.get(i));
				boList.add(gpsList);
				continue;
			}
			//将相近GPS分组
			for(int n=0;n<boList.size();n++){
				List<EocGps> bList = boList.get(n);
				if(bList==null || bList.size()==0){
					boList.remove(n);
					n--;
				}
				double dis = getDistance(bList.get(0), list.get(i));
				double durations = Math.abs(list.get(i).getTime().getTime()-bList.get(0).getTime().getTime())*0.001/3600;
				if((dis/durations)<=AVG_SPEED_CORRECT_GPS){
					bList.add(list.get(i));
					break;
				}else if(n==(boList.size()-1)){
					List<EocGps> gpsList = new ArrayList<EocGps>();
					gpsList.add(list.get(i));
					boList.add(gpsList);
					break;
				}
			}
		}
		//轮询分组，组的成员越多，时间最早的GPS点作为在线查车当前点
		EocGps result = null;
		int curNum = 0;
		for(List<EocGps> obj:boList){
			if(obj.size()>curNum){
				result = obj.get(0);
				curNum = obj.size();
			}
		}
		if(null==result){
			return list.get(0);
		}
		return result;
	}
	
	/**
	 * 判断gps是否在中国
	 * @param gps
	 * @return
	 */
	public static boolean isInChina(EocGps gps){
		if(gps==null){
			return false;
		}
		boolean flag = true;
		if(gps.getLat().doubleValue()<3 || gps.getLat().doubleValue()>54){
			flag = false;
		}else if(gps.getLon().doubleValue()<73 || gps.getLon().doubleValue()>136){
			flag = false;
		}
		return flag;
	}
	
	/**
	 * 判断gps是否正确
	 * @param gps
	 * @return
	 */
	public static boolean isGpsRight(EocGps gps){
		if(gps==null){
			return false;
		}
		boolean flag = true;
		if(gps.getLat().doubleValue()<-90 || gps.getLat().doubleValue()>90){
			flag = false;
		}else if(gps.getLon().doubleValue()<-180 || gps.getLon().doubleValue()>180){
			flag = false;
		}
		return flag;
	}
}
