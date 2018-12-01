package com.ajaxjs.cms.service;

import static org.junit.Assert.assertNotNull;

import java.sql.SQLException;
import java.util.Map;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.ajaxjs.cms.app.catelog.CatelogService;
import com.ajaxjs.ioc.BeanContext;
import com.ajaxjs.mock.DBConnection;
import com.ajaxjs.orm.JdbcConnection;
import com.ajaxjs.orm.dao.PageResult;

public class TestArticleService {

	@BeforeClass
	public static void initDb() {
		DBConnection.initTestDbAndIoc("c:\\project\\wyzx-pc\\src\\resources\\site_config.json", "com.ajaxjs.cms");
	}

	@Test
	public void testGetAllListByParentId() {
		CatelogService catalogService = (CatelogService) BeanContext.getBean("CatelogService");
		catalogService.getAllListByParentId(12);

		ArticleService articleService = (ArticleService) BeanContext.getBean("ArticleService");
		PageResult<Map<String, Object>> r = articleService.findPagedListByCatalogId(15, 0, 5);

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
