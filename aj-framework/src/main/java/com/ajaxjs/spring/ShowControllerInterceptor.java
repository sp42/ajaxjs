package com.ajaxjs.spring;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import com.ajaxjs.util.logger.LogHelper;

/**
 * 获得Controller方法名和注解信息
 * 
 * @author Frank Cheung sp42@qq.com
 *
 */
public class ShowControllerInterceptor implements HandlerInterceptor {
	private static final LogHelper LOGGER = LogHelper.getLog(ShowControllerInterceptor.class);

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
		if (handler instanceof HandlerMethod) {
			HandlerMethod h = (HandlerMethod) handler;

			LOGGER.info(h);
		}

		return true;
	}
}
