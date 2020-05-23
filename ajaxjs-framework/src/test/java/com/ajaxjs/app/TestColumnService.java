package com.ajaxjs.app;

import static org.junit.Assert.assertNotNull;

import java.sql.SQLException;
import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.ajaxjs.app.service.Column;
import com.ajaxjs.app.service.ColumnService;
import com.ajaxjs.framework.MockTest;
import com.ajaxjs.ioc.BeanContext;
import com.ajaxjs.orm.JdbcConnection;

public class TestColumnService {
	static ColumnService service;

	@BeforeClass
	public static void initDb() {
		MockTest.init2("D:\\project\\leidong", "com.ajaxjs.app");
		service = BeanContext.getBean(ColumnService.class);
	}

	@Test
	public void test() {
		List<Column> list = service.getListByCatalogId(30);
		
		System.out.println(list);
		assertNotNull(list);
	}

//	@Test
	public void testCreate() {
		assertNotNull(service);
		Column c = new Column();
		c.setCatalogId(30);
		c.setEntityUid(709360213836169216L);
		c.setTypeId(2);
		assertNotNull(service.create(c));

		c.setCatalogId(30);
		c.setEntityUid(644563412977713152L);
		c.setTypeId(1);
		assertNotNull(service.create(c));
	}

	@AfterClass
	public static void closeDb() {
		try {
			JdbcConnection.getConnection().close();
		} catch (SQLException e) {
		}

		JdbcConnection.clean();
	}
}
