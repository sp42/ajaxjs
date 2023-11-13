package com.ajaxjs.web.user;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import java.io.IOException;

/**
 * 會員已经登录时
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
public class UserLogined extends SimpleTagSupport {
	public static final String USER_IN_SESSION = "USER";
	
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
	 * @return true=會員已經登錄；false=未登录
	 */
	public static boolean isLogined(HttpServletRequest request) {
		return request != null && request.getSession() != null && request.getSession().getAttribute(USER_IN_SESSION) != null;
	}
	
	@Override
	public void doTag() throws JspException, IOException {
		HttpServletRequest request = getRequest(getJspContext());

		if (isLogined(request)) 
			getJspBody().invoke(null);
	}
}
