package com.ajaxjs.user.sso.service;

import javax.servlet.http.HttpServletRequest;

/**
 * 获取用户是否登录、Token 是否合法
 * 
 * @author Frank Cheung sp42@qq.com
 *
 */
public interface IStateService extends IBaseService {
	/**
	 * 查询用户是否已经登陆
	 * 
	 * @param req
	 * @return true 表示已经登录
	 */
	Boolean isLogined(HttpServletRequest req);

	/**
	 * 检查 Access Token 是否已经失效
	 * 
	 * @param access_token 访问令牌
	 * @return
	 */
	Boolean verify(String access_token);
}
