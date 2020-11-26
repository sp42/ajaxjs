package com.ajaxjs.workflow.util;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.ajaxjs.util.ioc.ComponentMgr;
import com.ajaxjs.workflow.BaseTest;
import com.ajaxjs.workflow.WorkflowEngine;
import com.ajaxjs.workflow.process.ProcessActive;
import com.ajaxjs.workflow.task.Task;

/**
 * 委托代理
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
public class TestSurrogate extends BaseTest {
	@Test
	public void testSurrogate() {
		deploy("test/util/surrogate.xml");

		Map<String, Object> args = new HashMap<>();
		args.put("task1.operator", new String[] { "test" });

		WorkflowEngine engine = ComponentMgr.get(WorkflowEngine.class);
		ProcessActive active = engine.startInstanceByName("surrogate", 0, WorkflowEngine.ADMIN, args);

		for (Task task : engine.task().findByActiveId(active.getId())) {
			engine.executeTask(task.getId(), 1L, args);
		}
	}
}
