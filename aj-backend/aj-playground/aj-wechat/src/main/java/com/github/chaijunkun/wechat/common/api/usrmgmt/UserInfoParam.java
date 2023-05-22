package com.github.chaijunkun.wechat.common.api.usrmgmt;

import com.fasterxml.jackson.annotation.JsonProperty;

import com.github.chaijunkun.wechat.common.api.WeChatAPIParamWithToken;

/**
 * 用户详细信息查询参数
 * @author chaijunkun
 * @since 2016年9月1日
 */
public class UserInfoParam extends WeChatAPIParamWithToken {
	
	/** 普通用户的标识，对当前公众号唯一 */
	@JsonProperty(value = "openid")
	private String openId;
	
	/** 返回国家地区语言版本 */
	@JsonProperty(value = "lang")
	private String lang;

	/**
	 * 获取普通用户的标识
	 * @return 普通用户的标识
	 */
	public String getOpenId() {
		return openId;
	}

	/**
	 * 设置普通用户的标识
	 * @param openId 普通用户的标识
	 */
	public void setOpenid(String openId) {
		this.openId = openId;
	}

	/**
	 * 获取返回国家地区语言版本
	 * @return 返回国家地区语言版本
	 */
	public String getLang() {
		return lang;
	}

	/**
	 * 设置返回国家地区语言版本
	 * @param lang 返回国家地区语言版本
	 */
	public void setLang(String lang) {
		this.lang = lang;
	}
	
}
