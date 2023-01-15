package com.ajaxjs.workflow.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiFunction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import com.ajaxjs.util.logger.LogHelper;
import com.ajaxjs.util.map.JsonHelper;
import com.ajaxjs.workflow.WorkflowEngine;
import com.ajaxjs.workflow.common.WfConstant;
import com.ajaxjs.workflow.common.WfException;
import com.ajaxjs.workflow.model.ProcessModel;
import com.ajaxjs.workflow.model.TransitionModel;
import com.ajaxjs.workflow.model.node.ForkModel;
import com.ajaxjs.workflow.model.node.JoinModel;
import com.ajaxjs.workflow.model.node.NodeModel;
import com.ajaxjs.workflow.model.node.StartModel;
import com.ajaxjs.workflow.model.node.work.SubProcessModel;
import com.ajaxjs.workflow.model.node.work.TaskModel;
import com.ajaxjs.workflow.model.po.Order;
import com.ajaxjs.workflow.model.po.ProcessPO;
import com.ajaxjs.workflow.model.po.Task;
import com.ajaxjs.workflow.model.po.TaskActor;
import com.ajaxjs.workflow.model.po.TaskHistory;
import com.ajaxjs.workflow.service.task.TaskFactory;

/**
 * Actor
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
@Component
public class TaskService extends BaseWfService {
	public static final LogHelper LOGGER = LogHelper.getLog(TaskService.class);

	@Autowired
	private ProcessService processService;

	/**
	 * 根据 id 获取任务
	 * 
	 * @param taskId
	 * @return
	 */
	public static Task getTaskById(Long taskId) {
		Task task = TaskDAO.findById(taskId);
		Objects.requireNonNull(task, "指定的任务[id=" + taskId + "]不存在");

		return task;
	}

	/**
	 * 根据流程 id 查找所有的任务
	 * 
	 * @param orderId 流程 id
	 * @return 所有的任务
	 */
	public List<Task> findByOrderId(Long orderId) {
		return TaskDAO.setWhereQuery("order_id", orderId).findList();
	}

	/**
	 * 根据流程 id 查找所有的历史任务
	 * 
	 * @param orderId 流程 id
	 * @return 所有的历史任务
	 */
	public List<TaskHistory> findHistoryTasksByOrderId(Long orderId) {
		return TaskHistoryDAO.setWhereQuery("order_id", orderId).findList();
	}

	/**
	 * 根据流程 id 和任务名称查找所有的历史任务
	 * 
	 * @param orderId
	 * @param taskName
	 * @return 所有的历史任务
	 */
	public List<TaskHistory> findHistoryTasksByOrderIdAndTaskName(Long orderId, String taskName) {
		return TaskHistoryDAO.setWhereQuery("order_id = " + orderId + " AND name ='" + taskName + "'").findList();
	}

	/**
	 * 获取任务模型。先查找任务对象，然后获取其流程实例 id，根据流程实例 id 获取流程定义 id，然后根据流程定义 id
	 * 获取流量定义模型，从定义模型根据任务名称查找节点模型
	 * 
	 * 不用改成一次性获取
	 * 
	 * @param taskId 任务 id
	 * @return TaskModel
	 */
	public TaskModel getTaskModel(Long taskId) {
		Task task = getTaskById(taskId);
		Order order = OrderDAO.findById(task.getOrderId());
		ProcessPO process = processService.findById(order.getProcessId());

		NodeModel nodeModel = process.getModel().getNode(task.getName());
		Objects.requireNonNull(nodeModel, "任务 id 无法找到节点模型");

		if (nodeModel instanceof TaskModel)
			return (TaskModel) nodeModel;
		else
			throw new IllegalArgumentException("任务 id 找到的节点模型不匹配");
	}

	/**
	 * 完成指定任务。 该方法仅仅结束活动任务，并不能驱动流程继续执行
	 * 
	 * @see WorkflowEngine#executeTask(String, String, java.util.Map)
	 * @param taskId
	 * @param operator
	 * @param args
	 * @return
	 */
	public Task complete(Long taskId, Long operator, Map<String, Object> args) {
		Task task = getTaskById(taskId);
		task.setVariable(JsonHelper.toJson(args));

//		if (!isAllowed(task, operator))
//			throw new WorkflowException("当前参与者[" + operator + "]不允许执行任务[taskId=" + taskId + "]");

		LOGGER.info("完成任务：创建历史任务，然后删除 Task");
		TaskHistory history = new TaskHistory(task);
		history.setFinishDate(new Date());
		history.setStat(WfConstant.STATE_FINISH);
		history.setOperator(operator);

		if (history.getActorIds() == null) {
			LOGGER.info("查询 任务参与者，保存到 TaskHistory");
			List<TaskActor> actors = TaskDAO.findTaskActorsByTaskId(task.getId());
			Long[] actorIds = new Long[actors.size()];

			for (int i = 0; i < actors.size(); i++)
				actorIds[i] = actors.get(i).getActorId();

			history.setActorIds(actorIds);
		}

		TaskHistoryDAO.create(history);
		TaskDAO.delete(task);

//		orderService.getCompletion().accept(history, null);

		return task;
	}

	/**
	 * 对指定的任务分配参与者。参与者可以为用户、部门、角色
	 * 
	 * @param taskId   任务 id
	 * @param actorIds 参与者 id 集合
	 */
	public static void assignTask(Long taskId, Long... actorIds) {
		if (ObjectUtils.isEmpty(actorIds))
			return;

		for (Long actorId : actorIds) {
			if (actorId == null)
				continue;
			// TODO:needs?
//			TaskActor taskActor = new TaskActor();
//			taskActor.setTaskId(taskId);
//			taskActor.setActorId(actorId);

			TaskDAO.createTaskActor(taskId, actorId);
		}
	}

	/**
	 * 根据任务主键 id 和操作人 id 提取任务。
	 * 
	 * @param taskId   任务 id
	 * @param operator 操作人 id
	 * @return Task 任务对象
	 */
	public Task take(Long taskId, Long operator) {
		LOGGER.info("提取任务 [{0}]", taskId);
		Task task = getTaskById(taskId);

		if (!isAllowed(task, operator))
			throw new WfException("当前参与者[" + operator + "]不允许提取任务[taskId=" + taskId + "]");

		Task u = new Task();
		u.setId(taskId);
		u.setOperator(operator);
		u.setFinishDate(new Date());
		TaskDAO.update(u);

		task.setOperator(operator);
		task.setFinishDate(u.getFinishDate());

		return task;
	}

	/**
	 * 根据任务主键 id、操作人撤回任务
	 * 
	 * @param taskId   任务 id
	 * @param operator 操作人
	 * @return Task 任务对象
	 */
	public Task withdrawTask(Long taskId, Long operator) {
		TaskHistory hist = TaskHistoryDAO.findById(taskId);
		Objects.requireNonNull(hist, "指定的历史任务[id=" + taskId + "]不存在");

		// getNextActiveTasks
		List<Task> tasks = hist.isPerformAny() ? TaskDAO.setWhereQuery("parent_task_id", hist.getId()).findList()
				: TaskDAO.getNextActiveTasks(hist.getOrderId(), hist.getName(), hist.getParentId());

		if (ObjectUtils.isEmpty(tasks))
			throw new WfException("后续活动任务已完成或不存在，无法撤回.");

		for (Task task : tasks)
			TaskDAO.delete(task);

		Task task = hist.undoTask();
		TaskFactory.saveTask(task);
		assignTask(task.getId(), task.getOperator());

		return task;
	}

	/**
	 * 根据当前任务对象驳回至上一步处理
	 * 
	 * @param model       流程定义模型，用以获取上一步模型对象
	 * @param currentTask 当前任务对象
	 * @return Task 任务对象
	 */
	public Task rejectTask(ProcessModel model, Task currentTask) {
		Long parentTaskId = currentTask.getParentId();

		if (parentTaskId == null || parentTaskId == 0)
			throw new WfException("上一步任务ID为空，无法驳回至上一步处理");

		NodeModel current = model.getNode(currentTask.getName());
		TaskHistory history = TaskHistoryDAO.findById(parentTaskId);
		NodeModel parent = model.getNode(history.getName());

		if (!canRejected(current, parent))
			throw new WfException("无法驳回至上一步处理，请确认上一步骤并非fork、join、suprocess以及会签任务");

		Task task = history.undoTask();
		task.setOperator(history.getOperator());
		TaskFactory.saveTask(task);
		assignTask(task.getId(), task.getOperator());

		return task;
	}

	/**
	 * 根据父节点模型、当前节点模型判断是否可退回。可退回条件：
	 * 
	 * 1、满足中间无 fork、join、subprocess 模型 2、满足父节点模型如果为任务模型时，参与类型为 any
	 * 
	 * @param current 当前节点模型
	 * @param parent  父节点模型
	 * @return 是否可以退回
	 */
	private static boolean canRejected(NodeModel current, NodeModel parent) {
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
	 * 根据历史任务主键 id，操作人唤醒历史任务 该方法会导致流程状态不可控，请慎用
	 * 
	 * @param taskId   历史任务 id
	 * @param operator 操作人 id
	 * @return Task 唤醒后的任务对象
	 */
	public Task resume(Long taskId, Long operator) {
		TaskHistory histTask = TaskHistoryDAO.findById(taskId);
		Objects.requireNonNull(histTask, "指定的历史任务[id=" + taskId + "]不存在");
		boolean isAllowed = true;

		if (histTask.getOperator() != null && histTask.getOperator() != 0)
			isAllowed = histTask.getOperator() == operator;

		if (isAllowed) {
			Task task = histTask.undoTask();
			TaskFactory.saveTask(task);
			assignTask(task.getId(), task.getOperator());

			return task;
		} else
			throw new WfException("当前参与者[" + operator + "]不允许唤醒历史任务[taskId=" + taskId + "]");
	}

	/**
	 * 根据 taskId、operator，判断当前操作人 operator 是否允许执行 taskId 指定的任务
	 * 
	 * @param task     任务对象
	 * @param operator 操作人
	 * @return boolean 是否允许操作
	 */
	private boolean isAllowed(Task task, Long operator) {
		if (operator != null && operator != 0) {
//			if (SnakerEngine.ADMIN.equalsIgnoreCase(operator) || SnakerEngine.AUTO.equalsIgnoreCase(operator))
//				return true;

			if (task.getOperator() != null && task.getOperator() != 0)
				return operator == task.getOperator();
		}

		List<TaskActor> actors = TaskDAO.findTaskActorsByTaskId(task.getId());

		if (ObjectUtils.isEmpty(actors))
			return true;

		return operator != null && operator != 0 && getTaskAccessStrategy().apply(operator, actors);
	}

	/**
	 * 
	 * 基于用户或组（角色、部门等）的访问策略类。 该策略类适合组作为参与者的情况。 如果操作人 id 所属的组只要有一项存在于参与者集合中，则表示可访问。
	 * 根据操作人 id、参与者集合判断是否允许访问所属任务。
	 * 
	 * @param operator 操作人 id
	 * @param actors   参与者列表 传递至该接口的实现类中的参与者都是为非空。确定的组集合[如操作人属于多个部门、拥有多个角色]
	 * @return boolean 是否允许访问
	 */
	private BiFunction<Long, List<TaskActor>, Boolean> taskAccessStrategy = (Long operator, List<TaskActor> actors) -> {
//		List<String> assignees = ensureGroup(operator);
		List<Long> assignees = null;
		if (assignees == null)
			assignees = new ArrayList<>();

		assignees.add(operator);
		boolean isAllowed = false;

		for (TaskActor actor : actors) {
			for (Long assignee : assignees) {
				if (actor.getActorId() == assignee) {
					isAllowed = true;
					break;
				}
			}
		}

		return isAllowed;
	};

	/**
	 * 
	 * @param id
	 * @param childOrderId
	 * @param activeNodes
	 * @return
	 */
	public List<Task> findByOrderIdAndExcludedIds(Long id, Long childOrderId, String[] activeNodes) {
//		Function<String, String> fn = by("orderId", id);
//
//		if (childOrderId != null && childOrderId != 0)
//			fn.andThen(setWhere("id NOT IN(" + childOrderId + ")"));
//
//		if (!ObjectUtils.isEmpty(activeNodes)) {
//			int i = 0;
//
//			for (String str : activeNodes)
//				activeNodes[i++] = "'" + str + "'";
//
//			fn.andThen(setWhere("name IN(" + String.join(",", activeNodes) + ")"));
//		}

		return TaskDAO.findList();
	}

	public BiFunction<Long, List<TaskActor>, Boolean> getTaskAccessStrategy() {
		return taskAccessStrategy;
	}

	public void setTaskAccessStrategy(BiFunction<Long, List<TaskActor>, Boolean> strategy) {
		this.taskAccessStrategy = strategy;
	}
}
