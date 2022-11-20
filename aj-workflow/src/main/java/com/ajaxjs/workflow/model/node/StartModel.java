package com.ajaxjs.workflow.model.node;

import java.util.Collections;
import java.util.List;

import com.ajaxjs.workflow.model.Execution;
import com.ajaxjs.workflow.model.TransitionModel;

/**
 * 开始节点定义 start 元素
 */
public class StartModel extends NodeModel {
	private static final long serialVersionUID = -4550530562581330477L;

	/**
	 * 开始节点无输入变迁
	 */
	@Override
	public List<TransitionModel> getInputs() {
		return Collections.emptyList();
	}

	@Override
	protected void exec(Execution execution) {
		runOutTransition(execution);
	}
}
