package com.ajaxjs.web.mvc.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * 存取 Session 中的值的基类
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
public abstract class SessionValueFilter implements FilterAction {
	/**
	 * 获取客户端提交过来的验证码参数
	 * 
	 * @param request   请求对象
	 * @param paramName 参数名称
	 * @return 找到的参数值，若找不到则抛出非法参数的异常
	 */
	public String getClientSideArgs(HttpServletRequest request, String paramName) {
		if (request.getParameter(paramName) == null)
			throw new IllegalArgumentException("客户端没有提供参数： " + paramName);

		return request.getParameter(paramName).trim();
	}

	/**
	 * 获取服务端的 Session 中的值
	 * 
	 * @param request    请求对象
	 * @param sessionKey Session 健名称
	 * @return 找到的 Session 值，若找不到则抛出空指针异常
	 */
	public String getServerSideValue(HttpServletRequest request, String sessionKey) {
		HttpSession session = request.getSession();
		Object value = session.getAttribute(sessionKey);

		if (value == null) 
			throw new NullPointerException("Session 中找不到 对应的 key 的值， key： " + sessionKey);
		 else 
			return (String) value;
	}
}
