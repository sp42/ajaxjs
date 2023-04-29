package com.ajaxjs.workflow.model.node;

import com.ajaxjs.workflow.model.Execution;

/**
 * 分支定义 fork 元素
 */
public class ForkModel extends NodeModel {
	private static final long serialVersionUID = 2030281774771653617L;

	@Override
	protected void exec(Execution execution) {
		runOutTransition(execution);
	}
}
