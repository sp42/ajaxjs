package com.ajaxjs.workflow.model.node;

import com.ajaxjs.workflow.model.Execution;

/**
 * 分支定义 fork 元素
 */
public class ForkModel extends NodeModel {
	private static final long serialVersionUID = 2030281774771653617L;

	/**
	 * 执行外部转换。
	 * 该方法覆盖了基类中的exec方法，专门用于执行输出转换操作。
	 *
	 * @param execution 执行对象，包含执行所需的所有信息和上下文。
	 */
	@Override
	protected void exec(Execution execution) {
	    // 执行输出转换
	    runOutTransition(execution);
	}
}
