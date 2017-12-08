package com.ajaxjs.security.wrapper;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ajaxjs.security.SecurityInit;
import com.ajaxjs.util.aop.Aop;
import com.ajaxjs.util.aop.ReturnAsArg;

public class CookieChecker extends Aop<SecurityInit> {

	@Override
	protected Object before(Method method, Object[] args) throws Throwable {
		if (method.getName().equals("initRequest")) {
			HttpServletRequest request = (HttpServletRequest) args[0];
			
			return new ReturnAsArg(new CookieRequest(request));
		}

		if (method.getName().equals("initResponse")) {
			HttpServletResponse response = (HttpServletResponse) args[0];
			return new ReturnAsArg(new CookieResponse(response));
		}
		return null;
	}

	@Override
	protected void after(Method method, Object[] args, Object returnObj) {
		// TODO Auto-generated method stub
		
	}

}
