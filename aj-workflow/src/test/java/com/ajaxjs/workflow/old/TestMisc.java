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

import java.util.List;

import org.junit.Test;

import com.ajaxjs.util.logger.LogHelper;
import com.ajaxjs.workflow.BaseTest;
import com.ajaxjs.workflow.model.Args;
import com.ajaxjs.workflow.model.node.work.TaskModel;
import com.ajaxjs.workflow.model.po.Order;
import com.ajaxjs.workflow.model.po.Task;
import com.ajaxjs.workflow.service.scheduling.JobCallback;

public class TestMisc extends BaseTest {
	// 实例编号自定义
	@Test
	public void testGenerator() {
		init("test/generator.xml");

		Args args = new Args();
		args.put("task1.operator", new String[] { "1" });
		Order order = engine.startInstanceById(engine.processService.lastDeployProcessId, 2L, args);

		List<Task> tasks = engine.taskService.findByOrderId(order.getId());

		for (Task task : tasks) {
			engine.executeTask(task.getId(), 1L, null);
		}
	}

	// 自由流
	@Test
	public void testFreeFlow() {
		init("test/freeflow.xml");

		Args args = new Args();
		args.put("task1.operator", new String[] { "1" });

		Order order = engine.startInstanceById(engine.processService.lastDeployProcessId, 2L, args);
		TaskModel tm1 = new TaskModel();
		tm1.setName("task1");
		tm1.setDisplayName("任务1");
		TaskModel tm2 = new TaskModel();
		tm2.setName("task2");
		tm2.setDisplayName("任务2");
		List<Task> tasks = engine.createFreeTask(order.getId(), 1L, args, tm1);

		for (Task task : tasks) {
			engine.taskService.complete(task.getId(), 1L, null);
		}

//		tasks = engine.createFreeTask(order.getId(), "1", args, tm2);
//		for(Task task : tasks) {
//			engine.taskService.complete(task.getId(), "1", null);
//		}
		engine.orderService.terminate(order.getId(), null);
	}

	private static final String PROCESSNAME = "expire";

	public static class TestCallback implements JobCallback {
		public static final LogHelper LOGGER = LogHelper.getLog(TestCallback.class);

		@Override
		public void callback(Long taskId, List<Task> newTasks) {
			LOGGER.info("callback taskId=" + taskId);
			LOGGER.info("newTasks=" + newTasks);
		}
	}

	@Test
	public void testExpire() {
//		System.out.println(DateHelper.parseTime(new DateTime(2014, 4, 6, 16, 41).toDate()));

		Args args = new Args();
		args.put("task1.operator", new String[] { "1" });
//		args.put("task1.expireTime", new DateTime(2014, 4, 15, 9, 0).toDate());
//		args.put("task1.reminderTime", new DateTime(2014, 4, 15, 8, 57).toDate());

		init("test/timeExpire.xml");

		Order order = engine.startInstanceByName(PROCESSNAME, null, 2L, args);
		System.out.println(order);
//		List<Task> tasks = queryService.getActiveTasks(new QueryFilter().setOrderId(order.getId()));
//		for(Task task : tasks) {
//			engine.executeTask(task.getId(), "1", args);
//		}
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
