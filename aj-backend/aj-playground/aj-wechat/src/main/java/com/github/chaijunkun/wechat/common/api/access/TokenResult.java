package com.github.chaijunkun.wechat.common.api.access;

import com.fasterxml.jackson.annotation.JsonProperty;

import com.github.chaijunkun.wechat.common.api.WeChatAPIRet;

/**
 * 获取token结果
 * @author chaijunkun
 * @since 2016年8月26日
 */
public class TokenResult extends WeChatAPIRet {
	
	private static final long serialVersionUID = -8242372755146179695L;

	/** 获取到的凭证 */
	@JsonProperty(value = "access_token")
	private String accessToken;
	
	/** 凭证有效时间，单位：秒 */
	@JsonProperty(value = "expires_in")
	private Integer expiresIn;

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
	
}
