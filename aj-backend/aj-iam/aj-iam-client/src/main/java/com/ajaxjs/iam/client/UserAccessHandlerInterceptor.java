package com.ajaxjs.iam.client;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

public class UserAccessHandlerInterceptor implements HandlerInterceptor {
	public static final String USER_IN_SESSION = "USER";

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		if (handler instanceof HandlerMethod) {
			Method method = ((HandlerMethod) handler).getMethod();

			if (method.getAnnotation(NeedsUserLogined.class) != null) {
				HttpSession session = request.getSession();
				boolean logined = session.getAttribute(USER_IN_SESSION) != null;

				if (!logined) {
					// 用户未登录，返回401未授权的消息
					response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
					response.setContentType("text/plain; charset=UTF-8");
					response.setCharacterEncoding("UTF-8");
					response.getWriter().println("未登录，请先进行登录");
				}

				return logined;
			}
		}

		return true;
	}
}
