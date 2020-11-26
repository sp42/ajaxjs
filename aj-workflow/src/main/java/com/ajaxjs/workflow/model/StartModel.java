
package com.ajaxjs.workflow.model;

import java.util.Collections;
import java.util.List;

import com.ajaxjs.workflow.Execution;

/**
 * 开始节点的定义，即 start 元素
 * 
 * @author sp42 frank@ajaxjs.com
 *
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
