package com.ajaxjs.cms.app;

import static org.junit.Assert.assertNotNull;

import java.sql.SQLException;
import java.util.Map;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.ajaxjs.cms.app.catalog.Catalog;
import com.ajaxjs.cms.app.catalog.CatalogService;
import com.ajaxjs.cms.utils.CmsUtils;
import com.ajaxjs.framework.PageResult;
import com.ajaxjs.ioc.BeanContext;
import com.ajaxjs.orm.JdbcConnection;

public class TestCatalogService {
	static CatalogService catalogService;

	@BeforeClass
	public static void initDb() {
		CmsUtils.initTestDbAndIoc("c:\\project\\wyzx-pc\\src\\resources\\site_config.json", "com.ajaxjs.cms");
		catalogService = (CatalogService) BeanContext.getBean("闪亮杯国际少儿音乐大赛");
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
		CatalogService catalogService = (CatalogService) BeanContext.getBean("闪亮杯国际少儿音乐大赛");
		catalogService.findAllListByParentId(12);

		ArticleService articleService = (ArticleService) BeanContext.getBean("ArticleService");
		PageResult<Map<String, Object>> r = articleService.list(15, 0, 5, 1);

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
