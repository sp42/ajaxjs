package com.ajaxjs.security.xss;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ajaxjs.security.SecurityInit;
import com.ajaxjs.util.aop.Aop;
import com.ajaxjs.util.aop.ReturnAsArg;

public class XssChecker extends Aop<SecurityInit> {
	@Override
	protected Object before(Method method, Object[] args) throws Throwable {
		if (method.getName().equals("initRequest")) {
			HttpServletRequest request = (HttpServletRequest) args[0];
			return new ReturnAsArg(new XssReqeust(request));
		}
		
		if (method.getName().equals("initResponse")) {
			HttpServletResponse response = (HttpServletResponse) args[0];
			return new ReturnAsArg(new XssResponse(response));
		}
		
		return null;
	}

	@Override
	protected void after(Method method, Object[] args, Object returnObj) {
		
	}

}
