package com.ajaxjs.workflow.old;

import static org.junit.Assert.assertNotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.ajaxjs.workflow.BaseTest;
import com.ajaxjs.workflow.model.ProcessModel;
import com.ajaxjs.workflow.model.po.Order;
import com.ajaxjs.workflow.model.po.Task;
import com.ajaxjs.workflow.model.work.TaskModel;

public class TestTask extends BaseTest {
//	@Test
	public void testConfig() {
		init("test/task/config.xml");
		Order order = engine.startInstanceByName("config", 0, 1000L, null);
		assertNotNull(order);

		Map<String, Object> args = new HashMap<>();
		args.put("task1.operator", new String[] { "1" });
		engine.executeTask(1L, 1L, args);// 任务执行
	}

	@Test
	public void testSimple() {
//		deploy("test/task/simple.xml");
//		engine.process().updateType(processId, "预算管理流程");

		Map<String, Object> args = new HashMap<>();
		args.put("task1.operator", new String[] { "1" });

		Order order = engine.startInstanceByName("simple", 0, 87L, args);

		List<Task> tasks = engine.task().findByOrderId(order.getId());
		assertNotNull(tasks);

		for (Task task : tasks)
			engine.executeTask(task.getId(), 87L, args);
	}

	// 提取任务
//	@Test
	public void testTake() {
//		Long id = deploy("test/task/take.xml");
//		assertNotNull(id);

		Map<String, Object> args = new HashMap<>();
		args.put("task1.operator", new String[] { "1" });

		Order order = engine.startInstanceById(155L, 2L, args);
		List<Task> tasks = engine.task().findByOrderId(order.getId());
		assertNotNull(tasks);

		for (Task task : tasks) {
			engine.task().take(task.getId(), 1L);
		}
	}

	// 转派任务 TODO
//	@Test
	public void testTransfer() {
//		deploy("test/task/transfer.xml");

		Order order = engine.startInstanceByName("transfer", 0, 1000L, null);

		List<Task> tasks = engine.task().findByOrderId(order.getId());

		for (Task task : tasks) {
//			engine.task().createNewTask(task.getId(), 0, 1000L);
//			engine.task().complete(task.getId(), null, null);
		}
	}

	// 驳回
//	@Test
	public void testReject() {
		init("test/task/reject.xml");
		engine.startInstanceById(engine.process().lastDeployProcessId, null, null);

		Map<String, Object> args = new HashMap<>();
		args.put("number", 2);
		engine.executeTask(1L, null, args);
		engine.executeAndJumpTask(1L, null, args, "task1");
	}

	// 唤醒
//	@Test
	public void testResume() {
		init("test/task/simple.xml");

		Map<String, Object> args = new HashMap<>();
		args.put("task1.operator", new String[] { "1" });

		Order order = engine.startInstanceByName("simple", null, 2L, args);
		List<Task> tasks = engine.task().findByOrderId(order.getId());

		for (Task task : tasks)
			engine.executeTask(task.getId(), 2L, args);

		engine.order().resume(order.getId());
	}

//	@Test
	public void testActor() {
		engine.task().addTaskActor(130L, 1L, 2L);
		engine.task().removeTaskActor(132L, 2L);
	}

	// 字段模型
//	@Test
	public void TestField() {
		Long processId = init("test/task/field.xml");

		Map<String, Object> args = new HashMap<>();
		args.put("task1.operator", new String[] { "1" });

		ProcessModel model = engine.process().findById(processId).getModel();
		TaskModel taskModel = (TaskModel) model.getNode("task1");

		assertNotNull(taskModel.getFields());
	}

//	@Test
	public void testModel() {
		Long processId = init("test/task/process.xml");

		Map<String, Object> args = new HashMap<>();
		args.put("task1.operator", new String[] { "1" });

		Order order = engine.startInstanceByName("simple", null, 2L, args);
		List<Task> tasks = engine.task().findByOrderId(order.getId());

		for (Task task : tasks) {
			TaskModel model = engine.task().getTaskModel(task.getId());
			System.out.println(model.getName());
			List<TaskModel> models = model.getNextModels(TaskModel.class);

			for (TaskModel tm : models) {
				System.out.println(tm.getName());
			}
		}

		List<TaskModel> models = engine.process().findById(processId).getModel().getModels(TaskModel.class);
		for (TaskModel tm : models) {
			System.out.println(tm.getName());
		}
	}

}
