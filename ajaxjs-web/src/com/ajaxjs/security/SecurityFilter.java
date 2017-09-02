package com.ajaxjs.security;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import com.ajaxjs.security.referer.PostFilter;
import com.ajaxjs.security.referer.RefererFilter;
import com.ajaxjs.security.xss.XssChecker;
import com.ajaxjs.security.xss.XssReqeust;
import com.ajaxjs.util.aop.Aop;
import com.ajaxjs.web.security.wrapper.XSS_Response;

public class SecurityFilter implements Filter {
	@Override
	public void init(FilterConfig arg0) throws ServletException {
	}

	static Filter simpleChecker = Aop.chain(new SecurityMgr(), new RefererFilter(), new PostFilter());
	static SecurityInit init = Aop.chain(new SecurityMgr(), new XssChecker());

	@Override
	public void doFilter(ServletRequest arg0, ServletResponse arg1, FilterChain chain) throws IOException, ServletException {
		try {
			simpleChecker.doFilter(arg0, arg1, chain);
		} catch (SecurityException e) {
			System.out.println(":::::" + e);

			return; // 中止链式调用
		}
		System.out.println(init.initRequest(arg0) instanceof XssReqeust);
		chain.doFilter(init.initRequest(arg0), init.initResponse(arg1));

	}

	@Override
	public void destroy() {
	}
}
