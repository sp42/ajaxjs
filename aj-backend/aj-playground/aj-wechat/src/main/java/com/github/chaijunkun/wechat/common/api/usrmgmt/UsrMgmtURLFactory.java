package com.github.chaijunkun.wechat.common.api.usrmgmt;

import com.github.chaijunkun.wechat.common.api.AbstractURLFactory;
import com.github.chaijunkun.wechat.common.api.URLBean;

/**
 * 用户管理接口相关URL
 * @author chaijunkun
 * @since 2016年9月6日
 */
public class UsrMgmtURLFactory extends AbstractURLFactory {
	
	/** 用户基本信息接口URL */
	private static final URLBean userInfo = new URLBean(true, COMMON_API_DOMAIN, "/cgi-bin/user/info");

	/**
	 * 获取用户基本信息接口URL
	 * @return 用户基本信息接口URL
	 */
	public String getUserInfo() {
		return userInfo.getAbsoluteURL();
	}
	
}
