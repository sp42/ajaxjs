package com.ajaxjs.web.user;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import java.io.IOException;

/**
 * 會員没有登录时
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
public class UserNotLogined extends SimpleTagSupport {
	@Override
	public void doTag() throws JspException, IOException {
		HttpServletRequest request = UserLogined.getRequest(getJspContext());

		if (!UserLogined.isLogined(request))
			getJspBody().invoke(null);
	}
}
