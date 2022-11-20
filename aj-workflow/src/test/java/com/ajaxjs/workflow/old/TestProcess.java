package com.ajaxjs.workflow.old;

import static org.junit.Assert.assertNotNull;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.ajaxjs.util.io.StreamHelper;
import com.ajaxjs.workflow.common.WfUtils;
import com.ajaxjs.workflow.model.po.OrderPO;

public class TestProcess extends BaseTest {
//	@Test
	public void testDeploy() {
		String processXml = StreamHelper.byteStream2string(WfUtils.getStreamFromClasspath("test/task/simple.xml"));
		long id = service.deploy(processXml, 1000L);
		assertNotNull(id);
	}

//	@Test
	public void testRead() {
		assertNotNull(service.findById(1L));
		assertNotNull(service.findById(2L));
		assertNotNull(service.findList());
		assertNotNull(service.findByName("simple"));
		assertNotNull(service.findByVersion("simple", 1));
	}

	@Test
	public void testStartOrder() {
		Map<String, Object> args = new HashMap<>();
		args.put("task1.operator", "1");

		OrderPO order = engine.startInstanceByName("simple", 0, null, args);
		assertNotNull(order);
//		engine.startInstanceById(79L, 0L, args);
//		service.undeploy(2L);
	}
}
