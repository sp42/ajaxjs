package com.ajaxjs.user.common.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * 用户登录业务
 * 
 * @author Frank Cheung<sp42@qq.com>
 *
 */
public interface ILoginService extends IBaseUserService {
	/**
	 * 用户登录
	 * 
	 * TODO 是否需要考虑 CaptchaFilter
	 * 
	 * @param userID   用户标识，可以是 username/email/phone 中的一种，后台自动判断
	 * @param password 用户密码
	 * @param tenantId 租户 id
	 * @param req      请求对象
	 * @return true 表示登录成功
	 */
	Boolean login(String userID, String password, int tenantId, HttpServletRequest req);

	/**
	 * 用户登出
	 * 
	 * @param session 会话对象
	 * @return true 表示登出成功
	 */
	Boolean logout(HttpSession session);

	/**
	 * 查看登录日志
	 * 
	 * @param start
	 * @param limit
	 * @return
	 */
	String listLog(int start, int limit);
}
