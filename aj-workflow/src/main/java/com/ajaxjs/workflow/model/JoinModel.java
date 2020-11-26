package com.ajaxjs.workflow.model;

import java.util.List;

import com.ajaxjs.workflow.Execution;

/**
 * 合并定义 join 元素
 * 
 */
public class JoinModel extends NodeModel {
	private static final long serialVersionUID = 5296621319088076775L;

	@Override
	public void exec(Execution execution) {
		JoinModel model = this;

		// 合并分支操作的处理器
		(new AbstractMergeHandler() {
			@Override
			protected String[] findActiveNodes() {
				StringBuilder sb = new StringBuilder(20);
				findForkTaskNames(model, sb);

				return sb.toString().split(",");
			}
		}).handle(execution);

		// 如果已经合并成功，则进行下一步变迁
		if (execution.isMerged())
			runOutTransition(execution);
	}

	/**
	 * 对 join 节点的所有输入变迁进行递归，查找 join 至 fork 节点的所有中间 task 元素。这是一个递归的函数
	 * 
	 * @param node 节点模型
	 * @param sb   记录 task 用的字符串
	 */
	private static void findForkTaskNames(NodeModel node, StringBuilder sb) {
		if (node instanceof ForkModel)
			return;

		List<TransitionModel> inputs = node.getInputs();

		for (TransitionModel tm : inputs) {
			if (tm.getSource() instanceof WorkModel)
				sb.append(tm.getSource().getName()).append(",");

			findForkTaskNames(tm.getSource(), sb);
		}
	}
}
