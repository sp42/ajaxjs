package com.ajaxjs.web.security;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 安装安全 request、response
 */
@WebFilter("/*")
public class InstallFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        SecurityRequest securityRequest = new SecurityRequest(httpRequest);
        SecurityResponse securityResponse = new SecurityResponse((HttpServletResponse) response);

        Object obj = httpRequest.getServletContext().getAttribute("");

        if (obj != null) {

        }

        chain.doFilter(securityRequest, securityResponse);// 继续处理请求
    }

    @Override
    public void destroy() {
    }
}
