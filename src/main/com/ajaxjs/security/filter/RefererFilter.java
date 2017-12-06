package com.ajaxjs.security.filter;

import java.lang.reflect.Method;

import javax.servlet.Filter;
import javax.servlet.http.HttpServletRequest;

import com.ajaxjs.util.StringUtil;
import com.ajaxjs.util.aop.Aop;

public class RefererFilter extends Aop<Filter> {

	@Override
	protected Object before(Method method, Object[] args) throws Throwable {
		if (method.getName().equals("doFilter")) {
			
			System.out.println("sdfsds111");
			HttpServletRequest request = (HttpServletRequest) args[0];
			if (!checkIt(request))
				throw new SecurityException("来路不对");
		}

		return null;
	}

	@Override
	protected void after(Method method, Object[] args, Object returnObj) {
	}

	public static boolean checkIt(HttpServletRequest request) {
		String referer = request.getHeader("referer");

		if (StringUtil.isEmptyString(referer))
			return false; // 请求没有 referer 字段不通过

		return referer.startsWith(request.getServerName());
	}
}
