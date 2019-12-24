package com.ajaxjs.mvc.filter;

import java.lang.reflect.Method;

import com.ajaxjs.mvc.ModelAndView;
import com.ajaxjs.mvc.controller.MvcOutput;
import com.ajaxjs.mvc.controller.MvcRequest;

public class XslMaker implements FilterAction  {

	@Override
	public boolean before(ModelAndView model, MvcRequest request, MvcOutput response, Method method, Object[] args) {
		if(request.getParameter("downloadXSL") != null) {
			// 通常是第一和第二的分页参数
			args[0] = 0;
			args[1] = 999999;
		}
		
		return true;
	}

	@Override
	public void after(ModelAndView model, MvcRequest request, MvcOutput response, Method method, boolean isSkip) {
		if(request.getParameter("downloadXSL") != null) {
			
		}
		
	}

}
