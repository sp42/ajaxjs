package com.ajaxjs.security.referer;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;

import javax.servlet.Filter;
import com.ajaxjs.util.aop.Aop;

public class PostFilter extends Aop<Filter> {

	@Override
	protected Object before(Method method, Object[] args) throws Throwable {
		if (method.getName().equals("doFilter")) {
			HttpServletRequest request = (HttpServletRequest) args[0];
			System.out.println("sdfsds2222");
			if (!checkIt(request))
				throw new SecurityException("禁止 POST");
		}

		return null;
	}

	@Override
	protected void after(Method method, Object[] args, Object returnObj) {
	}

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
}
