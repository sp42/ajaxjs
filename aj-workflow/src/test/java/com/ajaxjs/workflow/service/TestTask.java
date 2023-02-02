package com.ajaxjs.workflow.service;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.ajaxjs.workflow.BaseTest;
import com.ajaxjs.workflow.TestConfig;
import com.ajaxjs.workflow.model.Args;
import com.ajaxjs.workflow.model.node.work.TaskModel;
import com.ajaxjs.workflow.model.po.Order;

@ContextConfiguration(classes = TestConfig.class)
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
public class TestTask extends BaseTest {
	@Autowired
	TaskService taskService;

	public void testGetTaskModel() {
		TaskModel taskModel = taskService.getTaskModel(1L);
		assertNotNull(taskModel);
	}

	@Test
	public void executeTask() {
		engine.executeTask(3l, 1l, null);
	}

	/**
	 * 测试预配置参与者
	 */
	public void testActors() {
		init("test/task/config.xml");
		Order order = engine.startInstanceByName("config", 0, 1000L, null);
		assertNotNull(order);

		Args args = new Args();
		args.put("task1.operator", new String[] { "1" });
		engine.executeTask(1L, 1L, args);// 任务执行
	}
}
