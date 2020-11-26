package com.ajaxjs.workflow.flow;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.ajaxjs.util.ioc.ComponentMgr;
import com.ajaxjs.workflow.BaseTest;
import com.ajaxjs.workflow.Execution;
import com.ajaxjs.workflow.ExecutionHandler;
import com.ajaxjs.workflow.WorkflowEngine;
import com.ajaxjs.workflow.process.ProcessActive;

public class TestCustom extends BaseTest {
	public static class CustomHandler implements ExecutionHandler {
		@Override
		public String exec(Execution execution) {
			System.out.println("custom handler");
			assertNotNull(execution);
			return null;
		}
	}

	@Test
	public void testCustomHandler() {
		deploy("test/flow/custom/c1.xml");

		WorkflowEngine engine = ComponentMgr.get(WorkflowEngine.class);
		ProcessActive active = engine.startInstanceByName("custom1", 0, WorkflowEngine.ADMIN, null);
		assertNotNull(active);

//		for (Task task : engine.task().findByActiveId(active.getId())) {
//			engine.executeTask(task.getId(), null, null);
//		}
	}

	/**
	 * 自定义类
	 * 
	 */
	public static class CustomClass {
		public String execute(String msg) {
//			System.out.println("execute:" + msg);
			assertEquals("custom test", msg);
			return "return " + msg;
		}
	}

	@Test
	public void testCustomClass() {
		deploy("test/flow/custom/c2.xml");

		Map<String, Object> args = new HashMap<>();
		args.put("msg", "custom test");

		WorkflowEngine engine = ComponentMgr.get(WorkflowEngine.class);
		ProcessActive active = engine.startInstanceByName("custom2", 0, WorkflowEngine.ADMIN, args);
		assertNotNull(active);

//		for (Task task : engine.task().findByActiveId(active.getId())) {
//			engine.executeTask(task.getId(), null, args);
//		}
	}
}
