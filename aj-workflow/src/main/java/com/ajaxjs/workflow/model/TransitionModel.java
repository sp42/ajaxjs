package com.ajaxjs.workflow.model;

import com.ajaxjs.util.ioc.ComponentMgr;
import com.ajaxjs.workflow.Execution;
import com.ajaxjs.workflow.WorkflowInterceptor;

/**
 * 变迁定义 transition 元素
 * 
 */
public class TransitionModel extends BaseModel {
	private static final long serialVersionUID = 3688123410411321158L;

	/**
	 * 变迁的源节点引用
	 */
	private NodeModel source;

	/**
	 * 变迁的目标节点引用
	 */
	private NodeModel target;

	/**
	 * 变迁的目标节点 name 名称
	 */
	private String to;

	/**
	 * 变迁的条件表达式，用于 decision
	 */
	private String expr;

	/**
	 * 转折点图形数据
	 */
	private String g;

	/**
	 * 描述便宜位置
	 */
	private String offset;

	/**
	 * 当前变迁路径是否可用
	 */
	private boolean enabled = false;

	/**
	 * 执行下一步
	 * 
	 * @param execution 执行对象
	 */
	public void execute(Execution execution) {
		if (!enabled)
			return;

		if (target instanceof TaskModel) {// 如果目标节点模型为 TaskModel 则创建 task
			execution.addTasks(execution.getEngine().task().createTask((TaskModel) target, execution));
			// 从服务上下文中查找任务拦截器列表，依次对 task 集合进行拦截处理
			NodeModel.intercept(ComponentMgr.getAllByInterface(WorkflowInterceptor.class), execution);
		} else if (target instanceof SubProcessModel)// 如果目标节点模型为 SubProcessModel 则启动子流程
			((SubProcessModel) target).handle(execution);
		else
			target.execute(execution);// 如果目标节点模型为其它控制类型，则继续由目标节点执行
	}

	public NodeModel getSource() {
		return source;
	}

	public void setSource(NodeModel source) {
		this.source = source;
	}

	public NodeModel getTarget() {
		return target;
	}

	public void setTarget(NodeModel target) {
		this.target = target;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String getExpr() {
		return expr;
	}

	public void setExpr(String expr) {
		this.expr = expr;
	}

	public String getG() {
		return g;
	}

	public void setG(String g) {
		this.g = g;
	}

	public String getOffset() {
		return offset;
	}

	public void setOffset(String offset) {
		this.offset = offset;
	}
}
