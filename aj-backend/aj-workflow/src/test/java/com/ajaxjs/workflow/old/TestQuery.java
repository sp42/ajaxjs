package com.ajaxjs.workflow.old;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.ajaxjs.workflow.BaseTest;
import com.ajaxjs.workflow.model.Args;

public class TestQuery extends BaseTest {
	public void testRead() {
		assertNotNull(processService.findById(1L));
		assertNotNull(processService.findById(1L));
		assertNotNull(processService.findByName("simple"));
		assertNotNull(processService.findByVersion("simple", 1));
	}

	@Test
	public void testStartOrder() {
		Args args = new Args();
		args.put("task1.operator", "1");

//		e.startInstanceByName("simple", 0, null, args);
		engine.startInstanceById(1L, 0L, args);
		processService.undeploy(1L);
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
}
