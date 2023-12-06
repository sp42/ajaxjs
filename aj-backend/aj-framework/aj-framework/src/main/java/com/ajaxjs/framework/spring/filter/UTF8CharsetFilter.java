package com.ajaxjs.framework.spring.filter;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import com.ajaxjs.util.StrUtil;
import com.ajaxjs.util.convert.ConvertBasicValue;
import com.ajaxjs.util.convert.MapTool;
import com.ajaxjs.util.io.StreamHelper;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

/**
 * 避免乱码
 *
 * @author Frank Cheung<sp42@qq.com>
 */
public class UTF8CharsetFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) {
    }

    static String PUT = "PUT";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        request.setCharacterEncoding(StrUtil.UTF8_SYMBOL);
        response.setCharacterEncoding(StrUtil.UTF8_SYMBOL);

        chain.doFilter(new GetPutData((HttpServletRequest) request), response);
    }

    @Override
    public void destroy() {
    }
}
