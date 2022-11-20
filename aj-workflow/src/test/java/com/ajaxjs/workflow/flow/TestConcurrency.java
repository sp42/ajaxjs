package com.ajaxjs.workflow.flow;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.ajaxjs.workflow.BaseTest;
import com.ajaxjs.workflow.model.po.Order;
import com.ajaxjs.workflow.model.po.Task;

public class TestConcurrency extends BaseTest {
	@Test
	public void testActorAll() {
//		WorlflowEngine engine = (WorlflowEngine) init("test/concurrency/actorall.xml");

		Map<String, Object> args = new HashMap<>();
		args.put("task1.operator", new String[] { "1", "2" });
		
		Order order = engine.startInstanceByName("actorall", 0, 2L, args);
		System.out.println(order);
		// Assert.assertEquals(2, tasks.size());
		// execute(tasks, args);
	}

//	@Test
	public void testForkJoin() {
//		WorlflowEngine engine = (WorlflowEngine) init("test/concurrency/forkjoin.xml");

		Map<String, Object> args = new HashMap<>();
		args.put("task1.operator", new String[] { "1" });
		args.put("task2.operator", new String[] { "1" });
		args.put("task3.operator", new String[] { "1" });

		Order order = engine.startInstanceByName("forkjoin", 0, 2L, args);

		List<Task> tasks = engine.task().findByOrderId(order.getId());

		for (Task task : tasks) {
			engine.executeTask(task.getId(), 1L, null);
		}
	}
}
