package com.ajaxjs.view;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspContext;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

public abstract class AbstractUserLogin extends SimpleTagSupport {

	public static HttpServletRequest getRequest(JspContext context) {
		PageContext pageContext = (PageContext) context;
		HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();

		return request;
	}

	/**
	 * 会员是否已经登录
	 * 
	 * @param request 请求对象
	 * @return true=會員已經登錄；fasle=未登录
	 */
	public static boolean isLogined(HttpServletRequest request) {
		return request != null && request.getSession() != null && request.getSession().getAttribute("userId") != null;
	}
}
