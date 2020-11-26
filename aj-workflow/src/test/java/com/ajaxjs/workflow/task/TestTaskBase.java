package com.ajaxjs.workflow.task;

import static org.junit.Assert.assertNotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.ajaxjs.util.ioc.ComponentMgr;
import com.ajaxjs.workflow.BaseTest;
import com.ajaxjs.workflow.WorkflowEngine;
import com.ajaxjs.workflow.model.ProcessModel;
import com.ajaxjs.workflow.model.TaskModel;
import com.ajaxjs.workflow.process.ProcessActive;

public class TestTaskBase extends BaseTest {
	@Test
	public void testModel() {
		Long processId = deploy("test/task/process.xml");

		Map<String, Object> args = new HashMap<>();
		args.put("task1.operator", new String[] { "1" });

		WorkflowEngine engine = ComponentMgr.get(WorkflowEngine.class);
		ProcessActive active = engine.startInstanceByName("simple", null, 2L, args);

		for (Task task : engine.task().findByActiveId(active.getId())) {
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

	// 字段模型
//	@Test
	public void TestField() {
		Long processId = deploy("test/task/field.xml");

		Map<String, Object> args = new HashMap<>();
		args.put("task1.operator", new String[] { "1" });

		WorkflowEngine engine = ComponentMgr.get(WorkflowEngine.class);
		ProcessModel model = engine.process().findById(processId).getModel();
		TaskModel taskModel = (TaskModel) model.getNode("task1");

		assertNotNull(taskModel.getFields());
	}

	/**
	 * 提取任务
	 */
//	@Test
	public void testTake() {
//		deploy("test/task/take.xml");

		Map<String, Object> args = new HashMap<>();
		args.put("task1.operator", new String[] { "1" });

		WorkflowEngine engine = ComponentMgr.get(WorkflowEngine.class);
		ProcessActive pa = engine.startInstanceByName("testTake", null, 1111L, args);
		List<Task> tasks = engine.task().findByActiveId(pa.getId());
		assertNotNull(tasks);

		for (Task task : tasks)
			engine.task().take(task.getId(), WorkflowEngine.ADMIN);
	}

}
