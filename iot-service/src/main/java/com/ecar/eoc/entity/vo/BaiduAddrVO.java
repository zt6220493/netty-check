package com.ecar.eoc.entity.vo;

public class BaiduAddrVO {
	private String adcode;
	private String city;
	private String cityCode;
	private String addressName;
	
	public String getAdcode() {
		return adcode;
	}
	public void setAdcode(String adcode) {
		this.adcode = adcode;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getCityCode() {
		return cityCode;
	}
	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}
	public String getAddressName() {
		return addressName;
	}
	public void setAddressName(String addressName) {
		this.addressName = addressName;
	}
	
	@Override
	public String toString() {
		return "BaiduAddrVO [adcode=" + adcode + ", city=" + city
				+ ", cityCode=" + cityCode + ", addressName=" + addressName
				+ "]";
	}
}
