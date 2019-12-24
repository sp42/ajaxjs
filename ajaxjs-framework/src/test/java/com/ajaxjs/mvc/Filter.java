package com.ajaxjs.mvc;

import java.lang.reflect.Method;

import com.ajaxjs.mvc.controller.MvcOutput;
import com.ajaxjs.mvc.controller.MvcRequest;
import com.ajaxjs.mvc.filter.FilterAction;

public class Filter implements FilterAction {

	@Override
	public boolean before(ModelAndView model, MvcRequest request, MvcOutput response, Method method, Object[] args) {
		return true;
	}

	@Override
	public void after(ModelAndView model, MvcRequest request, MvcOutput response, Method method, boolean isSkip) {

	}

}
