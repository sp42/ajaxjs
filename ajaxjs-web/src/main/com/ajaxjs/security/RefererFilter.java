package com.ajaxjs.security;

import java.lang.reflect.Method;

import com.ajaxjs.mvc.controller.MvcOutput;
import com.ajaxjs.mvc.controller.MvcRequest;
import com.ajaxjs.mvc.filter.FilterAction;
import com.ajaxjs.util.CommonUtil;

/**
 * Referer 来路检测
 * 
 * @author Frank Cheung
 *
 */
public class RefererFilter implements FilterAction {
	@Override
	public boolean before(MvcRequest request, MvcOutput response, Method method) {
		String referer = request.getHeader("referer");

		if (CommonUtil.isEmptyString(referer))
			throw new SecurityException("请求没有 referer 字段不通过");

		return referer.startsWith(request.getServerName());
	}

	@Override
	public void after(MvcRequest request, MvcOutput response, Method method, boolean isSkip) {

	}
}
