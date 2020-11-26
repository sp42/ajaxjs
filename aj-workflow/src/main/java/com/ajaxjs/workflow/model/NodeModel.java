package com.ajaxjs.workflow.model;

import java.util.ArrayList;
import java.util.List;

import com.ajaxjs.util.CommonUtil;
import com.ajaxjs.util.ReflectUtil;
import com.ajaxjs.workflow.Execution;
import com.ajaxjs.workflow.WorkflowException;
import com.ajaxjs.workflow.WorkflowInterceptor;

/**
 * 节点元素（存在输入输出的变迁）
 * 
 */
public abstract class NodeModel extends BaseModel {
	private static final long serialVersionUID = -2377317472320109317L;

	/**
	 * 输入变迁集合
	 */
	private List<TransitionModel> inputs = new ArrayList<>();

	/**
	 * 输出变迁集合
	 */
	private List<TransitionModel> outputs = new ArrayList<>();

	/**
	 * layout
	 */
	private String layout;

	/**
	 * 局部前置拦截器
	 */
	private String preInterceptors;

	/**
	 * 局部后置拦截器
	 */
	private String postInterceptors;

	/**
	 * 前置局部拦截器实例集合
	 */
	private List<WorkflowInterceptor> preInterceptorList = new ArrayList<>();

	/**
	 * 后置局部拦截器实例集合
	 */
	private List<WorkflowInterceptor> postInterceptorList = new ArrayList<>();

	/**
	 * 具体节点模型需要完成的执行逻辑
	 * 
	 * @param execution 执行对象
	 */
	protected abstract void exec(Execution execution);

	/**
	 * 对执行逻辑增加前置、后置拦截处理
	 * 
	 * @param execution 执行对象
	 */
	public void execute(Execution execution) {
		intercept(preInterceptorList, execution);
		exec(execution);
		intercept(postInterceptorList, execution);
	}

	/**
	 * 运行变迁继续执行
	 * 
	 * @param execution 执行对象
	 */
	protected void runOutTransition(Execution execution) {
		for (TransitionModel tm : outputs) {
			tm.setEnabled(true);
			tm.execute(execution);
		}
	}

	/**
	 * 拦截方法
	 * 
	 * @param interceptorList 拦截器列表
	 * @param execution       执行对象
	 */
	public static void intercept(List<WorkflowInterceptor> interceptorList, Execution execution) {
		try {
			for (WorkflowInterceptor interceptor : interceptorList)
				interceptor.intercept(execution);
		} catch (Exception e) {
			throw new WorkflowException("拦截器执行失败", e);
		}
	}

	/**
	 * 根据父节点模型、当前节点模型判断是否可退回。可退回条件： 1、满足中间无fork、join、subprocess模型
	 * 2、满足父节点模型如果为任务模型时，参与类型为any
	 * 
	 * @param parent 父节点模型
	 * @return 是否可以退回
	 */
	public static boolean canRejected(NodeModel current, NodeModel parent) {
		if (parent instanceof TaskModel && !((TaskModel) parent).isPerformAny())
			return false;

		boolean result = false;

		for (TransitionModel tm : current.getInputs()) {
			NodeModel source = tm.getSource();

			if (source == parent)
				return true;

			if (source instanceof ForkModel || source instanceof JoinModel || source instanceof SubProcessModel || source instanceof StartModel)
				continue;

			result = result || canRejected(source, parent);
		}

		return result;
	}

	/**
	 * 获取下一步的模型
	 * 
	 * @param <T>   下一步模型的类型
	 * @param clazz 指定要匹配的类型
	 * @return 下一步模型集合
	 */
	public <T> List<T> getNextModels(Class<T> clazz) {
		List<T> models = new ArrayList<>();

		for (TransitionModel tm : getOutputs())
			addNextModels(models, tm, clazz);

		return models;
	}

	/**
	 * 查找下一步的模型
	 * 
	 * @param <T>    下一步模型的类型
	 * @param models 下一步模型集合
	 * @param tm     变迁模型
	 * @param clazz  指定要匹配的类型
	 */
	@SuppressWarnings("unchecked")
	protected static <T> void addNextModels(List<T> models, TransitionModel tm, Class<T> clazz) {
		NodeModel next = tm.getTarget();

		if (clazz.isInstance(next)) // 属于 clazz 类型的才加入到集合中
			models.add((T) next);
		else {
			for (TransitionModel tm2 : next.getOutputs()) // 递归查询
				addNextModels(models, tm2, clazz);
		}
	}

	public List<TransitionModel> getInputs() {
		return inputs;
	}

	public void setInputs(List<TransitionModel> inputs) {
		this.inputs = inputs;
	}

	public List<TransitionModel> getOutputs() {
		return outputs;
	}

	public void setOutputs(List<TransitionModel> outputs) {
		this.outputs = outputs;
	}

	public String getLayout() {
		return layout;
	}

	public void setLayout(String layout) {
		this.layout = layout;
	}

	public String getPreInterceptors() {
		return preInterceptors;
	}

	/**
	 * 
	 * @param preInterceptors
	 */
	public void setPreInterceptors(String preInterceptors) {
		if (CommonUtil.isEmptyString(preInterceptors))
			return;

		this.preInterceptors = preInterceptors;
		for (String interceptor : preInterceptors.split(",")) {
			WorkflowInterceptor instance = (WorkflowInterceptor) ReflectUtil.newInstance(interceptor);

			if (instance != null)
				preInterceptorList.add(instance);
		}
	}

	public String getPostInterceptors() {
		return postInterceptors;
	}

	/**
	 * 
	 * @param postInterceptors
	 */
	public void setPostInterceptors(String postInterceptors) {
		this.postInterceptors = postInterceptors;

		if (!CommonUtil.isEmptyString(postInterceptors)) {
			for (String interceptor : postInterceptors.split(",")) {
				WorkflowInterceptor instance = (WorkflowInterceptor) ReflectUtil.newInstance(interceptor);

				if (instance != null)
					this.postInterceptorList.add(instance);
			}
		}
	}
}
