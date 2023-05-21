package com.ajaxjs.workflow.flow;

import com.ajaxjs.workflow.BaseTest;
import com.ajaxjs.workflow.common.WfData;
import com.ajaxjs.workflow.model.Args;
import com.ajaxjs.workflow.model.po.Order;
import com.ajaxjs.workflow.model.po.Task;
import org.junit.Test;

import java.util.List;

public class TestCustom extends BaseTest {
	@Test
	public void testCustomHandler() {
//		WorkflowEngine engine = (WorkflowEngine) init("test/custom/c1.xml");
		Order order = engine.startInstanceByName("custom1", 0, null, null);
		List<Task> tasks = WfData.findTasksByOrderId(order.getId());

		if (tasks != null)
			for (Task task : tasks) {
				engine.executeTask(task.getId(), null, null);
			}
	}

	@Test
	public void testCustomClass() {
//		WorkflowEngine engine = (WorkflowEngine) init("test/custom/c2.xml");
		Args args = new Args();
		args.put("msg", "custom test");

		Order order = engine.startInstanceById(engine.processService.lastDeployProcessId, null, args);
		List<Task> tasks = WfData.findTasksByOrderId(order.getId());
		
		if (tasks != null)
			for (Task task : tasks) {
				engine.executeTask(task.getId(), null, args);
			}
	}
}
