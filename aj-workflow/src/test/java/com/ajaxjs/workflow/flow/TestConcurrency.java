package com.ajaxjs.workflow.flow;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.ajaxjs.workflow.model.po.OrderPO;
import com.ajaxjs.workflow.model.po.TaskPO;
import com.ajaxjs.workflow.old.BaseTest;

public class TestConcurrency extends BaseTest {
	@Test
	public void testActorAll() {
//		WorlflowEngine engine = (WorlflowEngine) init("test/concurrency/actorall.xml");

		Map<String, Object> args = new HashMap<>();
		args.put("task1.operator", new String[] { "1", "2" });
		
		OrderPO order = engine.startInstanceByName("actorall", 0, 2L, args);
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

		OrderPO order = engine.startInstanceByName("forkjoin", 0, 2L, args);

		List<TaskPO> tasks = engine.task().findByOrderId(order.getId());

		for (TaskPO task : tasks) {
			engine.executeTask(task.getId(), 1L, null);
		}
	}
}
