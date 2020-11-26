package com.ajaxjs.workflow.util;

import org.junit.Test;

import com.ajaxjs.util.ioc.ComponentMgr;
import com.ajaxjs.workflow.BaseTest;
import com.ajaxjs.workflow.WorkflowEngine;
import com.ajaxjs.workflow.process.ProcessActive;
import com.ajaxjs.workflow.task.Task;

public class TestInterceptor extends BaseTest {
	@Test
	public void testInterceptor() {
		deploy("test/util/interceptor.xml");

		WorkflowEngine engine = ComponentMgr.get(WorkflowEngine.class);
		ProcessActive active = engine.startInstanceByName("interceptor", null, WorkflowEngine.ADMIN, null);

		for (Task task : engine.task().findByActiveId(active.getId())) {
			engine.executeTask(task.getId(), 1L, null);
		}
	}
}
