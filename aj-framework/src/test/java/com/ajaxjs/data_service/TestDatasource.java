package com.ajaxjs.data_service;

import static org.junit.Assert.assertNotNull;

import java.sql.SQLException;
import java.util.Map;

import javax.sql.DataSource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.ajaxjs.data_service.api.MyDataSourceController;
import com.ajaxjs.framework.PageResult;
import com.ajaxjs.sql.JdbcConnection;

@ContextConfiguration(locations = { "classpath*:applicationContext.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
public class TestDatasource {
	@Autowired
	private MyDataSourceController controller;

	@Autowired
	DataSource ds;

	@Test
	public void test() throws ClassNotFoundException, SQLException {
		assertNotNull(controller);
		JdbcConnection.setConnection(ds.getConnection());
		long i = System.currentTimeMillis();
		PageResult<Map<String, Object>> tableAndComment = controller.getTableAndComment(2L, 0, 5, "trace", null);
		System.out.println(tableAndComment);
		System.out.println(System.currentTimeMillis() - i);

		JdbcConnection.closeDb();
	}
}
