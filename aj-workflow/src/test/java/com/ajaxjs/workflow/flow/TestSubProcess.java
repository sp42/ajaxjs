package com.ajaxjs.workflow.flow;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.ajaxjs.workflow.model.po.OrderPO;
import com.ajaxjs.workflow.model.po.TaskPO;
import com.ajaxjs.workflow.old.BaseTest;

public class TestSubProcess extends BaseTest {
	
//	@Test
	public void testSubProcess1() {
//		WorlflowEngine engine = (WorlflowEngine) init("test/subprocess/child.xml");
//		engine = (WorlflowEngine) init("test/subprocess/subprocess1.xml");

		Map<String, Object> args = new HashMap<>();
		args.put("task1.operator", new String[] { "1" });
		OrderPO order = engine.startInstanceByName("subprocess1", 0, 2L, args);
		List<TaskPO> tasks = engine.task().findByOrderId(order.getId());

		for (TaskPO task : tasks) {
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
		Map<String, Object> args = new HashMap<>();
		args.put("task1.operator", new String[] { "1" });
		OrderPO order = engine.startInstanceByName("subprocess2", 0, 2L, args);
		List<TaskPO> tasks = engine.task().findByOrderId(order.getId());

		if (tasks != null)
			for (TaskPO task : tasks) {
				engine.executeTask(task.getId(), 1L, args);
			}
	}
}
