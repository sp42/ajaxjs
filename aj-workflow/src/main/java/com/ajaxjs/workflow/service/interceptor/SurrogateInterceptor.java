/*
 * Copyright 2013-2015 www.snakerflow.com. Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law
 * or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.ajaxjs.workflow.service.interceptor;

import com.ajaxjs.workflow.WorkflowEngine;
import com.ajaxjs.workflow.model.Execution;
import com.ajaxjs.workflow.model.po.Task;
import com.ajaxjs.workflow.service.task.TaskActorMgr;

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
					new TaskActorMgr().addTaskActor(task.getId(), agent);
			}
		}
	}
}
