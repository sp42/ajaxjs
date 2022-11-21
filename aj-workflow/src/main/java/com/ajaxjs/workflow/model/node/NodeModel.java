package com.ajaxjs.workflow.model.node;

import java.util.ArrayList;
import java.util.List;

import org.springframework.util.StringUtils;

import com.ajaxjs.util.ReflectUtil;
import com.ajaxjs.util.logger.LogHelper;
import com.ajaxjs.workflow.common.WfException;
import com.ajaxjs.workflow.model.BaseWfModel;
import com.ajaxjs.workflow.model.Execution;
import com.ajaxjs.workflow.model.TransitionModel;
import com.ajaxjs.workflow.service.interceptor.WorkflowInterceptor;

/**
 * 节点元素（存在输入输出的变迁）
 */
public abstract class NodeModel extends BaseWfModel {
	public static final LogHelper LOGGER = LogHelper.getLog(NodeModel.class);

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
	 * @param exec 执行对象
	 */
	protected abstract void exec(Execution exec);

	/**
	 * 对执行逻辑增加前置、后置拦截处理
	 * 
	 * @param exec 执行对象
	 */

	public void execute(Execution exec) {
		intercept(preInterceptorList, exec);
		exec(exec);
		intercept(postInterceptorList, exec);
	}

	/**
	 * 运行变迁继续执行
	 * 
	 * @param exec 执行对象
	 */
	protected void runOutTransition(Execution exec) {
		for (TransitionModel tm : getOutputs()) {
			tm.setEnabled(true);
			tm.execute(exec);
		}
	}

	/**
	 * 拦截方法
	 * 
	 * @param interceptos 拦截器列表
	 * @param exec        执行对象
	 */
	private static void intercept(List<WorkflowInterceptor> interceptos, Execution exec) {
		try {
			for (WorkflowInterceptor interceptor : interceptos)
				interceptor.intercept(exec);
		} catch (Exception e) {
			LOGGER.warning("拦截器执行失败=" + e.getMessage());
			throw new WfException(e);
		}
	}

	/**
	 * 获取下一步的模型
	 * 
	 * @param <T>
	 * @param clazz
	 * @return
	 */
	public <T> List<T> getNextModels(Class<T> clazz) {
		List<T> models = new ArrayList<>();

		for (TransitionModel tm : this.getOutputs())
			addNextModels(models, tm, clazz);

		return models;
	}

	@SuppressWarnings("unchecked")
	protected <T> void addNextModels(List<T> models, TransitionModel tm, Class<T> clazz) {
		NodeModel next = tm.getTarget();

		if (clazz.isInstance(next))
			models.add((T) next);
		else {
			for (TransitionModel tm2 : next.getOutputs())
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
		this.preInterceptors = preInterceptors;

		if (StringUtils.hasText(preInterceptors)) {
			for (String interceptor : preInterceptors.split(",")) {
				WorkflowInterceptor instance = (WorkflowInterceptor) ReflectUtil.newInstance(interceptor);

				if (instance != null)
					this.preInterceptorList.add(instance);
			}
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

		if (StringUtils.hasText(postInterceptors)) {
			for (String interceptor : postInterceptors.split(",")) {
				WorkflowInterceptor instance = (WorkflowInterceptor) ReflectUtil.newInstance(interceptor);

				if (instance != null)
					this.postInterceptorList.add(instance);
			}
		}
	}
}
