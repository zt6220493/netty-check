package com.ecar.eoc.entity.vo;

public class GaodeAddrVO {
	private String address;
	private String province;
	private String citycode;
	private String city;
	private String district;
	private String adcode;
	private String township;
	private String towncode;
	
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getCitycode() {
		return citycode;
	}
	public void setCitycode(String citycode) {
		this.citycode = citycode;
	}
	public String getDistrict() {
		return district;
	}
	public void setDistrict(String district) {
		this.district = district;
	}
	public String getAdcode() {
		return adcode;
	}
	public void setAdcode(String adcode) {
		this.adcode = adcode;
	}
	public String getTownship() {
		return township;
	}
	public void setTownship(String township) {
		this.township = township;
	}
	public String getTowncode() {
		return towncode;
	}
	public void setTowncode(String towncode) {
		this.towncode = towncode;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	@Override
	public String toString() {
		return "GaodeAddrVO [address=" + address + ", province=" + province
				+ ", citycode=" + citycode + ", district=" + district
				+ ", adcode=" + adcode + ", township=" + township
				+ ", towncode=" + towncode + "]";
	}
}
