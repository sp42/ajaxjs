package com.ajaxjs.workflow.model;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.Map;

import com.ajaxjs.util.CommonUtil;
import com.ajaxjs.util.ReflectUtil;
import com.ajaxjs.util.map.JsonHelper;
import com.ajaxjs.workflow.Execution;
import com.ajaxjs.workflow.ExecutionHandler;
import com.ajaxjs.workflow.WorkflowException;
import com.ajaxjs.workflow.WorkflowUtils;
import com.ajaxjs.workflow.model.TaskModel.TaskType;
import com.ajaxjs.workflow.task.TaskHistory;
import com.ajaxjs.workflow.task.service.TaskBaseService;

/**
 * 自定义模型
 * 
 */
public class CustomModel extends WorkModel {
	private static final long serialVersionUID = 8796192915721758769L;

	/**
	 * 需要执行的 class 类路径
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
	 * 执行的返回值变量名，是获取 args 的 key
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
			throw new WorkflowException("自定义模型[class=%s]实例化对象失败", clazz);

		Object returnValue;

		if (invokeObject instanceof ExecutionHandler) {
			ExecutionHandler handler = (ExecutionHandler) invokeObject;
			returnValue = handler.exec(execution);

			if (!CommonUtil.isEmptyString(var))
				execution.getArgs().put(var, returnValue);
		} else { // 自定义类
			Method method = WorkflowUtils.findMethod(invokeObject.getClass(), methodName);

			if (method == null)
				throw new WorkflowException("自定义模型[class=%s]无法找到方法名称:[%s]", clazz, methodName);

			Object[] objects = getArgs(execution.getArgs(), args);
			returnValue = ReflectUtil.executeMethod(invokeObject, method, objects);

			if (!CommonUtil.isEmptyString(var))
				execution.getArgs().put(var, returnValue);
		}

		// 任务历史记录方法
		TaskHistory task = new TaskHistory();
		task.setOrderId(execution.getActive().getId());
		task.setName(getName());
		task.setDisplayName(getDisplayName());
		task.setFinishDate(new Date());
		task.setStat(ProcessModel.STATE_FINISH);
		task.setTaskType(TaskType.Record.ordinal());
		task.setParentId(execution.getTask() == null ? 0 : execution.getTask().getId());
		task.setVariable(JsonHelper.toJson(execution.getArgs()));
		TaskBaseService.initCreate(task);
		TaskBaseService.HISTORY_DAO.create(task);

		runOutTransition(execution);
	}

	/**
	 * 根据传递的执行参数、模型的参数列表返回实际的参数对象数组
	 * 
	 * @param execArgs 运行时传递的参数数据
	 * @param args     自定义节点需要的参数
	 * @return 调用自定义节点类方法的参数数组
	 */
	private static Object[] getArgs(Map<String, Object> execArgs, String args) {
		Object[] objects = null;

		if (!CommonUtil.isEmptyString(args)) {
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
