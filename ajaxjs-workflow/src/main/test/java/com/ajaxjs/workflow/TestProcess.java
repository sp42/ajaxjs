package com.ajaxjs.workflow;

import static org.junit.Assert.assertNotNull;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.ajaxjs.cms.utils.CmsUtils;
import com.ajaxjs.ioc.BeanContext;
import com.ajaxjs.orm.JdbcConnection;
import com.ajaxjs.util.cache.MemoryCacheManager;
import com.ajaxjs.util.io.IoHelper;
import com.ajaxjs.workflow.service.ProcessService;

public class TestProcess {
	static ProcessService service;

	@BeforeClass
	public static void initDb() {
		CmsUtils.init("C:\\project\\snaker-springmvc\\src\\main\\resources\\site_config.json",
				"C:\\project\\snaker-springmvc\\src\\main\\webapp\\META-INF\\context.xml", "com.ajaxjs.workflow");
		service = BeanContext.getBean(ProcessService.class);
		service.setCacheManager(new MemoryCacheManager());
		assertNotNull(service);
	}

	public void testDeploy() {
		String processXml = IoHelper.byteStream2string(WorkflowUtils.getStreamFromClasspath("test/task/simple/process.snaker"));
		long id = service.deploy(processXml);
		assertNotNull(id);
	}

	@Test
	public void testRead() {
		assertNotNull(service.findById(1L));
		assertNotNull(service.findById(1L));
		assertNotNull(service.findList());
		assertNotNull(service.findByName("simple"));
		assertNotNull(service.findByVersion("simple", 1));
	}
	
	public void testUndeploy() {
		service.undeploy(2L);
	}

	@AfterClass
	public static void closeDb() {
		JdbcConnection.closeDb();
	}
}
