package com.ecar.eoc.utils;

/**
 * 行程报告标签定义
 *
 */
public class LabelUtil {

	public static String labelContorl(Integer score){
		if(score != null){
			if(score >= 60 && score <65){
				return "不紧不慢";
			}else if(score >= 65 && score <70){
				return "起步平稳";
			}else if(score >= 70 && score <75){
				return "操作娴熟";
			}else if(score >= 75 && score <80){
				return "技艺精湛";
			}else if(score >= 80 && score <85){
				return "技艺超群";
			}else if(score >= 85 && score <90){
				return "出神入化";
			}else if(score >= 90 && score <=95){
				return "得心应手";
			}else if(score > 95 && score <=100){
				return "炉火纯青";
			}
		}
		return "";
	}
}
