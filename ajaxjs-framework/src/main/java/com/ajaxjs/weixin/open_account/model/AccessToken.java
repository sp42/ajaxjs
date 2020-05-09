package com.ajaxjs.weixin.open_account.model;

/**
 * 访问令牌，接口调用凭据。注意这里包含了 AccessToken + JsApiTicket
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
public class AccessToken {
	/**
	 * 获取到的凭证
	 */
	private String token;

	/**
	 * JS API 凭证
	 */
	private String jsApiTicket;

	/**
	 * 凭证有效时间，单位：秒
	 */
	private int expiresIn;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public int getExpiresIn() {
		return expiresIn;
	}

	public void setExpiresIn(int expiresIn) {
		this.expiresIn = expiresIn;
	}

	public String getJsApiTicket() {
		return jsApiTicket;
	}

	public void setJsApiTicket(String jsApiTicket) {
		this.jsApiTicket = jsApiTicket;
	}

}
