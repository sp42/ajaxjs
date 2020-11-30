package com.ajaxjs.web.mvc.filter;

/**
 * 前置过滤器参数
 * 
 * @param model    页面数据中间件
 * @param request  请求对象
 * @param response 响应对象
 * @param method   方法对象
 * @param args     执行的参数
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
public class FilterContext extends BaseArgs {
	public Object[] args;
}
