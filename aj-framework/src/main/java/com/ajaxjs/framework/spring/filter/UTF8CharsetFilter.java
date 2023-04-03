package com.ajaxjs.framework.spring.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * 避免乱码
 * 
 * @author Frank Cheung<sp42@qq.com>
 *
 */
public class UTF8CharsetFilter implements Filter {
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	private static final String UTF8 = "UTF-8";
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		request.setCharacterEncoding(UTF8);
		response.setCharacterEncoding(UTF8);
		chain.doFilter(request, response);
	}

	@Override
	public void destroy() {
	}
}
