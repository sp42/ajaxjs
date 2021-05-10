	package com.ajaxjs.entity;

import static org.junit.Assert.assertNotNull;

import java.util.Map;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.ajaxjs.TestHelper;
import com.ajaxjs.entity.model.Catalog;
import com.ajaxjs.entity.service.TreeLikeService;
import com.ajaxjs.sql.JdbcConnection;
import com.ajaxjs.sql.orm.PageResult;
import com.ajaxjs.util.ioc.ComponentMgr;
import com.ajaxjs.website.service.ArticleService;

public class TestCatalogService {
	static TreeLikeService catalogService;

	@BeforeClass
	public static void initDb() {
		TestHelper.initAll();
		catalogService = (TreeLikeService) ComponentMgr.get("闪亮杯国际少儿音乐大赛");
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
		TreeLikeService catalogService = (TreeLikeService) ComponentMgr.get("闪亮杯国际少儿音乐大赛");
		catalogService.getAllChildren(12);

		ArticleService articleService = (ArticleService) ComponentMgr.get("ArticleService");
		PageResult<Map<String, Object>> r = articleService.list(15, 0, 5, 1);

		assertNotNull(r.size());
	}

	@AfterClass
	public static void closeDb() {
		JdbcConnection.closeDb();
	}
}
