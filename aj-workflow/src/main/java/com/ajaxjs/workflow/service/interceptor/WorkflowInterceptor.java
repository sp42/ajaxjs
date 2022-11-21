package com.ajaxjs.workflow.service.interceptor;

import com.ajaxjs.workflow.model.Execution;

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
	 * @param exec 执行对象。可从中获取执行的数据
	 */
	public void intercept(Execution exec);
}
