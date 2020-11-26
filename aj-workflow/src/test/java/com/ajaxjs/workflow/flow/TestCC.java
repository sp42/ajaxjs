package com.ajaxjs.workflow.flow;

import static org.junit.Assert.assertNotNull;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.ajaxjs.util.ioc.ComponentMgr;
import com.ajaxjs.workflow.BaseTest;
import com.ajaxjs.workflow.WorkflowEngine;
import com.ajaxjs.workflow.process.ProcessActive;

public class TestCC extends BaseTest {
	@Test
	public void testCC() {
		deploy("test/task/simple.xml");

		Map<String, Object> args = new HashMap<>();
		args.put("task1.operator", new String[] { "1" });
		
		WorkflowEngine engine = ComponentMgr.get(WorkflowEngine.class);
		ProcessActive active = engine.startInstanceByName("simple", 0, WorkflowEngine.ADMIN, args);
//		engine.order().createCCOrder(active.getId(), "test");
//		engine.order().updateCCStatus("b0fcc08da45d4e88819d9c287917b525", "test");
//		engine.order().deleteCCOrder("01b960b9d5df4be7b8565b9f64bc1856", "test");
		
		assertNotNull(active);
	}
}
