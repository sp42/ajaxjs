package com.ajaxjs.workflow.service;

import com.ajaxjs.workflow.BaseTest;
import com.ajaxjs.workflow.common.WfData;
import com.ajaxjs.workflow.model.Args;
import com.ajaxjs.workflow.model.Execution;
import com.ajaxjs.workflow.model.ProcessModel;
import com.ajaxjs.workflow.model.node.work.TaskModel;
import com.ajaxjs.workflow.model.po.Order;
import com.ajaxjs.workflow.model.po.Task;
import com.ajaxjs.workflow.service.interceptor.WorkflowInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.List;
import java.util.function.BiFunction;

import static org.junit.Assert.assertNotNull;

public class TestWorkflow extends BaseTest {
    @Test
    public void testSimple() {
//		deploy("workflow/task/simple.xml");
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
    @Test
    public void testTake() {
//		Long id = deploy("workflow/task/take.xml");
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
    @Test
    public void testTransfer() {
//		deploy("workflow/task/transfer.xml");
        Order order = engine.startInstanceByName("transfer", 0, 1000L, null);
        List<Task> tasks = WfData.findTasksByOrderId(order.getId());

        for (Task task : tasks) {
//			engine.taskService.createNewTask(task.getId(), 0, 1000L);
//			engine.taskService.complete(task.getId(), null, null);
        }
    }

    // 驳回
    @Test
    public void testReject() {
        init("workflow/task/reject.xml");
        engine.startInstanceById(engine.processService.lastDeployProcessId, null, null);

        Args args = new Args();
        args.put("number", 2);
        engine.executeTask(1L, null, args);
        engine.executeAndJumpTask(1L, null, args, "task1");
    }

    // 唤醒
    @Test
    public void testResume() {
        init("workflow/task/simple.xml");

        Args args = new Args();
        args.put("task1.operator", new String[]{"1"});

        Order order = engine.startInstanceByName("simple", null, 2L, args);
        List<Task> tasks = WfData.findTasksByOrderId(order.getId());

        for (Task task : tasks)
            engine.executeTask(task.getId(), 2L, args);

        engine.orderService.resume(order.getId());
    }

    @Test
    public void testActor() {
        TaskService.addTaskActor(130L, 1L, 2L);
        TaskService.removeTaskActor(132L, 2L);
    }

    // 字段模型
    @Test
    public void TestField() {
        Long processId = init("workflow/task/field.xml");
        Args args = new Args();
        args.put("task1.operator", new String[]{"1"});

        ProcessModel model = engine.processService.findById(processId).getModel();
        TaskModel taskModel = (TaskModel) model.getNode("task1");

        assertNotNull(taskModel.getFields());
    }

    @Test
    public void testModel() {
        Long processId = init("workflow/task/process.xml");

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

    @Test
    public void testAssignmentHandler() {
        init("workflow/task/assignmenthandler/process.snaker");
        Order order = engine.startInstanceById(engine.processService.lastDeployProcessId, 2L, null);
        List<Task> tasks = WfData.findTasksByOrderId(order.getId());

        for (Task task : tasks) {
//			engine.executeTask(task.getId(), "admin");
            engine.executeTask(task.getId(), null, null);
        }
    }

    // 协办流程
    @Test
    public void testAidant() {
        init("workflow/task/aidant/process.snaker");
        Order order = engine.startInstanceById(engine.processService.lastDeployProcessId, 2L, null);
        List<Task> tasks = WfData.findTasksByOrderId(order.getId());

        for (Task task : tasks) {
//			engine.taskService.createNewTask(task.getId(), 1, 1000L);
        }
    }

    // 测试无权限执行任务
    @Test
    public void TestNotAllow() {
        init("workflow/task/right/process.snaker");
        Args args = new Args();
        args.put("task1.operator", new String[]{"2"});
        Order order = engine.startInstanceById(engine.processService.lastDeployProcessId, 2L, args);
        List<Task> tasks = WfData.findTasksByOrderId(order.getId());

        for (Task task : tasks) {
//			engine.executeTask(task.getId(), SnakerEngine.ADMIN, args);
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
        init("workflow/task/group/process.snaker");
        Args args = new Args();
        args.put("task1.operator", new String[]{"role1"});
        Order order = engine.startInstanceByName("group", 0, 2L, args);
        System.out.println("order=" + order);
        List<Task> tasks = WfData.findTasksByOrderId(order.getId());

        for (Task task : tasks) {
            // 操作人改为test时，角色对应test，会无权处理
            engine.executeTask(task.getId(), 1000L, args);
        }
    }

    @Slf4j
    public static class LocalTaskInterceptor implements WorkflowInterceptor {
        public void intercept(Execution execution) {
            log.info("LocalTaskInterceptor start...");

            for (Task task : execution.getTasks()) {
                StringBuilder buffer = new StringBuilder(100);
                buffer.append("创建任务[标识=").append(task.getId());
                buffer.append(",名称=").append(task.getDisplayName());
                buffer.append(",创建时间=").append(task.getCreateDate());
                buffer.append(",参与者={");

                if (task.getActorIds() != null) {
                    for (Long actor : task.getActorIds())
                        buffer.append(actor).append(";");
                }
                buffer.append("}]");
                log.info(buffer.toString());
            }

            log.info("LocalTaskInterceptor finish...");
        }
    }

//	@Before
//	public void before() {
//		processId = engine.processService.deploy(WorkflowUtils.getStreamFromClasspath("workflow/task/interceptor/process.snaker"));
//	}
//
//	@Test
//	public void testInterceptor() {
//		Order order = engine.startInstanceById(processId, "2");
//		System.out.println("order=" + order);
//		List<Task> tasks = queryService.getActiveTasks(new QueryFilter().setOrderId(order.getId()));
//		for (Task task : tasks) {
//			engine.executeTask(task.getId(), "1");
//		}
//	}
}
