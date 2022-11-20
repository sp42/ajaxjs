package com.ajaxjs.workflow.flow;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.ajaxjs.workflow.BaseTest;
import com.ajaxjs.workflow.model.po.Order;

public class TestCC extends BaseTest {
	@Test
	public void testCC() {
		init("test/task/simple.xml");

		Map<String, Object> args = new HashMap<>();
		args.put("task1.operator", new String[] { "1" });

		Order order = engine.startInstanceByName("simple", 0, 2L, args);
//		engine.order().createCCOrder(order.getId(), "test");
//		engine.order().updateCCStatus("b0fcc08da45d4e88819d9c287917b525", "test");
//		engine.order().deleteCCOrder("01b960b9d5df4be7b8565b9f64bc1856", "test");
	}
}
