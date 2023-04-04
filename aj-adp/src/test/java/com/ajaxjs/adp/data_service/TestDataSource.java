package com.ajaxjs.adp.data_service;

import static org.junit.Assert.assertNotNull;

import java.sql.SQLException;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.ajaxjs.adp.TestConfig;
import com.ajaxjs.data_service.model.DataSourceInfo;
import com.ajaxjs.framework.TestHelper;

@ContextConfiguration(classes = TestConfig.class)
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
public class TestDataSource {

	@Autowired
	private DataSourceController dataSourceController;

	@Test
	public void testDataSourceList() throws SQLException {
		List<DataSourceInfo> list = dataSourceController.list();
		assertNotNull(list.get(0));

		TestHelper.printJson(list);
	}
}
