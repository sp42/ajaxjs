package com.ajaxjs.web.mvc.filter;

/**
 * 后置过滤器所需的参数太多了，用一个类承载
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
public class FilterAfterArgs extends BaseArgs {
	public Object result;
	public Throwable err;
	public boolean isbeforeSkip;
	public boolean isAfterSkip = false;
}
