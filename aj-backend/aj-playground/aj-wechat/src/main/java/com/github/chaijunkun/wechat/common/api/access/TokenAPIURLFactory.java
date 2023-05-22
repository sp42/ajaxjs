package com.github.chaijunkun.wechat.common.api.access;

import com.github.chaijunkun.wechat.common.api.AbstractURLFactory;
import com.github.chaijunkun.wechat.common.api.URLBean;

/**
 * 后端接入微信接口相关URL
 * @author chaijunkun
 * @since 2016年8月26日
 */
public class TokenAPIURLFactory extends AbstractURLFactory {
	
	/** 接入令牌接口URL */
	private final URLBean token = new URLBean(true, COMMON_API_DOMAIN, "/cgi-bin/token");
	
	/**
	 * 获取接入令牌接口URL
	 * @return 接入令牌接口URL
	 */
	public String getToken() {
		return token.getAbsoluteURL();
	}

}
