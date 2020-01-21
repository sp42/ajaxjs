package com.ajaxjs.workflow.model;

import java.util.ArrayList;
import java.util.List;

import com.ajaxjs.util.CommonUtil;
import com.ajaxjs.util.ReflectUtil;
import com.ajaxjs.util.logger.LogHelper;
import com.ajaxjs.workflow.Execution;

import com.ajaxjs.workflow.WorkflowException;
import com.ajaxjs.workflow.interceptor.SnakerInterceptor;
/**
 * 节点元素（存在输入输出的变迁）
 * 
 */
public abstract class NodeModel extends BaseWfModel implements Action {
	private static final long serialVersionUID = -2377317472320109317L;

	public static final LogHelper LOGGER = LogHelper.getLog(NodeModel.class);

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
	private List<SnakerInterceptor> preInterceptorList = new ArrayList<>();

	/**
	 * 后置局部拦截器实例集合
	 */
	private List<SnakerInterceptor> postInterceptorList = new ArrayList<>();

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
	@Override
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
		for (TransitionModel tm : getOutputs()) {
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
	private void intercept(List<SnakerInterceptor> interceptorList, Execution execution) {
		try {
			for (SnakerInterceptor interceptor : interceptorList)
				interceptor.intercept(execution);
		} catch (Exception e) {
			LOGGER.warning("拦截器执行失败=" + e.getMessage());
			throw new WorkflowException(e);
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

			if (source instanceof ForkModel || source instanceof JoinModel || source instanceof SubProcessModel
					|| source instanceof StartModel)
				continue;

			result = result || canRejected(source, parent);
		}
		return result;
	}

	public <T> List<T> getNextModels(Class<T> clazz) {
		List<T> models = new ArrayList<>();
		
		for (TransitionModel tm : this.getOutputs())
			addNextModels(models, tm, clazz);

		return models;
	}

	@SuppressWarnings("unchecked")
	protected <T> void addNextModels(List<T> models, TransitionModel tm, Class<T> clazz) {
		if (clazz.isInstance(tm.getTarget())) {
			models.add((T) tm.getTarget());
		} else {
			for (TransitionModel tm2 : tm.getTarget().getOutputs())
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

	public void setPreInterceptors(String preInterceptors) {
		this.preInterceptors = preInterceptors;

		if (!CommonUtil.isEmptyString(preInterceptors)) {
			for (String interceptor : preInterceptors.split(",")) {
				SnakerInterceptor instance = (SnakerInterceptor) ReflectUtil.newInstance(interceptor);

				if (instance != null)
					this.preInterceptorList.add(instance);
			}
		}
	}

	public String getPostInterceptors() {
		return postInterceptors;
	}

	public void setPostInterceptors(String postInterceptors) {
		this.postInterceptors = postInterceptors;

		if (!CommonUtil.isEmptyString(postInterceptors)) {
			for (String interceptor : postInterceptors.split(",")) {
				SnakerInterceptor instance = (SnakerInterceptor) ReflectUtil.newInstance(interceptor);

				if (instance != null)
					this.postInterceptorList.add(instance);
			}
		}
	}
}
