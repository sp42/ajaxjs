package com.ajaxjs.workflow.service;

import com.ajaxjs.workflow.BaseTest;
import com.ajaxjs.workflow.model.Args;
import com.ajaxjs.workflow.model.node.work.TaskModel;
import com.ajaxjs.workflow.model.po.Order;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertNotNull;


public class TestTask extends BaseTest {
    @Autowired
    TaskService taskService;

    @Test
    public void testGetTaskModel() {
        TaskModel taskModel = taskService.getTaskModel(1L);
        assertNotNull(taskModel);
    }

    @Test
    public void executeTask() {
        engine.executeTask(1L, 1L, null);
    }

    /**
     * 测试预配置参与者
     */
    @Test
    public void testActors() {
        init("workflow/task/config.xml");
        Order order = engine.startInstanceByName("config", 0, 1000L, null);
        assertNotNull(order);

        Args args = new Args();
        args.put("task1.operator", new String[]{"1"});
        engine.executeTask(1L, 1L, args);// 任务执行
    }
}
