package com.ajaxjs.workflow;

import com.ajaxjs.data.jdbc_helper.JdbcConn;
import com.ajaxjs.framework.spring.filter.dbconnection.DataBaseConnection;
import com.ajaxjs.util.io.Resources;
import com.ajaxjs.workflow.common.WfConstant;
import com.ajaxjs.workflow.service.ProcessService;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * 基础测试类
 *
 * @author Frank Cheung
 */
@ContextConfiguration(classes = TestConfig.class)
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
public abstract class BaseTest implements WfConstant {
    @Autowired
    protected ProcessService processService;

    @Autowired
    protected WorkflowEngine engine;

    @Before
    public void initDb() {
//        processService.setCacheManager(new MemoryCacheManager());
        DataBaseConnection.initDb();
    }

    @After
    public void closeDB() {
        JdbcConn.closeDb();
    }

    public long init(String xmlPath) {
        String processXml = Resources.getResourceText(xmlPath);
        return processService.deploy(processXml, 1000L);
    }
}
