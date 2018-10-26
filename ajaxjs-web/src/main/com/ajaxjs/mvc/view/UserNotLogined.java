package com.ajaxjs.mvc.view;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;

/**
 * 會員没有登录时
 * 
 * @author Frank Cheung
 *
 */
public class UserNotLogined extends AbstractUserLogin {
	@Override
	public void doTag() throws JspException, IOException {
		HttpServletRequest request = getRequest(getJspContext());

		if (!isLogined(request))
			getJspBody().invoke(null);
	}
}
