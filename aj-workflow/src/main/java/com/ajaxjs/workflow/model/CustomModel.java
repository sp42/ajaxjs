/*
 * Copyright 2013-2015 www.snakerflow.com. Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law
 * or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.ajaxjs.workflow.model;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.Map;

import org.springframework.util.StringUtils;

import com.ajaxjs.util.ReflectUtil;
import com.ajaxjs.util.map.JsonHelper;
import com.ajaxjs.workflow.WorkflowConstant;
import com.ajaxjs.workflow.WorkflowException;
import com.ajaxjs.workflow.WorkflowUtils;
import com.ajaxjs.workflow.model.TaskModel.TaskType;
import com.ajaxjs.workflow.model.entity.TaskHistory;
import com.ajaxjs.workflow.service.TaskBaseService;

/**
 * 自定义模型
 * 
 * @author yuqs
 * @since 1.0
 */
public class CustomModel extends WorkModel {
	private static final long serialVersionUID = 8796192915721758769L;

	/**
	 * 需要执行的class类路径
	 */
	private String clazz;

	/**
	 * 需要执行的class对象的方法名称
	 */
	private String methodName;

	/**
	 * 执行方法时传递的参数表达式变量名称
	 */
	private String args;

	/**
	 * 执行的返回值变量
	 */
	private String var;

	/**
	 * 加载模型时初始化的对象实例
	 */
	private Object invokeObject;

	@Override
	public void exec(Execution execution) {
		if (invokeObject == null)
			invokeObject = ReflectUtil.newInstance(clazz);

		if (invokeObject == null)
			throw new WorkflowException("自定义模型[class=" + clazz + "]实例化对象失败");

		if (invokeObject instanceof IHandler) {
			IHandler handler = (IHandler) invokeObject;
			handler.handle(execution);
		} else {
			Method method = WorkflowUtils.findMethod(invokeObject.getClass(), methodName);

			if (method == null)
				throw new WorkflowException("自定义模型[class=" + clazz + "]无法找到方法名称:" + methodName);

			Object[] objects = getArgs(execution.getArgs(), args);
			Object returnValue = ReflectUtil.executeMethod(invokeObject, method, objects);

			if (StringUtils.hasText(var))
				execution.getArgs().put(var, returnValue);
		}

		createHistory(execution, this);
		runOutTransition(execution);
	}

	/**
	 * 任务历史记录方法
	 * 
	 * @param execution 执行对象
	 * @param model     自定义节点模型
	 * @return 历史任务对象
	 */
	private static TaskHistory createHistory(Execution execution, CustomModel model) {
		TaskHistory task = new TaskHistory();
		task.setOrderId(execution.getOrder().getId());
		task.setName(model.getName());
		task.setFinishDate(new Date());
		task.setDisplayName(model.getDisplayName());
		task.setStat(WorkflowConstant.STATE_FINISH);
		task.setTaskType(TaskType.Record.ordinal());
		task.setParentId(execution.getTask() == null ? 0 : execution.getTask().getId());
		task.setVariable(JsonHelper.toJson(execution.getArgs()));
		TaskBaseService.initCreate(task);
		TaskBaseService.historyDao.create(task);

		return task;
	}

	/**
	 * 根据传递的执行参数、模型的参数列表返回实际的参数对象数组
	 * 
	 * @param execArgs 运行时传递的参数数据
	 * @param args     自定义节点需要的参数
	 * @return 调用自定义节点类方法的参数数组
	 */
	private Object[] getArgs(Map<String, Object> execArgs, String args) {
		Object[] objects = null;

		if (StringUtils.hasText(args)) {
			String[] argArray = args.split(",");
			objects = new Object[argArray.length];

			for (int i = 0; i < argArray.length; i++)
				objects[i] = execArgs.get(argArray[i]);
		}

		return objects;
	}

	public String getClazz() {
		return clazz;
	}

	public void setClazz(String clazz) {
		this.clazz = clazz;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public String getArgs() {
		return args;
	}

	public void setArgs(String args) {
		this.args = args;
	}

	public String getVar() {
		return var;
	}

	public void setVar(String var) {
		this.var = var;
	}
}
