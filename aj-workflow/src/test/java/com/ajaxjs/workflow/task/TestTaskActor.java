package com.ajaxjs.workflow.task;

import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

import org.junit.Test;

import com.ajaxjs.util.ioc.ComponentMgr;
import com.ajaxjs.workflow.BaseTest;
import com.ajaxjs.workflow.Execution;
import com.ajaxjs.workflow.WorkflowEngine;
import com.ajaxjs.workflow.model.TaskModel;
import com.ajaxjs.workflow.process.ProcessActive;

public class TestTaskActor extends BaseTest {
//	@Test
	public void testActor() {
		WorkflowEngine engine = ComponentMgr.get(WorkflowEngine.class);

		engine.task().addTaskActor(130L, 1L, 2L);
		engine.task().removeTaskActor(132L, 2L);
	}

	/**
	 * 获取参与者
	 */
	@Test
	public void testAssignee() {
		deploy("test/task/actor/assignee.xml");
		WorkflowEngine engine = ComponentMgr.get(WorkflowEngine.class);
		ProcessActive active = engine.startInstanceByName("assignee", 0, 1000L, null);

		for (Task task : engine.task().findByActiveId(active.getId())) {
			for (long id : task.getActorIds()) {
				assertTrue(id == 1000L || id == 2000L);
			}
			engine.executeTask(task.getId(), 87L, null);
		}
	}

	/**
	 * 获取参与者（通过 args）
	 */
//	@Test
	public void testAssigneeByArgs() {
//		deploy("test/task/actor/assigneeByArgs.xml");
//		engine.process().updateType(processId, "预算管理流程"); TODO

		Map<String, Object> args = new HashMap<>();
		args.put("task1.operator", new String[] { "1333" });

		WorkflowEngine engine = ComponentMgr.get(WorkflowEngine.class);
		ProcessActive active = engine.startInstanceByName("assigneeByArgs", 0, 87L, args);

		for (Task task : engine.task().findByActiveId(active.getId())) {
			for (long id : task.getActorIds()) {
				assertTrue(id == 1333L);
			}
			engine.executeTask(task.getId(), 88L, args);
		}
	}

	/**
	 * 编码设置参与者
	 */
	@Test
	public void testAssignmentHandler() {
		deploy("test/task/assignmenthandler.xml");

		WorkflowEngine engine = ComponentMgr.get(WorkflowEngine.class);
		ProcessActive active = engine.startInstanceByName("assignmenthandler", null, WorkflowEngine.ADMIN, null);

		for (Task task : engine.task().findByActiveId(active.getId())) {
//			engine.executeTask(task.getId(), "admin");
			engine.executeTask(task.getId(), null, null);
		}
	}

	BiFunction<TaskModel, Execution, Object> TaskAssign = (TaskModel model, Execution execution) -> {
		System.out.println(model);
		System.out.println(execution.getArgs());

		return "admin";
	};

//	public class CustomAccessStrategy extends GeneralAccessStrategy {
//		@Override
//		protected List<String> ensureGroup(String operator) {
//			List<String> groups = new ArrayList<String>();
//			if (operator.equals("test")) {
//				groups.add("test");
//			} else {
//				groups.add("role1");
//			}
//			return groups;
//		}
//	}

	// 测试该类时，确认是否配置了自定义的访问策略，请检查snaker.xml中的配置
	@Test
	public void testCustomAccess() {
		deploy("test/task/group.xml");

		Map<String, Object> args = new HashMap<>();
		args.put("task1.operator", new String[] { "role1" });

		WorkflowEngine engine = ComponentMgr.get(WorkflowEngine.class);
		ProcessActive active = engine.startInstanceByName("group", 0, WorkflowEngine.ADMIN, null);

		for (Task task : engine.task().findByActiveId(active.getId())) {
			// 操作人改为test时，角色对应test，会无权处理
			engine.executeTask(task.getId(), 1000L, args);
		}
	}

	// 测试无权限执行任务
	/**
	 * 
	 */
	@Test
	public void TestNotAllow() {
		deploy("test/task/actor/right.xml");
		Map<String, Object> args = new HashMap<>();
		args.put("task1.operator", new String[] { "2" });

		WorkflowEngine engine = ComponentMgr.get(WorkflowEngine.class);
		ProcessActive active = engine.startInstanceByName("notallow", null, WorkflowEngine.ADMIN, args);

		for (Task task : engine.task().findByActiveId(active.getId())) {
			engine.executeTask(task.getId(), WorkflowEngine.ADMIN, args);
		}
	}
}
