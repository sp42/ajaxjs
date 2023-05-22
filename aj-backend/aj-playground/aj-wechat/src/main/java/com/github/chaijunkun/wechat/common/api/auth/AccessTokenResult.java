package com.github.chaijunkun.wechat.common.api.auth;

import com.fasterxml.jackson.annotation.JsonProperty;

import com.github.chaijunkun.wechat.common.api.WeChatAPIRet;

/**
 * 获取token结果
 * @author chaijunkun
 * @since 2016年8月26日
 */
public class AccessTokenResult extends WeChatAPIRet {
	
	private static final long serialVersionUID = -7231483765347236143L;

	/** 获取到的凭证 */
	@JsonProperty(value = "access_token")
	private String accessToken;
	
	/** 凭证有效时间，单位：秒 */
	@JsonProperty(value = "expires_in")
	private Integer expiresIn;
	
	/** 用户刷新access_token */
	@JsonProperty(value = "refresh_token")
	private String refreshToken;
	
	/**
	 * 用户唯一标识，
	 * 请注意，在未关注公众号时，用户访问公众号的网页，也会产生一个用户和公众号唯一的OpenID
	 */
	@JsonProperty(value = "openid")
	private String openId;
	
	/** 用户授权的作用域，使用逗号（,）分隔 */
	@JsonProperty(value = "scope")
	private String scope;

	/**
	 * 获取获取到的凭证
	 * @return 获取到的凭证
	 */
	public String getAccessToken() {
		return accessToken;
	}

	/**
	 * 设置获取到的凭证
	 * @param accessToken 获取到的凭证
	 */
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	/**
	 * 获取凭证有效时间
	 * @return 凭证有效时间
	 */
	public Integer getExpiresIn() {
		return expiresIn;
	}

	/**
	 * 设置凭证有效时间
	 * @param expiresIn 凭证有效时间
	 */
	public void setExpiresIn(Integer expiresIn) {
		this.expiresIn = expiresIn;
	}

	/**
	 * 获取用户刷新access_token
	 * @return 用户刷新access_token
	 */
	public String getRefreshToken() {
		return refreshToken;
	}

	/**
	 * 设置用户刷新access_token
	 * @param refreshToken 用户刷新access_token
	 */
	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	/**
	 * 获取用户唯一标识
	 * @return 用户唯一标识
	 */
	public String getOpenId() {
		return openId;
	}

	/**
	 * 设置用户唯一标识
	 * @param openId 用户唯一标识
	 */
	public void setOpenId(String openId) {
		this.openId = openId;
	}

	/**
	 * 获取用户授权的作用域
	 * @return 用户授权的作用域
	 */
	public String getScope() {
		return scope;
	}

	/**
	 * 设置用户授权的作用域
	 * @param scope 用户授权的作用域
	 */
	public void setScope(String scope) {
		this.scope = scope;
	}
	
}
