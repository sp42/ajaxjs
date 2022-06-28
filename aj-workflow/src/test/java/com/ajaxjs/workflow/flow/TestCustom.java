package com.ajaxjs.workflow.flow;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.ajaxjs.workflow.BaseTest;
import com.ajaxjs.workflow.model.entity.Order;
import com.ajaxjs.workflow.model.entity.Task;

public class TestCustom extends BaseTest {
//	@Test
	public void testCustomHandler() {
//		WorlflowEngine engine = (WorlflowEngine) init("test/custom/c1.xml");
		Order order = engine.startInstanceByName("custom1", 0, null, null);
		List<Task> tasks = engine.task().findByOrderId(order.getId());

		if (tasks != null)
			for (Task task : tasks) {
				engine.executeTask(task.getId(), null, null);
			}
	}

	@Test
	public void testCustomClass() {
//		WorlflowEngine engine = (WorlflowEngine) init("test/custom/c2.xml");
		Map<String, Object> args = new HashMap<>();
		args.put("msg", "custom test");

		Order order = engine.startInstanceById(engine.process().lastDeployProcessId, null, args);
		List<Task> tasks = engine.task().findByOrderId(order.getId());
		
		if (tasks != null)
			for (Task task : tasks) {
				engine.executeTask(task.getId(), null, args);
			}
	}
}
