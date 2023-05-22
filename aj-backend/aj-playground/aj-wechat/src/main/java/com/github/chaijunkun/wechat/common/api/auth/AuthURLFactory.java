package com.github.chaijunkun.wechat.common.api.auth;

import com.github.chaijunkun.wechat.common.api.AbstractURLFactory;
import com.github.chaijunkun.wechat.common.api.URLBean;

/**
 * 消息管理接口相关URL
 * @author chaijunkun
 * @since 2016年9月6日
 */
public class AuthURLFactory extends AbstractURLFactory {
	
	/** 通过code换取网页授权access_token URL */
	private final URLBean accessToken = new URLBean(true, COMMON_API_DOMAIN, "/sns/oauth2/access_token");

	/**
	 * 获取通过code换取网页授权access_token URL
	 * @return 通过code换取网页授权access_token URL
	 */
	public String getAccessToken() {
		return accessToken.getAbsoluteURL();
	}

}
