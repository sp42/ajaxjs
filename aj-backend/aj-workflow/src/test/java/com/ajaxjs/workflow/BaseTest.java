package com.ajaxjs.workflow;

import org.springframework.beans.factory.annotation.Autowired;

import com.ajaxjs.util.io.StreamHelper;
import com.ajaxjs.workflow.common.WfUtils;
import com.ajaxjs.workflow.service.BaseWfService;
import com.ajaxjs.workflow.service.ProcessService;

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
		String processXml = StreamHelper.byteStream2string(WfUtils.getStreamFromClasspath(xmlPath));
		return processService.deploy(processXml, 1000L);
	}
}
