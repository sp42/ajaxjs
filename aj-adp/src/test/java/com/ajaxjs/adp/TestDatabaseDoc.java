package com.ajaxjs.adp;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.ajaxjs.database_meta.DataBaseQuery;

@ContextConfiguration(classes = TestConfig.class)
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
public class TestDatabaseDoc {

	@Autowired
	DataSource ds;

	@Test
	public void test() throws SQLException {
		DataBaseQuery.saveToDiskJson(ds.getConnection(), "D:\\code\\ajaxjs\\aj-framework\\aj-ui-widget\\database-doc\\json.js");
	}
}
