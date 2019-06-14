package com.ajaxjs.mvc;

import java.lang.reflect.Method;

import com.ajaxjs.mvc.controller.MvcOutput;
import com.ajaxjs.mvc.controller.MvcRequest;
import com.ajaxjs.mvc.filter.FilterAction;

public class Filter implements FilterAction {

	@Override
	public boolean before(MvcRequest request, MvcOutput response, Method method) {
		return true;
	}

	@Override
	public void after(MvcRequest request, MvcOutput response, Method method, boolean isSkip) {

	}

}
