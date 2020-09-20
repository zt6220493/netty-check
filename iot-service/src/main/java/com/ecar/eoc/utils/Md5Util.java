package com.ecar.eoc.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Md5Util {

	/**
	 * 将字符串转成MD5值
	 * 
	 * @param string
	 * @return
	 */
	public static String stringToMD5(String string) {
		byte[] hash;
		try {
			hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
		} catch (NoSuchAlgorithmException e) {
			return null;
		} catch (UnsupportedEncodingException e) {
			return null;
		}

		StringBuilder hex = new StringBuilder(hash.length * 2);
		for (byte b : hash) {
			if ((b & 0xFF) < 0x10)
				hex.append("0");
			hex.append(Integer.toHexString(b & 0xFF));
		}

		return hex.toString();
	}

	public final static String MD5(String s) {
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
		try {
			byte[] btInput = s.getBytes("UTF-8");
			// 获得MD5摘要算法�?MessageDigest 对象
			MessageDigest mdInst = MessageDigest.getInstance("MD5");
			// 使用指定的字节更新摘�?
			mdInst.update(btInput);
			// 获得密文
			byte[] md = mdInst.digest();
			// 把密文转换成十六进制的字符串形式
			int j = md.length;
			char str[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}
			return new String(str);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String stringToMD516(String string) {
		return stringToMD5(string).substring(8, 24);
	}

	public static void main(String[] args) {
		System.out.println(stringToMD5("309QdIrN"));
		System.out.println(stringToMD516("309QdIrN").toUpperCase());
	}
}
