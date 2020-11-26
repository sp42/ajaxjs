package com.ajaxjs.workflow.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import org.joda.time.DateTime;
import org.junit.Test;

import com.ajaxjs.util.ioc.ComponentMgr;
import com.ajaxjs.util.logger.LogHelper;
import com.ajaxjs.workflow.BaseTest;
import com.ajaxjs.workflow.WorkflowEngine;
import com.ajaxjs.workflow.process.ProcessActive;
import com.ajaxjs.workflow.task.Task;
import com.ajaxjs.workflow.util.scheduling.JobCallback;

public class TestExpire extends BaseTest {
	public static class TestCallback implements JobCallback {
		private static final LogHelper LOGGER = LogHelper.getLog(TestCallback.class);

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

		deploy("test/util/timeExpire.xml");

		WorkflowEngine engine = ComponentMgr.get(WorkflowEngine.class);
		ProcessActive active = engine.startInstanceByName("expire", null, WorkflowEngine.ADMIN, args);

		for (Task task : engine.task().findByActiveId(active.getId())) {
			engine.executeTask(task.getId(), 1L, args);
		}

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}