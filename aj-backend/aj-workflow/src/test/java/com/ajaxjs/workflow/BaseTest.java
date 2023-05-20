package com.ajaxjs.workflow;

import com.ajaxjs.util.io.Resources;
import com.ajaxjs.workflow.service.BaseWfService;
import com.ajaxjs.workflow.service.ProcessService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 基础测试类
 * 
 * @author Frank Cheung
 *
 */
public abstract class BaseTest extends BaseWfService {
	@Autowired
	protected ProcessService processService;

	@Autowired
	protected WorkflowEngine engine;

//	@Before
//	public void initDb() {
//		processService.setCacheManager(new MemoryCacheManager());
//	}

	/**
	 * 
	 * @param xmlPath 流程定义文件的路径
	 * @return
	 */
	public long init(String xmlPath) {
		String processXml = Resources.getResourceText(xmlPath);
		return processService.deploy(processXml, 1000L);
	}
}
