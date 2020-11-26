package com.ajaxjs.workflow.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;

import com.ajaxjs.util.CommonUtil;
import com.ajaxjs.util.ReflectUtil;
import com.ajaxjs.util.logger.LogHelper;
import com.ajaxjs.workflow.Execution;
import com.ajaxjs.workflow.task.Task;
import com.ajaxjs.workflow.util.scheduling.JobCallback;

/**
 * 任务定义 task 元素
 * 
 */
public class TaskModel extends WorkModel {
	private static final LogHelper LOGGER = LogHelper.getLog(TaskModel.class);

	private static final long serialVersionUID = 1775545243233990922L;

	/**
	 * 参与者在 args 里面的键
	 */
	public static final String KEY_ACTOR = "S-ACTOR";

	/**
	 * 类型：普通任务
	 */
	public static final String PERFORMTYPE_ANY = "ANY";

	public static final int PERFORM_TYPE_ANY = 0;

	public static final int PERFORM_TYPE_ALL = 1;

	/**
	 * 类型：参与者 fork 任务
	 */
	public static final String PERFORMTYPE_ALL = "ALL";

	/**
	 * 类型：主办任务
	 */
	public static final String TASKTYPE_MAJOR = "Major";

	/**
	 * 类型：协办任务
	 */
	public static final String TASKTYPE_AIDANT = "Aidant";

	/**
	 * 参与类型
	 */
	public enum PerformType {
		ANY, ALL;
	}

	/**
	 * 任务类型(Major:主办的,Aidant:协助的,Record:仅仅作为记录的)
	 */
	public enum TaskType {
		Major, Aidant, Record;
	}

	/**
	 * 参与者变量名称
	 */
	private String assignee;

	/**
	 * 参与方式 any：任何一个参与者处理完即执行下一步 all：所有参与者都完成，才可执行下一步
	 */
	private String performType = PERFORMTYPE_ANY;

	/**
	 * 任务类型 major：主办任务 aidant：协办任务
	 */
	private String taskType = TASKTYPE_MAJOR;

	/**
	 * 期望完成时间
	 */
	private Date expireTime;

	/**
	 * 提醒时间
	 */
	private Date reminderTime;

	/**
	 * 提醒间隔(分钟)
	 */
	private String reminderRepeat;

	/**
	 * 是否自动执行
	 */
	private String autoExecute;

	/**
	 * 任务执行后回调类
	 */
	private String callback;

	/**
	 * 分配参与者处理类型
	 */
	private String assignmentHandler;

	/**
	 * 任务执行后回调对象
	 */
	private JobCallback callbackObject;

	/**
	 * 字段模型集合
	 */
	private List<FieldModel> fields = null;

	@Override
	protected void exec(Execution execution) {
		LOGGER.info("任务模型的执行");

		if (performType == null || performType.equalsIgnoreCase(PERFORMTYPE_ANY))
			runOutTransition(execution);// any 方式，直接执行输出变迁
		else {
			String taskName = getName(); // all 的任务名称

			/*
			 * all 方式，需要判断是否已全部合并 由于 all 方式分配任务，是每人一个任务那么此时需要判断之前分配的所有任务都执行完成后，才可执行下一步，否则不处理
			 * actor all 方式的合并处理器。查询参数为：orderId、taskName
			 */
			AbstractMergeHandler m = new AbstractMergeHandler() {
				@Override
				protected String[] findActiveNodes() {
					return new String[] { taskName };
				}
			};
			m.handle(execution);

//			fire(new AbstractMergeHandler() {
//				@Override
//				protected String[] findActiveNodes() {
//					return new String[] { taskName };
//				}
//			}, execution);

			if (execution.isMerged())
				runOutTransition(execution);
		}
	}

	/**
	 * Model 转换为 Task。把已知的 Model 字段先拷贝到 Task
	 * 
	 * @return Task 实体
	 */
	public Task toTask() {
		Task task = new Task();// 根据模型、执行对象、任务类型构建基本的 task 对象
		task.setName(getName());
		task.setDisplayName(getDisplayName());
		task.setModel(this);
		task.setTaskType(isMajor() ? TaskType.Major.ordinal() : TaskType.Aidant.ordinal());

		return task;
	}

	public boolean isPerformAny() {
		return PERFORMTYPE_ANY.equalsIgnoreCase(this.performType);
	}

	public boolean isPerformAll() {
		return PERFORMTYPE_ALL.equalsIgnoreCase(this.performType);
	}

	public boolean isMajor() {
		return TASKTYPE_MAJOR.equalsIgnoreCase(this.taskType);
	}

	public String getAssignee() {
		return assignee;
	}

	public void setAssignee(String assignee) {
		this.assignee = assignee;
	}

	public Date getExpireTime() {
		return expireTime;
	}

	public void setExpireTime(Date expireTime) {
		this.expireTime = expireTime;
	}

	public String getTaskType() {
		return taskType;
	}

	public void setTaskType(String taskType) {
		this.taskType = (CommonUtil.isEmptyString(taskType) ? TASKTYPE_MAJOR : taskType);
	}

	public String getPerformType() {
		return performType;
	}

	public void setPerformType(String performType) {
		this.performType = (CommonUtil.isEmptyString(performType) ? PERFORMTYPE_ANY : performType);
	}

	public Date getReminderTime() {
		return reminderTime;
	}

	public void setReminderTime(Date reminderTime) {
		this.reminderTime = reminderTime;
	}

	public String getReminderRepeat() {
		return reminderRepeat;
	}

	public void setReminderRepeat(String reminderRepeat) {
		this.reminderRepeat = reminderRepeat;
	}

	public String getAutoExecute() {
		return autoExecute;
	}

	public void setAutoExecute(String autoExecute) {
		this.autoExecute = autoExecute;
	}

	/**
	 * 如何分配参与者
	 */
	private BiFunction<TaskModel, Execution, Object> assignment;

	public String getAssignmentHandler() {
		return assignmentHandler;
	}

	public String getCallback() {
		return callback;
	}

	public JobCallback getCallbackObject() {
		return callbackObject;
	}

	public void setCallback(String callbackStr) {
		if (!CommonUtil.isEmptyString(callbackStr)) {
			this.callback = callbackStr;
			callbackObject = (JobCallback) ReflectUtil.newInstance(callbackStr);
			Objects.requireNonNull(callbackObject, "回调处理类实例化失败");
		}
	}

	public List<FieldModel> getFields() {
		return fields;
	}

	public void setFields(List<FieldModel> fields) {
		this.fields = fields;
	}

	/**
	 * 获取后续任务模型集合（方便预处理）
	 * 
	 * @return 模型集合
	 * @deprecated
	 */
	public List<TaskModel> getNextTaskModels() {
		List<TaskModel> models = new ArrayList<TaskModel>();
		for (TransitionModel tm : this.getOutputs())
			addNextModels(models, tm, TaskModel.class);

		return models;
	}

	public BiFunction<TaskModel, Execution, Object> getAssignment() {
		return assignment;
	}

	public void setAssignment(BiFunction<TaskModel, Execution, Object> assignment) {
		this.assignment = assignment;
	}
}
