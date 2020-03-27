package com.ajaxjs.user.filter;

import java.lang.reflect.Method;

import com.ajaxjs.mvc.ModelAndView;
import com.ajaxjs.mvc.controller.MvcOutput;
import com.ajaxjs.mvc.controller.MvcRequest;
import com.ajaxjs.mvc.filter.FilterAction;
import com.ajaxjs.mvc.filter.FilterAfterArgs;
import com.ajaxjs.user.controller.BaseUserController;

/**
 * 是否已经登录的拦截器
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
public class LoginCheck implements FilterAction {

	@Override
	public boolean before(ModelAndView model, MvcRequest request, MvcOutput response, Method method, Object[] args) {
		if (BaseUserController.isLogined(request)) {
			return true;
		} else {
			throw new IllegalAccessError("尚未登录，没有权限操作");
		}
	}

	@Override
	public boolean after(FilterAfterArgs args) {
		return true;
	}
}
