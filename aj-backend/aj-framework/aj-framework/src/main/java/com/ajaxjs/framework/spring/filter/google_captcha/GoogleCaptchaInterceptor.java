package com.ajaxjs.framework.spring.filter;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class GoogleCaptchaInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse resp, Object handler) {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();

            if (method != null) {
                String httpMethod = req.getMethod();

                if (("POST".equals(httpMethod) || "PUT".equals(httpMethod)) && method.getAnnotation(GoogleCaptchaCheck.class) != null) {
                    // 有注解，要检测
                    System.out.println("开始检测");

                    if (googleFilter.check(req))
                        return true;

                    return false;
                }
            }
        }

        return true;
    }
}
