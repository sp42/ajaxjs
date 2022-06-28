package com.ajaxjs.workflow.task;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.ajaxjs.workflow.BaseTest;
import com.ajaxjs.workflow.WorkflowEngine;
import com.ajaxjs.workflow.model.TaskModel;
import com.ajaxjs.workflow.model.entity.Order;
import com.ajaxjs.workflow.model.entity.Task;

public class TestFreeFlow extends BaseTest {
	// 自由流
	@Test
	public void testFreeFlow() {
		WorkflowEngine engine = (WorkflowEngine) init("test/freeflow.xml");

		Map<String, Object> args = new HashMap<>();
		args.put("task1.operator", new String[] { "1" });

		Order order = engine.startInstanceById(engine.process().lastDeployProcessId, 2L, args);
		TaskModel tm1 = new TaskModel();
		tm1.setName("task1");
		tm1.setDisplayName("任务1");
		TaskModel tm2 = new TaskModel();
		tm2.setName("task2");
		tm2.setDisplayName("任务2");
		List<Task> tasks = engine.createFreeTask(order.getId(), 1L, args, tm1);

		for (Task task : tasks) {
			engine.task().complete(task.getId(), 1L, null);
		}

//		tasks = engine.createFreeTask(order.getId(), "1", args, tm2);
//		for(Task task : tasks) {
//			engine.task().complete(task.getId(), "1", null);
//		}
		engine.order().terminate(order.getId(), null);
	}
}
