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
import com.ajaxjs.workflow.model.node.work.TaskModel;

@ContextConfiguration(classes = TestConfig.class)
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
public class TestTask extends BaseTest {
	@Autowired
	TaskService taskService;

	@Test
	public void testGetTaskModel() {
		TaskModel taskModel = taskService.getTaskModel(1L);
		assertNotNull(taskModel);
	}
}
