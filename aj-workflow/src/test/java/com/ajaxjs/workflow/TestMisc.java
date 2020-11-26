package com.ajaxjs.workflow;

import static org.junit.Assert.assertNotNull;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.ajaxjs.util.ioc.ComponentMgr;
import com.ajaxjs.workflow.process.ProcessActive;
import com.ajaxjs.workflow.process.ProcessDefinition;
import com.ajaxjs.workflow.task.Task;

public class TestMisc extends BaseTest {

	public void testRead() {
		assertNotNull(service.findById(1L));
		assertNotNull(service.findById(1L));
		assertNotNull(service.findList());
		assertNotNull(service.findByName("simple"));
		assertNotNull(service.findByVersion("simple", 1));
	}

	@Test
	public void testStartOrder() {
		Map<String, Object> args = new HashMap<>();
		args.put("task1.operator", "1");

		WorkflowEngine engine = ComponentMgr.get(WorkflowEngine.class);
//		e.startInstanceByName("simple", 0, null, args);
		engine.startInstanceById(1L, 0L, args);
		service.undeploy(1L);
	}

	@Test
	public void TestQueryTask() {
//		System.out.println(queryService.getActiveTasks(new Page<Task>(), new QueryFilter().setOperator("1")));
//		System.out.println(queryService.getWorkItems(new Page<WorkItem>(), new QueryFilter().setOperator("1").setOrderId("36c0228fcfa740d5b62682dc954eaecd")));
	}

	@Test
	public void TestQueryOrder() {
//		Page<Order> page = new Page<Order>();
//		System.out.println(engine.query().getActiveOrders(new QueryFilter().setCreateTimeStart("2014-01-01").setProcessId("860e5edae536495a9f51937f435a1c01")));
//		System.out.println(engine.query().getActiveOrders(page, new QueryFilter()));
//		System.out.println(engine.query().getOrder("b2802224d75d4847ae5bfb0f7e621b8f"));
	}

	@Test
	public void TestQueryHistTask() {
//		System.out.println(queryService.getTaskHistorys(new Page<TaskHistory>(), new QueryFilter().setOperator("admin")));
//		System.out.println(queryService.getHistoryWorkItems(new Page<WorkItem>(), new QueryFilter().setOperator("admin")));
	}

	@Test
	public void TestQueryHistOrder() {
//		System.out.println(engine.query().getHistoryOrders(new QueryFilter().setCreateTimeStart("2014-01-01").setName("simple").setState(0).setProcessType("预算管理流程1")));
//		System.out.println(engine.query().getHistoryOrders(new Page<HistoryOrder>(), new QueryFilter()));
	}

	@Test
	public void TestQueryCCOrder() {
//		Page<HistoryOrder> page = new Page<HistoryOrder>();
//		System.out.println(engine.query().getCCWorks(page, new QueryFilter().setState(1)));
	}

	/**
	 * 实例编号自定义
	 */
	@Test
	public void testGenerator() {
		deploy("test/uitl/generator.xml");

		Map<String, Object> args = new HashMap<>();
		args.put("task1.operator", new String[] { "1" });

		WorkflowEngine engine = ComponentMgr.get(WorkflowEngine.class);
		ProcessActive active = engine.startInstanceByName("generator", null, WorkflowEngine.ADMIN, args);

		for (Task task : engine.task().findByActiveId(active.getId())) {
			engine.executeTask(task.getId(), 1L, null);
		}
	}

	/**
	 * 测试并发性能
	 */
	@Test
	public void TestConcurrent() {
		WorkflowEngine engine = ComponentMgr.get(WorkflowEngine.class);
		ProcessDefinition process = ComponentMgr.get(WorkflowEngine.class).process().findByName("simple");

		for (int i = 0; i < 50; i++) {
			new Thread(new StartProcess(engine, process.getId())).start();
		}
	}

}

class StartProcess implements Runnable {
	private WorkflowEngine engine;
	private Long processId;

	public StartProcess(WorkflowEngine engine, Long processId) {
		this.engine = engine;
		this.processId = processId;
	}

	@Override
	public void run() {
		Map<String, Object> args = new HashMap<>();
		args.put("task1.operator", new String[] { "1" });

		try {
			engine.startInstanceById(processId, 2L, args);// simple流程
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
