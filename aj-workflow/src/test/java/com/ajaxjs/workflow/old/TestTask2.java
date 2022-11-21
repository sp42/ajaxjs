package com.ajaxjs.workflow.old;

import java.util.List;
import java.util.function.BiFunction;

import org.junit.Test;

import com.ajaxjs.util.logger.LogHelper;
import com.ajaxjs.workflow.BaseTest;
import com.ajaxjs.workflow.model.Args;
import com.ajaxjs.workflow.model.Execution;
import com.ajaxjs.workflow.model.node.work.TaskModel;
import com.ajaxjs.workflow.model.po.Order;
import com.ajaxjs.workflow.model.po.Task;
import com.ajaxjs.workflow.service.interceptor.WorkflowInterceptor;

public class TestTask2 extends BaseTest {
	@Test
	public void testAssignmentHandler() {
		init("test/task/assignmenthandler/process.snaker");
		Order order = engine.startInstanceById(engine.processService.lastDeployProcessId, 2L, null);
		List<Task> tasks = engine.taskService.findByOrderId(order.getId());

		for (Task task : tasks) {
//			engine.executeTask(task.getId(), "admin");
			engine.executeTask(task.getId(), null, null);
		}
	}

	// 协办流程
	@Test
	public void testAidant() {
		init("test/task/aidant/process.snaker");
		Order order = engine.startInstanceById(engine.processService.lastDeployProcessId, 2L, null);
		List<Task> tasks = engine.taskService.findByOrderId(order.getId());

		for (Task task : tasks) {
//			engine.taskService.createNewTask(task.getId(), 1, 1000L);
		}
	}

	// 测试无权限执行任务
	@Test
	public void TestNotAllow() {
		init("test/task/right/process.snaker");
		Args args = new Args();
		args.put("task1.operator", new String[] { "2" });
		Order order = engine.startInstanceById(engine.processService.lastDeployProcessId, 2L, args);
		List<Task> tasks = engine.taskService.findByOrderId(order.getId());

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
		init("test/task/group/process.snaker");
		Args args = new Args();
		args.put("task1.operator", new String[] { "role1" });
		Order order = engine.startInstanceByName("group", 0, 2L, args);
		System.out.println("order=" + order);
		List<Task> tasks = engine.taskService.findByOrderId(order.getId());

		for (Task task : tasks) {
			// 操作人改为test时，角色对应test，会无权处理
			engine.executeTask(task.getId(), 1000L, args);
		}
	}

	public static class LocalTaskInterceptor implements WorkflowInterceptor {
		public static final LogHelper LOGGER = LogHelper.getLog(LocalTaskInterceptor.class);

		public void intercept(Execution execution) {
			LOGGER.info("LocalTaskInterceptor start...");

			for (Task task : execution.getTasks()) {
				StringBuffer buffer = new StringBuffer(100);
				buffer.append("创建任务[标识=").append(task.getId());
				buffer.append(",名称=").append(task.getDisplayName());
				buffer.append(",创建时间=").append(task.getCreateDate());
				buffer.append(",参与者={");

				if (task.getActorIds() != null) {
					for (Long actor : task.getActorIds()) {
						buffer.append(actor).append(";");
					}
				}
				buffer.append("}]");
				LOGGER.info(buffer.toString());
			}

			LOGGER.info("LocalTaskInterceptor finish...");
		}

	}

//	@Before
//	public void before() {
//		processId = engine.processService.deploy(WorkflowUtils.getStreamFromClasspath("test/task/interceptor/process.snaker"));
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
