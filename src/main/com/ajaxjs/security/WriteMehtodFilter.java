package com.ajaxjs.security;

import java.lang.reflect.Method;

import com.ajaxjs.mvc.controller.MvcOutput;
import com.ajaxjs.mvc.controller.MvcRequest;
import com.ajaxjs.mvc.filter.FilterAction;

/**
 * HTTP 中的 POST、PUT、DELETE 都是写入的方法，这里对其检测
 * 
 * @author Frank Cheung
 *
 */
public class WriteMehtodFilter implements FilterAction {

	@Override
	public boolean before(MvcRequest request, MvcOutput response, Method method) {
		if (!"GET".equalsIgnoreCase(request.getMethod())) {
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
	public void after(MvcRequest request, MvcOutput response, Method method, boolean isSkip) {

	}

}
