/* Copyright 2013-2015 www.snakerflow.com.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ajaxjs.workflow.model;

import java.util.List;
import java.util.Map;

import com.ajaxjs.spring.DiContextUtil;
import com.ajaxjs.util.logger.LogHelper;
import com.ajaxjs.workflow.interceptor.WorkflowInterceptor;
import com.ajaxjs.workflow.model.po.TaskPO;
import com.ajaxjs.workflow.service.handler.IHandler;
import com.ajaxjs.workflow.service.handler.SubProcessHandler;

/**
 * 变迁定义 transition 元素
 * 
 */
public class TransitionModel extends BaseWfModel {
	public static final LogHelper LOGGER = LogHelper.getLog(TransitionModel.class);

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
	 * 变迁的目标节点name名称
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

	public void execute(Execution execution) {
		if (!enabled)
			return;

		if (target instanceof TaskModel) {
			final TaskModel tm = (TaskModel) target;

			// 如果目标节点模型为 TaskModel，则创建 task，这是 CreateTaskHandler
			fire(new IHandler() {
				@Override
				public void handle(Execution execution) {
					LOGGER.info("创建 {0} 任务", tm.getName());

					List<TaskPO> tasks = execution.getEngine().task().createTask(tm, execution);
					execution.addTasks(tasks);

					// 从服务上下文中查找任务拦截器列表，依次对 task 集合进行拦截处理
					Map<String, WorkflowInterceptor> interceptors = DiContextUtil.findByInterface(WorkflowInterceptor.class);

					try {
						for (String id : interceptors.keySet())
							interceptors.get(id).intercept(execution);
					} catch (Exception e) {
						LOGGER.warning("拦截器执行失败=" + e.getMessage());
					}
				}
			}, execution);
		} else if (target instanceof SubProcessModel)
			fire(new SubProcessHandler((SubProcessModel) target), execution);// 如果目标节点模型为SubProcessModel，则启动子流程
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
