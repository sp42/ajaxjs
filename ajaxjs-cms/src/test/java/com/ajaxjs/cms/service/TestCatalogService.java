package com.ajaxjs.cms.service;

import static org.junit.Assert.assertNotNull;

import java.sql.SQLException;
import java.util.Map;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.ajaxjs.cms.model.Catalog;
import com.ajaxjs.ioc.BeanContext;
import com.ajaxjs.mock.DBConnection;
import com.ajaxjs.orm.JdbcConnection;
import com.ajaxjs.orm.dao.PageResult;

public class TestCatalogService {
	static CatalogService catalogService;

	@BeforeClass
	public static void initDb() {
		DBConnection.initTestDbAndIoc("c:\\project\\wyzx-pc\\src\\resources\\site_config.json", "com.ajaxjs.cms");
		catalogService = (CatalogService) BeanContext.getBean("CatalogService");
	}

	// @Test
	public void test() {
		assertNotNull(catalogService);

		Catalog c = new Catalog();
		c.setName("foot2222");
		c.setPid(12);

		assertNotNull(catalogService.create(c));

		c = new Catalog();
		c.setName("bar2222");
		c.setPid(14);

		assertNotNull(catalogService.create(c));

		catalogService.findPagedList(0, 99);
	}

	@Test
	public void create() {
		Catalog c;
		c = new Catalog();
		c.setPid(-1);
		c.setName("test");
		assertNotNull(catalogService.create(c));

		c = new Catalog();
		c.setPid(119);
		c.setName("test-sub");
		assertNotNull(catalogService.create(c));
	}

	@Test
	public void testGetAllListByParentId() {
		CatalogService catalogService = (CatalogService) BeanContext.getBean("CatalogService");
		catalogService.getAllListByParentId(12);

		ArticleService articleService = (ArticleService) BeanContext.getBean("ArticleService");
		PageResult<Map<String, Object>> r = articleService.findPagedListByCatalogId(15, 0, 5);

		assertNotNull(r.size());
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
