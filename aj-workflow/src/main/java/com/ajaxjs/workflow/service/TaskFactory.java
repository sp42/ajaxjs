package com.ajaxjs.workflow.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiFunction;

import org.springframework.util.StringUtils;

import com.ajaxjs.util.map.JsonHelper;
import com.ajaxjs.workflow.common.WfException;
import com.ajaxjs.workflow.common.WfUtils;
import com.ajaxjs.workflow.common.WfConstant.TaskType;
import com.ajaxjs.workflow.model.Execution;
import com.ajaxjs.workflow.model.po.Order;
import com.ajaxjs.workflow.model.po.Task;
import com.ajaxjs.workflow.model.work.TaskModel;

/**
 * Task 怎么生成？ Task 就是根据 XML 里面的定义转换到 Java 对象，再持久化
 * 
 * @author Frank Cheung<sp42@qq.com>
 *
 */
public class TaskFactory {
	/**
	 * 根据模型、执行对象、任务类型构建基本的 task 对象
	 * 
	 * @param model 模型
	 * @param exec  执行对象
	 * @return 任务列表
	 */
	public static Task create(TaskModel model, Execution exec, Long[] actors) {
		Task task = new Task();

		linkOrder(task, exec.getOrder());

		task.setName(model.getName());
		task.setDisplayName(model.getDisplayName());
		task.setTaskType(model.isMajor() ? TaskType.MAJOR : TaskType.AIDANT);
		task.setModel(model);
		task.setExpireDate(model.getExpireTime());

		if (exec.getTask() != null)
			task.setParentId(exec.getTask().getId());

		Map<String, Object> args = getArgs(exec, actors);
		task.setVariable(JsonHelper.toJson(args));

		setActionUrl(task, args, model);

		return task;
	}

	private static void setActionUrl(Task task, Map<String, Object> args, TaskModel model) {
		String form = model.getForm();
		String formArgs = (String) args.get(form);

		if (StringUtils.hasText(formArgs))
			form = formArgs;

		task.setActionUrl(form);
	}

	private static Map<String, Object> getArgs(Execution exec, Long[] actors) {
		Map<String, Object> args = exec.getArgs();

		if (args == null)
			args = new HashMap<String, Object>();

		args.put(Task.KEY_ACTOR, WfUtils.join(actors));

		return args;
	}

	/**
	 * order 是流程实例，这里关联之
	 * 
	 * @param task
	 * @param order
	 */
	private static void linkOrder(Task task, Order order) {
		Objects.requireNonNull(order);
		Long orderId = order.getId();
		Objects.requireNonNull(order, "缺少流程实例 id");

		task.setOrderId(orderId);
	}

	/**
	 * 根据 Task 模型的 assignee、assignmentHandler 属性以及运行时数据，确定参与者 actor = assignee
	 * 
	 * @param model 模型
	 * @param exec  执行对象
	 * @return 参与者数组
	 */
	public static Long[] getActors(TaskModel model, Execution exec) {
		String assignee = model.getAssignee();
		Map<String, Object> args = exec.getArgs();
		Object actors = assignee;

		if (StringUtils.hasText(assignee) && args != null && args.containsKey(assignee)) {
			actors = args.get(assignee);
		} else if (model.getAssignment() != null) {
			BiFunction<TaskModel, Execution, Object> handler = model.getAssignment();
			actors = handler.apply(model, exec);
		}

		return getActors(actors);
	}

	/**
	 * 根据 taskmodel 指定的 assignee 属性，从 args 中取值将取到的值处理为 String[] 类型。
	 * 
	 * @param actors 参与者对象
	 * @return 参与者数组
	 */
	private static Long[] getActors(Object actors) {
		if (actors == null)
			return null;

		Long[] results;

		if (actors instanceof String[]) {
			String[] arr = (String[]) actors;
			results = new Long[arr.length];

			for (int i = 0; i < arr.length; i++)
				results[i] = Long.parseLong(arr[i]);

		} else if (actors instanceof String) {// 如果值为字符串类型，则使用逗号,分隔
			if (actors.toString().indexOf(",") != -1) {
				String[] arr = actors.toString().split(",");
				results = new Long[arr.length];

				for (int i = 0; i < arr.length; i++)
					results[i] = Long.parseLong(arr[i]);
			} else {
				results = new Long[1];
				results[0] = Long.parseLong(actors.toString());
			}
		} else if (actors instanceof List) {
			// jackson会把stirng[]转成arraylist，此处增加arraylist的逻辑判断,by 红豆冰沙2014.11.21
			List<?> list = (List<?>) actors;
			results = new Long[list.size()];

			for (int i = 0; i < list.size(); i++)
				results[i] = Long.parseLong(list.get(i).toString());
		} else if (actors instanceof Long) {// 如果为Long类型，则返回1个元素的String[]
			results = new Long[1];
			results[0] = (Long) actors;
		} else if (actors instanceof Integer) {// 如果为Integer类型，则返回1个元素的String[]
			results = new Long[1];
			results[0] = Long.parseLong(actors + "");
		} else if (actors instanceof Long[]) {
			results = (Long[]) actors;// 如果为String[]类型，则直接返回
		} else {
			// 其它类型，抛出不支持的类型异常
			throw new WfException("任务参与者对象[" + actors + "]类型不支持." + "合法参数示例:Long,Integer,new String[]{},'10000,20000',List<String>");
		}

		return results;
	}
}
