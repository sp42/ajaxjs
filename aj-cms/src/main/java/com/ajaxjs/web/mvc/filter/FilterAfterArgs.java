package com.ajaxjs.web.mvc.filter;

/**
 * 后置过滤器所需的参数太多了，用一个类承载
 * 
 * @param model    页面数据中间件
 * @param request  请求对象
 * @param response 响应对象
 * @param method   方法对象
 * @param isSkip   是否已经中止控制器方法的执行，也就是 before() 返回的值
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
