package com.ajaxjs.mvc.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * 
 * @author Frank Cheung
 *
 */
public abstract class SessionValueFilter implements FilterAction {
	/**
	 * 
	 * @param request
	 * @param paramName
	 * @return
	 */
	public String getClientSideArgs(HttpServletRequest request, String paramName) {
		if (request.getParameter(paramName) == null)
			throw new NullPointerException("客户端没有提供参数： " + paramName);

		return request.getParameter(paramName);
	}

	/**
	 * 
	 * @param request
	 * @param sessionKey
	 * @return
	 */
	public String getServerSideValue(HttpServletRequest request, String sessionKey) {
		HttpSession session = request.getSession();
		Object value = session.getAttribute(sessionKey);
		
		if (value == null) {
			throw new NullPointerException("session 中找不到 对应的 key 的值， key： " + sessionKey);
		} else {
			return (String) value;
		}
	}
}
