package com.ajaxjs.web.mvc.filter;

import java.lang.reflect.Method;

import com.ajaxjs.web.mvc.ModelAndView;
import com.ajaxjs.web.mvc.controller.MvcOutput;
import com.ajaxjs.web.mvc.controller.MvcRequest;

/**
 * 
 * 过滤器上下文
 * 
 * 试想象下，如果一个方法带有 n 个参数，会很麻烦，于是用一个类承载这些参数。
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
public abstract class BaseArgs {
	public ModelAndView model;
	public MvcRequest request;
	public MvcOutput response;
	public Method method;
}
