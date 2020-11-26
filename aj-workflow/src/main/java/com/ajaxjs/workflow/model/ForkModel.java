package com.ajaxjs.workflow.model;

import com.ajaxjs.workflow.Execution;

/**
 * 分支定义fork元素
 */
public class ForkModel extends NodeModel {
	private static final long serialVersionUID = 2030281774771653617L;

	@Override
	protected void exec(Execution execution) {
		runOutTransition(execution);
	}
}
