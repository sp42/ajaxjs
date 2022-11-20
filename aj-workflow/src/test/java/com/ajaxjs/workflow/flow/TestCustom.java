package com.ajaxjs.workflow.flow;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.ajaxjs.workflow.model.po.OrderPO;
import com.ajaxjs.workflow.model.po.TaskPO;
import com.ajaxjs.workflow.old.BaseTest;

public class TestCustom extends BaseTest {
//	@Test
	public void testCustomHandler() {
//		WorlflowEngine engine = (WorlflowEngine) init("test/custom/c1.xml");
		OrderPO order = engine.startInstanceByName("custom1", 0, null, null);
		List<TaskPO> tasks = engine.task().findByOrderId(order.getId());

		if (tasks != null)
			for (TaskPO task : tasks) {
				engine.executeTask(task.getId(), null, null);
			}
	}

	@Test
	public void testCustomClass() {
//		WorlflowEngine engine = (WorlflowEngine) init("test/custom/c2.xml");
		Map<String, Object> args = new HashMap<>();
		args.put("msg", "custom test");

		OrderPO order = engine.startInstanceById(engine.process().lastDeployProcessId, null, args);
		List<TaskPO> tasks = engine.task().findByOrderId(order.getId());
		
		if (tasks != null)
			for (TaskPO task : tasks) {
				engine.executeTask(task.getId(), null, args);
			}
	}
}
