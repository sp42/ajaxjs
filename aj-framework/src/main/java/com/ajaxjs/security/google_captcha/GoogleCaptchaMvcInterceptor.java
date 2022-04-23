package com.ajaxjs.security.google_captcha;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 验证码拦截器
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
public class GoogleCaptchaMvcInterceptor implements HandlerInterceptor {
	@Autowired
	private GoogleFilter googleFilter;

//	@Autowired
//	private EasyConfig easyCfg;

	@Override
	public boolean preHandle(HttpServletRequest req, HttpServletResponse resp, Object handler) {
//		if (handler instanceof HandlerMethod && easyCfg.getBol("webSecurity_isCaptcha")) {
		if (handler instanceof HandlerMethod) {
			HandlerMethod handlerMethod = (HandlerMethod) handler;
			Method method = handlerMethod.getMethod();

			if (method != null) {
				String httpMethod = req.getMethod();

				if (("POST".equals(httpMethod) || "PUT".equals(httpMethod)) && method.getAnnotation(GoogleCaptchaCheck.class) != null) {
					// 有注解，要检测
					if (googleFilter.check(req))
						return true;

					return false;
				}
			}
		}

		return true;
	}
}
