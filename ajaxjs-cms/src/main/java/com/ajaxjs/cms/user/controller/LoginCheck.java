package com.ajaxjs.cms.user.controller;

import java.lang.reflect.Method;

import com.ajaxjs.mvc.controller.MvcOutput;
import com.ajaxjs.mvc.controller.MvcRequest;
import com.ajaxjs.mvc.filter.FilterAction;

/**
 * 是否已经登录的拦截器
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
public class LoginCheck implements FilterAction {

	@Override
	public boolean before(MvcRequest request, MvcOutput response, Method method) {
		if (BaseUserController.isLogined(request)) {
			return true;
		} else {
			throw new IllegalAccessError("尚未登录，没有权限操作");
		}
	}

	@Override
	public void after(MvcRequest request, MvcOutput response, Method method, boolean isSkip) {
	}
}
