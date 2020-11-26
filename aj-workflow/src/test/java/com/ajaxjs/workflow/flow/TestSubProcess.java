package com.ajaxjs.workflow.flow;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.ajaxjs.util.ioc.ComponentMgr;
import com.ajaxjs.workflow.BaseTest;
import com.ajaxjs.workflow.WorkflowEngine;
import com.ajaxjs.workflow.process.ProcessActive;
import com.ajaxjs.workflow.task.Task;

public class TestSubProcess extends BaseTest {
//	@Test
	public void testSubProcess1() {
//		WorlflowEngine engine = (WorlflowEngine) init("test/subprocess/child.xml");
//		engine = (WorlflowEngine) init("test/subprocess/subprocess1.xml");

		WorkflowEngine engine = ComponentMgr.get(WorkflowEngine.class);
		Map<String, Object> args = new HashMap<>();
		args.put("task1.operator", new String[] { "1" });
		ProcessActive active = engine.startInstanceByName("subprocess1", 0, 2L, args);
		List<Task> tasks = engine.task().findByActiveId(active.getId());

		for (Task task : tasks) {
			System.out.println("************************begin:::::" + task);
			engine.executeTask(task.getId(), 1L, args);
			System.out.println("************************end:::::" + task);
		}
	}

	/**
	 * 测试子流程的fork-join流程
	 * 
	 * <pre>
	 * start->subprocess1----->end
	 *    |___subprocess2_______|
	 * </pre>
	 */
	@Test
	public void testSubProcess2() {
//		WorlflowEngine engine = (WorlflowEngine) init("test/subprocess/subprocess2.xml");
		WorkflowEngine engine = ComponentMgr.get(WorkflowEngine.class);
		Map<String, Object> args = new HashMap<>();
		args.put("task1.operator", new String[] { "1" });
		ProcessActive active = engine.startInstanceByName("subprocess2", 0, 2L, args);
		List<Task> tasks = engine.task().findByActiveId(active.getId());

		if (tasks != null)
			for (Task task : tasks) {
				engine.executeTask(task.getId(), 1L, args);
			}
	}
}
