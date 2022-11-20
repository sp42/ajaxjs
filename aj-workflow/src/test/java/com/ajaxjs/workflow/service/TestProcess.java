package com.ajaxjs.workflow.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.ajaxjs.util.io.StreamHelper;
import com.ajaxjs.workflow.BaseTest;
import com.ajaxjs.workflow.TestConfig;
import com.ajaxjs.workflow.common.WfUtils;
import com.ajaxjs.workflow.model.po.Order;
import com.ajaxjs.workflow.model.po.ProcessPO;

@ContextConfiguration(classes = TestConfig.class)
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
public class TestProcess extends BaseTest {
//	@Test
	public void testCRUD() {
		List<ProcessPO> list = ProcessDAO.findList();

		assertNotNull(list.get(0));

		ProcessPO process1 = processService.findById(1L);
		ProcessPO process2 = processService.findById(1L);

		assertNotNull(process1);
		assertEquals(process1, process2);
	}

//	@Test
	public void testDeploy() {
		String processXml = StreamHelper.byteStream2string(WfUtils.getStreamFromClasspath("workflow/task/simple.xml"));
		long id = processService.deploy(processXml, 1000L);
		assertNotNull(id);
	}

//	@Test
	public void testRead() {
		assertNotNull(processService.findById(1L));
		assertNotNull(processService.findById(2L));
		assertNotNull(processService.findByName("simple"));
		assertNotNull(processService.findByVersion("simple", 0));
	}

	@Test
	public void testStartOrder() {
		Map<String, Object> args = new HashMap<>();
		args.put("task1.operator", "1");

		Order order = engine.startInstanceByName("simple", 0, null, args);
		assertNotNull(order);
//		engine.startInstanceById(1L, 1L, args);

	}

//	@Test
	public void testUndeploy() {
		processService.undeploy(1L);
	}
}
