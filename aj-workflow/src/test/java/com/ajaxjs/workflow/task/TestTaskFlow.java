package com.ajaxjs.workflow.task;

import static org.junit.Assert.assertNotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.ajaxjs.util.ioc.ComponentMgr;
import com.ajaxjs.workflow.BaseTest;
import com.ajaxjs.workflow.WorkflowEngine;
import com.ajaxjs.workflow.model.TaskModel;
import com.ajaxjs.workflow.process.ProcessActive;

public class TestTaskFlow extends BaseTest {
	/**
	 * 转派任务
	 */
	@Test
	public void testTransfer() {
		deploy("test/task/transfer.xml");

		WorkflowEngine engine = ComponentMgr.get(WorkflowEngine.class);
		ProcessActive pa = engine.startInstanceByName("transfer", 0, 1000L, null);

		for (Task task : engine.task().findByActiveId(pa.getId())) {
			engine.task().createNewTask(task.getId(), 0, 1000L);
			engine.task().complete(task.getId(), null, null);
		}
	}

	/**
	 * 驳回
	 */
//	@Test
	public void testReject() {
		deploy("test/task/reject.xml");

		Map<String, Object> args = new HashMap<>();
		args.put("number", 2);

		WorkflowEngine engine = ComponentMgr.get(WorkflowEngine.class);
		ProcessActive active = engine.startInstanceByName("reject", null, WorkflowEngine.ADMIN, args);

		for (Task task : engine.task().findByActiveId(active.getId())) {
			assertNotNull(task);
		}

		engine.executeTask(1L, null, args);
		engine.executeAndJumpTask(1L, null, args, "task1");
	}

	/**
	 * 唤醒
	 */
//	@Test
	public void testResume() {
		deploy("test/task/simple.xml");

		Map<String, Object> args = new HashMap<>();
		args.put("task1.operator", new String[] { "1" });

		WorkflowEngine engine = ComponentMgr.get(WorkflowEngine.class);
		ProcessActive active = engine.startInstanceByName("simple", null, 2L, args);

		for (Task task : engine.task().findByActiveId(active.getId()))
			engine.executeTask(task.getId(), 2L, args);

		engine.active().resume(active.getId());
	}

	/**
	 * 协办流程
	 */
	@Test
	public void testAidant() {
		deploy("test/task/aidant.xml");

		WorkflowEngine engine = ComponentMgr.get(WorkflowEngine.class);
		ProcessActive active = engine.startInstanceByName("aidant", null, WorkflowEngine.ADMIN, null);

		for (Task task : engine.task().findByActiveId(active.getId())) {
			engine.task().createNewTask(task.getId(), 1, 1000L);
		}
	}

	/**
	 * 自由流
	 */
//	@Test
	public void testFreeFlow() {
		deploy("test/freeflow.xml");

		TaskModel tm1 = new TaskModel();
		tm1.setName("task1");
		tm1.setDisplayName("任务1");
		TaskModel tm2 = new TaskModel();
		tm2.setName("task2");
		tm2.setDisplayName("任务2");

		Map<String, Object> args = new HashMap<>();
		args.put("task1.operator", new String[] { "1" });

		WorkflowEngine engine = ComponentMgr.get(WorkflowEngine.class);
		ProcessActive active = engine.startInstanceByName("aidant", null, WorkflowEngine.ADMIN, args);
		List<Task> tasks = engine.createFreeTask(active.getId(), 1L, args, tm1);

		for (Task task : tasks) {
			engine.task().complete(task.getId(), 1L, null);
		}

		engine.active().terminate(active.getId(), null);
	}

}
