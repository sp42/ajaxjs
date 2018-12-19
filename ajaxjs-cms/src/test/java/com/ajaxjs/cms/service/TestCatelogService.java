package com.ajaxjs.cms.service;

import static org.junit.Assert.assertNotNull;

import java.sql.SQLException;
import java.util.Map;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.ajaxjs.cms.app.catelog.Catelog;
import com.ajaxjs.cms.app.catelog.CatelogService;
import com.ajaxjs.cms.utils.CmsUtils;
import com.ajaxjs.ioc.BeanContext;
import com.ajaxjs.orm.JdbcConnection;
import com.ajaxjs.orm.dao.PageResult;

public class TestCatelogService {
	static CatelogService catalogService;

	@BeforeClass
	public static void initDb() {
		CmsUtils.initTestDbAndIoc("c:\\project\\wyzx-pc\\src\\resources\\site_config.json", "com.ajaxjs.cms");
		catalogService = (CatelogService) BeanContext.getBean("CatelogService");
	}

	// @Test
	public void test() {
		assertNotNull(catalogService);

		Catelog c = new Catelog();
		c.setName("foot2222");
		c.setPid(12);

		assertNotNull(catalogService.create(c));

		c = new Catelog();
		c.setName("bar2222");
		c.setPid(14);

		assertNotNull(catalogService.create(c));

		catalogService.findPagedList(0, 99);
	}

	@Test
	public void create() {
		Catelog c;
		c = new Catelog();
		c.setPid(-1);
		c.setName("test");
		assertNotNull(catalogService.create(c));

		c = new Catelog();
		c.setPid(119);
		c.setName("test-sub");
		assertNotNull(catalogService.create(c));
	}

	@Test
	public void testGetAllListByParentId() {
		CatelogService catalogService = (CatelogService) BeanContext.getBean("CatelogService");
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
