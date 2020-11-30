package com.ajaxjs.workflow;

/**
 * 任务拦截器，对产生的任务结果进行拦截
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
public interface WorkflowInterceptor {
	/**
	 * 拦截方法
	 * 
	 * @param execution 执行对象。可从中获取执行的数据
	 */
	public void intercept(Execution execution);
}