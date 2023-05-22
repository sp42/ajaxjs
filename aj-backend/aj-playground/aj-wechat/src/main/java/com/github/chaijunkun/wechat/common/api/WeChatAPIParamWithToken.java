package com.github.chaijunkun.wechat.common.api;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * API基本的需要签名的公共参数定义
 * @author chaijunkun
 * @since 2015年4月15日
 */
public abstract class WeChatAPIParamWithToken extends WeChatAPIParam {
	
	/** 访问令牌参数名 */
	public static final String ACCESS_TOKEN_FIELD = "access_token";
	
	/** 访问令牌 */
	@JsonProperty(value = ACCESS_TOKEN_FIELD)
	private String accessToken;

	/**
	 * 获取访问令牌
	 * @return 访问令牌
	 */
	public String getAccessToken() {
		return accessToken;
	}

	/**
	 * 设置访问令牌
	 * @param accessToken 访问令牌
	 */
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	
}
