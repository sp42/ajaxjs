package com.ajaxjs.iam.server.config;

import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//public class CrossFilter extends OncePerRequestFilter {
//    @Override
//    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain filterChain) throws ServletException, IOException {
//        String origin = req.getHeader("Origin");
//
//        if (!org.springframework.util.StringUtils.isEmpty(origin))
//            res.addHeader("Access-Control-Allow-Origin", origin);
//
//        res.addHeader("Access-Control-Allow-Methods", "*");
//        res.addHeader("Access-Control-Allow-Credentials", "true");
//        res.addHeader("Access-Control-Allow-Headers", "*");
//
//        filterChain.doFilter(req, res);
//    }
//}

@WebFilter(urlPatterns = "/*")
public class CrossFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse res = (HttpServletResponse) response;
        // 获取请求来源
        String origin = request.getHeader("Origin");

        // 允许跨域请求
        if (origin != null) {
        }
        res.addHeader("Access-Control-Allow-Origin", "*");
        res.addHeader("Access-Control-Allow-Methods", "*");
        res.addHeader("Access-Control-Allow-Credentials", "true");
        res.addHeader("Access-Control-Allow-Headers", "*");

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }
}