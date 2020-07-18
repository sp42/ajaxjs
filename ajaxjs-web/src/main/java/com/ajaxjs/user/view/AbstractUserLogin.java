package com.ajaxjs.user.view;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspContext;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

/**
 * 会员标签的抽象类
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
public abstract class AbstractUserLogin extends SimpleTagSupport {
	/**
	 * 标签获取请求对象的方法
	 * 
	 * @param context JSP 上下文对象
	 * @return 请求对象
	 */
	public static HttpServletRequest getRequest(JspContext context) {
		PageContext pageContext = (PageContext) context;

		return (HttpServletRequest) pageContext.getRequest();
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
