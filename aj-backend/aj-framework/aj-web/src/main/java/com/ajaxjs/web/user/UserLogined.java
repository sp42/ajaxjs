package com.ajaxjs.web.user;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import java.io.IOException;

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
