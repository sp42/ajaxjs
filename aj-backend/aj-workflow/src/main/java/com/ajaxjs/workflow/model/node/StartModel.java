package com.ajaxjs.workflow.model.node;

import java.util.Collections;
import java.util.List;

import com.ajaxjs.workflow.model.Execution;
import com.ajaxjs.workflow.model.TransitionModel;

/**
 * 开始节点 start 元素
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

	/**
	 * 执行相应的输出过渡。
	 * <p>此方法覆盖了基类的exec方法，专门用于执行输出过渡操作。当调用此方法时，会触发执行所传入的Execution对象中的输出过渡逻辑。</p>
	 *
	 * @param exec 执行对象，包含了执行输出过渡所需的全部信息。
	 */
	@Override
	protected void exec(Execution exec) {
	    // 执行输出过渡
	    runOutTransition(exec);
	}
}
