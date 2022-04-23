package com.ajaxjs.wechat.applet.model;

import com.ajaxjs.user.User;

/**
 * 小程序执行 wx.getUserInfo() 返回的用户信息
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
public class UserInfo {
	private String nickName;
	private String avatarUrl;
	private int gender;
	private String province;
	private String city;
	private String country;

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getAvatarUrl() {
		return avatarUrl;
	}

	public void setAvatarUrl(String avatarUrl) {
		this.avatarUrl = avatarUrl;
	}

	public int getGender() {
		return gender;
	}

	public void setGender(int gender) {
		this.gender = gender;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	/**
	 * 转为系统的用户
	 * 
	 * @return 标准用户类型
	 */
	public User toSystemUser() {
		User user = new User();
		user.setUsername(nickName);
		user.setGender(gender);
		user.setAvatar(avatarUrl);
		user.setLocation(country + " " + province + " " + city);

		return user;
	}
}
