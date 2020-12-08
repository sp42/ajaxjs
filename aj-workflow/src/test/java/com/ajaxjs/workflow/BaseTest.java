package com.ajaxjs.workflow;

import java.util.Objects;

import org.junit.AfterClass;
import org.junit.BeforeClass;

import com.ajaxjs.TestHelper;
import com.ajaxjs.sql.JdbcConnection;
import com.ajaxjs.util.cache.MemoryCacheManager;
import com.ajaxjs.util.io.IoHelper;
import com.ajaxjs.util.ioc.ComponentMgr;
import com.ajaxjs.workflow.process.service.ProcessDefinitionService;

public class BaseTest {
	protected static ProcessDefinitionService service;

	@BeforeClass
	public static void initDb() {
		TestHelper.init("C:\\project\\workflow\\demo\\WebContent\\META-INF\\site_config.json", "C:\\project\\workflow\\demo\\WebContent\\META-INF\\context.xml",
				"com.ajaxjs");

		service = ComponentMgr.get(ProcessDefinitionService.class);
		Objects.requireNonNull(service);
		service.setCacheManager(new MemoryCacheManager());
	}

	/**
	 * 
	 * @param xmlPath 流程定义文件的路径
	 * @return
	 */
	public static long deploy(String xmlPath) {
		String processXml = IoHelper.byteStream2string(WorkflowUtils.getStreamFromClasspath(xmlPath));
		return service.deploy(processXml, 1000L);
	}

	@AfterClass
	public static void closeDb() {
		JdbcConnection.closeDb();
	}
}
