package com.ajaxjs.user.third.weibo;

/**
 * 从微博接口可以获取有用的用户信息
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
public class WeiboUserInfo {
	/**
	 * 
	 */
	private String accessToken;

	/**
	 * 
	 */
	private String name;
	
	/**
	 * 
	 */
	private String uid;

	/**
	 * 
	 */
	private String gender;

	/**
	 * 
	 */
	private String avatar;

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
