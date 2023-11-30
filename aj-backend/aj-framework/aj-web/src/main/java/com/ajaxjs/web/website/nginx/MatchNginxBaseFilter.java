package com.ajaxjs.web.website.nginx;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

public abstract class MatchNginxBaseFilter implements Filter {
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		MatchNginxRequestWrapper paramsRequest = new MatchNginxRequestWrapper((HttpServletRequest) request);

		chain.doFilter(paramsRequest, response);
	}

	@Override
	public void init(FilterConfig arg0) {
	}

	@Override
	public void destroy() {
	}
}
