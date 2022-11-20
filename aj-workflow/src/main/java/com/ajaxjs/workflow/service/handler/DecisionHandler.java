package com.ajaxjs.workflow.service.handler;

import com.ajaxjs.workflow.model.Execution;

/**
 * 决策处理器接口
 *
 */
public interface DecisionHandler {
	/**
	 * 定义决策方法，实现类需要根据执行对象做处理，并返回后置流转的 name
	 * 
	 * @param execution 执行对象
	 * @return String 后置流转的name
	 */
	String decide(Execution execution);
}
