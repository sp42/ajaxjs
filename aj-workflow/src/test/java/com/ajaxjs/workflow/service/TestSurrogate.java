package com.ajaxjs.workflow.service;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.ajaxjs.workflow.BaseTest;
import com.ajaxjs.workflow.model.po.Order;

public class TestSurrogate extends BaseTest {
	// 委托代理
	@Test
	public void testSurrogate() {
		init("test/surrogate.xml");

		Map<String, Object> args = new HashMap<>();
		args.put("task1.operator", new String[] { "test" });

		Order order = engine.startInstanceByName("surrogate", 0, 2L, args);
		System.out.println(order);
//		List<Task> tasks = queryService.getActiveTasks(new QueryFilter().setOrderId(order.getId()));
//		for(Task task : tasks) {
//			//engine.executeTask(task.getId(), "1", args);
//		}
	}
}
