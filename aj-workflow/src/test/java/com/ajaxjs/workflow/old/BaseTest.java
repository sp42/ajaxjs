package com.ajaxjs.workflow.old;

import org.junit.AfterClass;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;

import com.ajaxjs.sql.JdbcConnection;
import com.ajaxjs.util.cache.MemoryCacheManager;
import com.ajaxjs.util.io.StreamHelper;
import com.ajaxjs.workflow.WorkflowEngine;
import com.ajaxjs.workflow.common.WfUtils;
import com.ajaxjs.workflow.service.ProcessService;

/**
 * 基础测试类
 * 
 * @author Frank Cheung
 *
 */
public abstract class BaseTest {
	@Autowired
	ProcessService service;

	@Autowired
	protected WorkflowEngine engine;

	@Before
	public void initDb() {
		service.setCacheManager(new MemoryCacheManager());
	}

	/**
	 * 
	 * @param xmlPath 流程定义文件的路径
	 * @return
	 */
	public long deploy(String xmlPath) {
		String processXml = StreamHelper.byteStream2string(WfUtils.getStreamFromClasspath(xmlPath));
		return service.deploy(processXml, 1000L);
	}

	public Object init(String xml) {
		String processXml = StreamHelper.byteStream2string(WfUtils.getStreamFromClasspath(xml));
		service.deploy(processXml, 0L);

		return engine;
	}

	@AfterClass
	public static void closeDb() {
		JdbcConnection.closeDb();
	}
}
