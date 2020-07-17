package com.ajaxjs.web.mvc.filter;

import java.lang.reflect.Method;

import com.ajaxjs.web.mvc.ModelAndView;
import com.ajaxjs.web.mvc.controller.MvcOutput;
import com.ajaxjs.web.mvc.controller.MvcRequest;

/**
 * 后置过滤器所需的参数太多了，用一个类承载
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
public class FilterAfterArgs {
	public ModelAndView model;
	public Object result;
	public MvcRequest request;
	public MvcOutput response;
	public Throwable err;
	public Method method;
	public boolean isbeforeSkip;
	public boolean isAfterSkip = false;
}
