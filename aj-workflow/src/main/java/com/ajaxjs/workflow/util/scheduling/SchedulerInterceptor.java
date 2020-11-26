package com.ajaxjs.workflow.util.scheduling;

import java.util.Date;
import java.util.Map;

import com.ajaxjs.util.ioc.ComponentMgr;
import com.ajaxjs.workflow.Execution;
import com.ajaxjs.workflow.WorkflowInterceptor;
import com.ajaxjs.workflow.WorkflowUtils;
import com.ajaxjs.workflow.model.TaskModel;
import com.ajaxjs.workflow.task.Task;
import com.ajaxjs.workflow.util.scheduling.JobEntity.JobType;

/**
 * 时限控制拦截器 主要拦截任务的 expireDate(期望完成时间) 再交给具体的调度器完成调度处理.
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
public class SchedulerInterceptor implements WorkflowInterceptor {
	/**
	 * 调度器接口
	 */
	private IScheduler scheduler;

	/**
	 * 是否调度
	 */
	private boolean isScheduled = true;

	/**
	 * 时限控制拦截方法
	 */
	@Override
	public void intercept(Execution execution) {
		if (!isScheduled)
			return;

		for (Task task : execution.getTasks()) {
			// 流程 id + 流程实例 id + 任务 id
			String id = execution.getProcess().getId() + "-" + execution.getActive().getId() + "-" + task.getId();

			// 如果有期望完成时间则设置限期
			Date expireDate = task.getExpireDate();
			if (expireDate != null)
				schedule(id, task, expireDate, JobType.EXECUTER.ordinal(), execution.getArgs());

			// 如果有提醒时间则设置提醒
			Date remindDate = task.getRemindDate();
			if (remindDate != null)
				schedule(id, task, remindDate, JobType.REMINDER.ordinal(), execution.getArgs());
		}
	}

	/**
	 * 安排一个新的计划。
	 * 
	 * @param id        任务对应的业务 id 串
	 * @param task      任务对象
	 * @param startDate 开始时间
	 * @param jobType   任务类型
	 * @param args      执行参数
	 */
	private void schedule(String id, Task task, Date startDate, int jobType, Map<String, Object> args) {
		JobEntity entity = new JobEntity(id, task, startDate, args);
		entity.setModelName(task.getName());
		entity.setJobType(jobType);

		/**
		 * 如果是提醒类型这设置间隔时间(分钟)
		 */
		if (jobType == JobType.REMINDER.ordinal()) {
			TaskModel model = (TaskModel) task.getModel();

			// TODO getReminderRepeat 为什么不直接设为 int
			if (model != null && WorkflowUtils.isNumeric(model.getReminderRepeat()))
				entity.setPeriod(Integer.parseInt(model.getReminderRepeat()));
		}

		// 找到计划调度器，开始进行计划
		if (scheduler == null)
//			scheduler = ServiceContext.getContext().find(IScheduler.class);
			scheduler = ComponentMgr.get(IScheduler.class);

		if (scheduler != null)
			scheduler.schedule(entity);
		else
			isScheduled = false; // 没有调度器，不能安排计划
	}
}
