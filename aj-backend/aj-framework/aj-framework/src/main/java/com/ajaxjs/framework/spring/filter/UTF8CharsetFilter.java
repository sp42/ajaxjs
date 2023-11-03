package com.ajaxjs.framework.spring.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import com.ajaxjs.util.StrUtil;

/**
 * 避免乱码
 *
 * @author Frank Cheung<sp42@qq.com>
 */
public class UTF8CharsetFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        request.setCharacterEncoding(StrUtil.UTF8_SYMBOL);
        response.setCharacterEncoding(StrUtil.UTF8_SYMBOL);
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    }
}
