package com.ajaxjs.workflow.Misc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import org.joda.time.DateTime;
import org.junit.Test;

import com.ajaxjs.util.logger.LogHelper;
import com.ajaxjs.workflow.BaseTest;
import com.ajaxjs.workflow.WorkflowEngine;
import com.ajaxjs.workflow.model.entity.Order;
import com.ajaxjs.workflow.model.entity.Task;
import com.ajaxjs.workflow.scheduling.JobCallback;

public class TestExpire extends BaseTest {
	private static final String PROCESSNAME = "expire";

	public static class TestCallback implements JobCallback {
		public static final LogHelper LOGGER = LogHelper.getLog(TestCallback.class);

		@Override
		public void callback(Long taskId, List<Task> newTasks) {
			LOGGER.info("callback taskId=" + taskId);
			LOGGER.info("newTasks=" + newTasks);
		}
	}

	@Test
	public void testExpire() {
//		System.out.println(DateHelper.parseTime(new DateTime(2014, 4, 6, 16, 41).toDate()));

		Map<String, Object> args = new HashMap<>();
		args.put("task1.operator", new String[] { "1" });
//		args.put("task1.expireTime", new DateTime(2014, 4, 15, 9, 0).toDate());
//		args.put("task1.reminderTime", new DateTime(2014, 4, 15, 8, 57).toDate());

		WorkflowEngine engine = (WorkflowEngine) init("test/timeExpire.xml");

		Order order = engine.startInstanceByName(PROCESSNAME, null, 2L, args);

//		List<Task> tasks = queryService.getActiveTasks(new QueryFilter().setOrderId(order.getId()));
//		for(Task task : tasks) {
//			engine.executeTask(task.getId(), "1", args);
//		}
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}