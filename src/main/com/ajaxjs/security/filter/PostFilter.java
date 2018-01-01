package com.ajaxjs.security.filter;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;

import javax.servlet.Filter;
import com.ajaxjs.util.aop.Aop;

public class PostFilter extends Aop<Filter> {

	public static boolean checkIt(HttpServletRequest request) {
		if (!"POST".equalsIgnoreCase(request.getMethod())) {
			return true;
		}

		String uri = request.getRequestURI();

		//		if(!isInWhiteList(uri) && !isInBlackList(uri)){
		//			return true;
		//		}
		//		if(isInWhiteList(uri))
		//			return true;
		//		if(isInBlackList(uri))
		//			return false;	

		return true; // 没有任何信息则通过
	}

	@Override
	protected Object before(Filter target, Method method, String methodName, Object[] args) throws Throwable {
		if (methodName.equals("doFilter")) {
			HttpServletRequest request = (HttpServletRequest) args[0];
			if (!checkIt(request))
				throw new SecurityException("禁止 POST");
		}

		return null;
	}

	@Override
	protected void after(Filter target, Method method, String methodName, Object[] args, Object returnObj) {
		// TODO Auto-generated method stub

	}
}
