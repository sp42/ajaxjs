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
import com.ajaxjs.framework.service.ServiceException;
import com.ajaxjs.ioc.BeanContext;
import com.ajaxjs.orm.JdbcConnection;
import com.ajaxjs.orm.dao.PageResult;
import com.ajaxjs.orm.dao.QueryParams;

public class TestArticleService {

	@BeforeClass
	public static void initDb() {
		ConfigService.load("C:\\project\\wyzx-pc\\src\\main\\site_config.json");
		JdbcConnection.setConnection(MockDataSource.getTestMySqlConnection(ConfigService.getValueAsString("testServer.mysql.url"), ConfigService.getValueAsString("testServer.mysql.user"),
				ConfigService.getValueAsString("testServer.mysql.password")));
		BeanContext.init("com.ajaxjs.cms");
	}

	// @Test
	public void test() {
		CatalogService catalogService = (CatalogService) BeanContext.getBean("CatalogService");

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
	public void testGetAllListByParentId() {
		CatalogService catalogService = (CatalogService) BeanContext.getBean("CatalogService");
		catalogService.getAllListByParentId(12);

		Map<String, String[]> inputMap = new HashMap<>();
		QueryParams qp = new QueryParams(inputMap);

		ArticleService articleService = (ArticleService) BeanContext.getBean("ArticleService");
		PageResult<Map<String, Object>> r = articleService.findPagedListByCatalogId(15, qp, 0, 5);

		assertNotNull(r.size());
	}

	@Test
	public void getInfo() {
		ArticleService articleService = (ArticleService) BeanContext.getBean("ArticleService");
		articleService.findById(3L);
	}

	@AfterClass
	public static void closeDb() {
		try {
			JdbcConnection.getConnection().close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		JdbcConnection.clean();
	}
}
