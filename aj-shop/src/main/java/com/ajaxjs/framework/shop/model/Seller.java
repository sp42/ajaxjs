package com.ajaxjs.framework.shop.model;

import com.ajaxjs.framework.BaseModel;

/**
 * 店家
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
public class Seller extends BaseModel {
	private double latitude;

	private String tel;

	private String sellerMaster;

	private String address;

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getSellerMaster() {
		return sellerMaster;
	}

	public void setSellerMaster(String sellerMaster) {
		this.sellerMaster = sellerMaster;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	private double longitude;
}
