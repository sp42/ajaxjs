package com.ajaxjs.cms.service;

import static org.junit.Assert.assertNotNull;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.ajaxjs.cms.model.Catalog;
import com.ajaxjs.cms.service.ArticleService;
import com.ajaxjs.cms.service.CatalogService;
import com.ajaxjs.config.ConfigService;
import com.ajaxjs.framework.dao.MockDataSource;
import com.ajaxjs.framework.dao.QueryParams;
import com.ajaxjs.framework.service.ServiceException;
import com.ajaxjs.ioc.BeanContext;
import com.ajaxjs.jdbc.PageResult;
import com.ajaxjs.orm.JdbcConnection;

public class TestCatalogService {
	static CatalogService catalogService;

	@BeforeClass
	public static void initDb() {
		ConfigService.load("c:\\project\\wyzx-pc\\src\\resources\\site_config.json");
		JdbcConnection.setConnection(MockDataSource.getTestMySqlConnection(ConfigService.getValueAsString("testServer.mysql.url"), ConfigService.getValueAsString("testServer.mysql.user"),
				ConfigService.getValueAsString("testServer.mysql.password")));
		BeanContext.init("com.ajaxjs.cms");
		catalogService = (CatalogService) BeanContext.getBean("CatalogService");
	}

	// @Test
	public void test() throws ServiceException {
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
	public void create() throws ServiceException {
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

//	@Test
	public void testGetAllListByParentId() throws ServiceException {
		CatalogService catalogService = (CatalogService) BeanContext.getBean("CatalogService");
		catalogService.getAllListByParentId(12);

		Map<String, String[]> inputMap = new HashMap<>();
		QueryParams qp = new QueryParams(inputMap);

		ArticleService articleService = (ArticleService) BeanContext.getBean("ArticleService");
		PageResult<Map<String, Object>> r = articleService.findPagedListByCatalogId(15, qp, 0, 5);

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
