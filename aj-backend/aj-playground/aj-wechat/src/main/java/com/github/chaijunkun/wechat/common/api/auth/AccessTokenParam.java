package com.github.chaijunkun.wechat.common.api.auth;

import com.fasterxml.jackson.annotation.JsonProperty;

import com.github.chaijunkun.wechat.common.api.WeChatAPIParam;

/**
 * 
 * @author chaijunkun
 * @since 2016年8月26日
 */
public class AccessTokenParam extends WeChatAPIParam {
	
	public static enum AccessTokenGrantType{
		authorization_code("authorization_code");
		private String type;
		private AccessTokenGrantType(String type){
			this.type = type;
		}
		public String getType(){
			return type;
		}
	}
	
	/** appId */
	@JsonProperty(value = "appid")
	private String appId;
	
	/** 密钥 */
	@JsonProperty(value = "secret")
	private String secret;
	
	/** 前一步获取的code参数*/
	@JsonProperty(value = "code")
	private String code;
	
	/** 授权类型 */
	@JsonProperty(value = "grant_type")
	private String grantType;
	
	/**
	 * 获取appId
	 * @return appId
	 */
	public String getAppId() {
		return appId;
	}

	/**
	 * 设置appId
	 * @param appId appId
	 */
	public void setAppId(String appId) {
		this.appId = appId;
	}

	/**
	 * 获取密钥
	 * @return 密钥
	 */
	public String getSecret() {
		return secret;
	}

	/**
	 * 设置密钥
	 * @param secret 密钥
	 */
	public void setSecret(String secret) {
		this.secret = secret;
	}
	
	/**
	 * 获取前一步获取的code参数
	 * @return 前一步获取的code参数
	 */
	public String getCode() {
		return code;
	}

	/**
	 * 设置前一步获取的code参数
	 * @param code 前一步获取的code参数
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * 获取授权类型
	 * @return 授权类型
	 */
	public String getGrantType() {
		return grantType;
	}

	/**
	 * 设置授权类型
	 * @param grantType 授权类型
	 */
	public void setGrantType(String grantType) {
		this.grantType = grantType;
	}

}
