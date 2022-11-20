/*
 * Copyright 2013-2015 www.snakerflow.com. Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law
 * or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.ajaxjs.workflow.interceptor;

import com.ajaxjs.util.logger.LogHelper;
import com.ajaxjs.workflow.model.Execution;
import com.ajaxjs.workflow.model.po.TaskPO;


/**
 * 日志拦截器
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
public class LogInterceptor implements WorkflowInterceptor {
	public static final LogHelper LOGGER = LogHelper.getLog(LogInterceptor.class);

	/**
	 * 拦截产生的任务对象，打印日志
	 */
	@Override
	public void intercept(Execution execution) {
		for (TaskPO task : execution.getTasks()) {
			StringBuffer buffer = new StringBuffer(100);
			buffer.append("创建任务[标识=").append(task.getId());
			buffer.append(",名称=").append(task.getDisplayName());
			buffer.append(",创建时间=").append(task.getCreateDate());
			buffer.append(",参与者={");

			if (task.getActorIds() != null) {
				for (Long actor : task.getActorIds())
					buffer.append(actor).append(";");
			}

			buffer.append("}]");
			LOGGER.info(buffer.toString());
		}
	}
}
