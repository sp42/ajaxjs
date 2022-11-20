/* Copyright 2013-2015 www.snakerflow.com.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ajaxjs.workflow.old;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.ajaxjs.workflow.WorkflowEngine;
import com.ajaxjs.workflow.model.po.OrderPO;
import com.ajaxjs.workflow.model.po.TaskPO;

public class TestMisc extends BaseTest {
	// 实例编号自定义
	@Test
	public void testGenerator() {
		WorkflowEngine engine = (WorkflowEngine) init("test/generator.xml");

		Map<String, Object> args = new HashMap<>();
		args.put("task1.operator", new String[] { "1" });
		OrderPO order = engine.startInstanceById(engine.process().lastDeployProcessId, 2L, args);

		List<TaskPO> tasks = engine.task().findByOrderId(order.getId());

		for (TaskPO task : tasks) {
			engine.executeTask(task.getId(), 1L, null);
		}
	}


}
