package com.ajaxjs.workflow;

import static org.junit.Assert.assertNotNull;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.ajaxjs.util.io.StreamHelper;
import com.ajaxjs.util.ioc.ComponentMgr;
import com.ajaxjs.workflow.process.ProcessActive;

public class TestProcess extends BaseTest {
//	@Test
	public void testDeploy() {
		String processXml = StreamHelper.byteStream2string(WorkflowUtils.getStreamFromClasspath("test/task/simple.xml"));
		long id = service.deploy(processXml, 1000L);
		assertNotNull(id);

		service.undeploy(id);
	}

	@Test
	public void testRead() {
		assertNotNull(service.findById(1L));
		assertNotNull(service.findById(2L));
		assertNotNull(service.findList());
		assertNotNull(service.findByName("simple"));
		assertNotNull(service.findByVersion("simple", 1));
	}

//	@Test
	public void testStartOrder() {
		WorkflowEngine engine = ComponentMgr.get(WorkflowEngine.class);

		Map<String, Object> args = new HashMap<>();
		args.put("task1.operator", "1");

		ProcessActive actProc = engine.startInstanceByName("simple", 0, null, args);
		assertNotNull(actProc);
//		engine.startInstanceById(79L, 0L, args);
//		service.undeploy(2L);
	}
}
