/*
'   * Copyright 2013-2015 www.snakerflow.com. Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law
 * or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.ajaxjs.workflow.interceptor;

import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.ajaxjs.workflow.common.WfUtils;
import com.ajaxjs.workflow.model.Execution;
import com.ajaxjs.workflow.model.TaskModel;
import com.ajaxjs.workflow.model.po.TaskPO;
import com.ajaxjs.workflow.scheduling.IScheduler;
import com.ajaxjs.workflow.scheduling.JobEntity;
import com.ajaxjs.workflow.scheduling.JobEntity.JobType;

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
	@Autowired(required = false)
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

		for (TaskPO task : execution.getTasks()) {
			// 流程 id + 流程实例 id + 任务 id
			String id = execution.getProcess().getId() + "-" + execution.getOrder().getId() + "-" + task.getId();

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
	private void schedule(String id, TaskPO task, Date startDate, int jobType, Map<String, Object> args) {
		JobEntity entity = new JobEntity(id, task, startDate, args);
		entity.setModelName(task.getName());
		entity.setJobType(jobType);

		if (jobType == JobType.REMINDER.ordinal()) {// 如果是提醒类型这设置间隔时间(分钟)
			TaskModel model = (TaskModel) task.getModel();

			// TODO getReminderRepeat 为什么不直接设为 int
			if (model != null && WfUtils.isNumeric(model.getReminderRepeat()))
				entity.setPeriod(Integer.parseInt(model.getReminderRepeat()));
		}

		// 找到计划调度器，开始进行计划
		if (scheduler != null)
			scheduler.schedule(entity);
		else
			isScheduled = false; // 没有调度器，不能安排计划
	}
}
