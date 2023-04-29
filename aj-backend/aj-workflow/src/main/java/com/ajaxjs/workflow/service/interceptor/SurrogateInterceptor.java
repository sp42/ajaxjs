package com.ajaxjs.workflow.service.interceptor;

import com.ajaxjs.workflow.WorkflowEngine;
import com.ajaxjs.workflow.model.Execution;
import com.ajaxjs.workflow.model.po.Task;
import com.ajaxjs.workflow.service.TaskService;

/**
 * 新创建的任务通过 SurrogateInterceptor 创建委托。 查询 wf_surrogate 表获取委托代理人，并通过
 * addTaskActor() 设置为参与者(即授权人、代理人都可处理任务)；
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
public class SurrogateInterceptor implements WorkflowInterceptor {
	@Override
	public void intercept(Execution execution) {
		WorkflowEngine engine = execution.getEngine();

		for (Task task : execution.getTasks()) {
			if (task.getActorIds() == null)
				continue;

			for (Long actor : task.getActorIds()) {
				if (actor == null || actor == 0)
					continue;

				// 查询 wf_surrogate 获取委托代理人
				Long agent = engine.surrogateService.getSurrogate(actor, execution.getProcess().getName());

				if (agent != null && agent != 0)
					TaskService.addTaskActor(task.getId(), agent);
			}
		}
	}
}
