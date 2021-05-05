package com.ajaxjs.user.view;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;

/**
 * 會員已经登录时
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
public class UserLogined extends AbstractUserLogin {
	@Override
	public void doTag() throws JspException, IOException {
		HttpServletRequest request = getRequest(getJspContext());

		if (isLogined(request))
			getJspBody().invoke(null);
	}
}