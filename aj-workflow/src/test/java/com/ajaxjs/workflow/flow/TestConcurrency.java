package com.ajaxjs.workflow.flow;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.ajaxjs.util.ioc.ComponentMgr;
import com.ajaxjs.workflow.BaseTest;
import com.ajaxjs.workflow.WorkflowEngine;
import com.ajaxjs.workflow.process.ProcessActive;
import com.ajaxjs.workflow.task.Task;

public class TestConcurrency extends BaseTest {
	@Test
	public void testActorAll() {
//		deploy("test/concurrency/actorall.xml");

		Map<String, Object> args = new HashMap<>();
		args.put("task1.operator", new String[] { "1", "2" });

		WorkflowEngine engine = ComponentMgr.get(WorkflowEngine.class);
		ProcessActive active = engine.startInstanceByName("actorall", null, 2L, args);
		System.out.println(active);
		// Assert.assertEquals(2, tasks.size());
		// execute(tasks, args);
	}

//	@Test
	public void testForkJoin() {
//		deploy("test/concurrency/forkjoin.xml");

		Map<String, Object> args = new HashMap<>();
		args.put("task1.operator", new String[] { "1" });
		args.put("task2.operator", new String[] { "1" });
		args.put("task3.operator", new String[] { "1" });

		WorkflowEngine engine = ComponentMgr.get(WorkflowEngine.class);
		ProcessActive active = engine.startInstanceByName("forkjoin", 0, 2L, args);

		List<Task> tasks = engine.task().findByActiveId(active.getId());

		for (Task task : tasks) {
			engine.executeTask(task.getId(), 1L, null);
		}
	}
}
