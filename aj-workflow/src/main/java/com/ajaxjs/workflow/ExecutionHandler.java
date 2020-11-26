package com.ajaxjs.workflow;

/**
 * 执行回调。可用在：决策处理器接口 DecisionModel、自定义节点 CustomModel、编码设置参与者
 *
 */
public interface ExecutionHandler {
	/**
	 * 定义决策方法，实现类需要根据执行对象做处理，
	 * 
	 * @param execution 执行对象
	 * @return Object 返回任意值
	 */
	public Object exec(Execution execution);
}
