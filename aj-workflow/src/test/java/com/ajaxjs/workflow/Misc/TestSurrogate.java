package com.ajaxjs.workflow.Misc;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.ajaxjs.workflow.WorkflowEngine;
import com.ajaxjs.workflow.model.po.OrderPO;
import com.ajaxjs.workflow.old.BaseTest;

public class TestSurrogate extends BaseTest {
	// 委托代理
	@Test
	public void testSurrogate() {
		WorkflowEngine engine = (WorkflowEngine) init("test/surrogate.xml");

		Map<String, Object> args = new HashMap<>();
		args.put("task1.operator", new String[] { "test" });

		OrderPO order = engine.startInstanceByName("surrogate", 0, 2L, args);
		System.out.println(order);
//		List<Task> tasks = queryService.getActiveTasks(new QueryFilter().setOrderId(order.getId()));
//		for(Task task : tasks) {
//			//engine.executeTask(task.getId(), "1", args);
//		}
	}
}
