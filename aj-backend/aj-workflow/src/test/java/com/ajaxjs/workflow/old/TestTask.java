package com.ajaxjs.workflow.old;

import static org.junit.Assert.assertNotNull;

import java.util.List;

import com.ajaxjs.workflow.BaseTest;
import com.ajaxjs.workflow.common.WfData;
import com.ajaxjs.workflow.model.Args;
import com.ajaxjs.workflow.model.ProcessModel;
import com.ajaxjs.workflow.model.node.work.TaskModel;
import com.ajaxjs.workflow.model.po.Order;
import com.ajaxjs.workflow.model.po.Task;
import com.ajaxjs.workflow.service.TaskService;

public class TestTask extends BaseTest {
    //	@Test
    public void testSimple() {
//		deploy("test/task/simple.xml");
//		engine.processService.updateType(processId, "预算管理流程");

        Args args = new Args();
        args.put("task1.operator", new String[]{"1"});

        Order order = engine.startInstanceByName("simple", 0, 87L, args);

        List<Task> tasks = WfData.findTasksByOrderId(order.getId());
        assertNotNull(tasks);

        for (Task task : tasks)
            engine.executeTask(task.getId(), 87L, args);
    }

    // 提取任务
//	@Test
    public void testTake() {
//		Long id = deploy("test/task/take.xml");
//		assertNotNull(id);

        Args args = new Args();
        args.put("task1.operator", new String[]{"1"});

        Order order = engine.startInstanceById(155L, 2L, args);
        List<Task> tasks = WfData.findTasksByOrderId(order.getId());
        assertNotNull(tasks);

        for (Task task : tasks) {
            engine.taskService.take(task.getId(), 1L);
        }
    }

    // 转派任务 TODO
//	@Test
    public void testTransfer() {
//		deploy("test/task/transfer.xml");
        Order order = engine.startInstanceByName("transfer", 0, 1000L, null);
        List<Task> tasks = WfData.findTasksByOrderId(order.getId());

        for (Task task : tasks) {
//			engine.taskService.createNewTask(task.getId(), 0, 1000L);
//			engine.taskService.complete(task.getId(), null, null);
        }
    }

    // 驳回
//	@Test
    public void testReject() {
        init("test/task/reject.xml");
        engine.startInstanceById(engine.processService.lastDeployProcessId, null, null);

        Args args = new Args();
        args.put("number", 2);
        engine.executeTask(1L, null, args);
        engine.executeAndJumpTask(1L, null, args, "task1");
    }

    // 唤醒
//	@Test
    public void testResume() {
        init("test/task/simple.xml");

        Args args = new Args();
        args.put("task1.operator", new String[]{"1"});

        Order order = engine.startInstanceByName("simple", null, 2L, args);
        List<Task> tasks = WfData.findTasksByOrderId(order.getId());

        for (Task task : tasks)
            engine.executeTask(task.getId(), 2L, args);

        engine.orderService.resume(order.getId());
    }

    //	@Test
    public void testActor() {
        TaskService.addTaskActor(130L, 1L, 2L);
        TaskService.removeTaskActor(132L, 2L);
    }

    // 字段模型
//	@Test
    public void TestField() {
        Long processId = init("test/task/field.xml");

        Args args = new Args();
        args.put("task1.operator", new String[]{"1"});

        ProcessModel model = engine.processService.findById(processId).getModel();
        TaskModel taskModel = (TaskModel) model.getNode("task1");

        assertNotNull(taskModel.getFields());
    }

    //	@Test
    public void testModel() {
        Long processId = init("test/task/process.xml");

        Args args = new Args();
        args.put("task1.operator", new String[]{"1"});

        Order order = engine.startInstanceByName("simple", null, 2L, args);
        List<Task> tasks = WfData.findTasksByOrderId(order.getId());

        for (Task task : tasks) {
            TaskModel model = engine.taskService.getTaskModel(task.getId());
            System.out.println(model.getName());
            List<TaskModel> models = model.getNextModels(TaskModel.class);

            for (TaskModel tm : models) {
                System.out.println(tm.getName());
            }
        }

        List<TaskModel> models = engine.processService.findById(processId).getModel().getModels(TaskModel.class);
        for (TaskModel tm : models) {
            System.out.println(tm.getName());
        }
    }

}
