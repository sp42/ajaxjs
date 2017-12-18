/**
 * Copyright 2015 Sp42 frank@ajaxjs.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ajaxjs.security;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import com.ajaxjs.security.filter.PostFilter;
import com.ajaxjs.security.filter.RefererFilter;
import com.ajaxjs.security.wrapper.CookieChecker;
import com.ajaxjs.security.wrapper.XssChecker;
import com.ajaxjs.security.wrapper.XssReqeust;
import com.ajaxjs.util.aop.Aop;

public class SecurityFilter implements Filter {
	@Override
	public void init(FilterConfig arg0) throws ServletException {
	}
	static SecurityMgr securityMgr = new SecurityMgr();
	static Filter simpleChecker = Aop.chain(securityMgr, new RefererFilter(), new PostFilter());
	static SecurityInit init = Aop.chain(securityMgr, new XssChecker(), new CookieChecker());

	@Override
	public void doFilter(ServletRequest arg0, ServletResponse arg1, FilterChain chain)
			throws IOException, ServletException {
		try {
			simpleChecker.doFilter(arg0, arg1, chain);
		} catch (SecurityException e) {
			System.out.println(":::::" + e);

			return; // 中止链式调用
		}
		System.out.println(init.initRequest(arg0) instanceof XssReqeust);
		chain.doFilter(init.initRequest(arg0), init.initResponse(arg1));
		XssReqeust request = (XssReqeust)init.initRequest(arg0);
		System.out.println(request.getParameter("foo"));
	}

	@Override
	public void destroy() {
	}
}
