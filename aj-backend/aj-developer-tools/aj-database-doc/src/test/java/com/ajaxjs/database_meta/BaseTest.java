package com.ajaxjs.database_meta;

import com.ajaxjs.data.jdbc_helper.JdbcConn;
import com.ajaxjs.framework.spring.filter.dbconnection.DataBaseConnection;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@ContextConfiguration(classes = BaseTestConfig.class)
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
public abstract class BaseTest {
    @Before
    public void initDb() {
        DataBaseConnection.initDb();
    }

    @After
    public void closeDb() {
        JdbcConn.closeDb();
    }
}