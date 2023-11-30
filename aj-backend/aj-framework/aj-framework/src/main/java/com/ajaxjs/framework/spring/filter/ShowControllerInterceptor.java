package com.ajaxjs.framework.spring.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Map;

/**
 * 获得 Controller 方法名、请求参数和注解信息
 *
 * @author Frank Cheung sp42@qq.com
 */
@Slf4j
public class ShowControllerInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (handler instanceof HandlerMethod) {
            HandlerMethod h = (HandlerMethod) handler;
            log.info("控制器方法：" + h);
            log.info("请求 URL：" + request.getRequestURL());

            StringBuilder s = new StringBuilder();
            Map<String, String[]> parameterMap = request.getParameterMap();

            if (parameterMap.size() > 0) {
                for (String key : parameterMap.keySet())
                    s.append(key).append("=").append(Arrays.toString(parameterMap.get(key))).append("\n");

                log.info(request.getMethod() + "请求参数：\n" + s);
            }
        }

        return true;
    }
}
