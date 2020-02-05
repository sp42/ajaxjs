package com.ajaxjs.cms;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.ajaxjs.cms.CommonEntityService.CommonEntityyDao;
import com.ajaxjs.cms.app.ArticleService;
import com.ajaxjs.framework.PageResult;
import com.ajaxjs.framework.Repository;
import com.ajaxjs.orm.JdbcConnection;

public class TestDomain {
	@Before
	public void setUp() {
		JdbcConnection.setConnection(JdbcConnection.getSqliteConnection("C:\\project\\wstsq\\WebContent\\META-INF\\database.sqlite"));
	}

	@After
	public void setEnd() throws SQLException {
		JdbcConnection.clean();
	}

	@Test
	public void testDao() {
		CommonEntityyDao dao = new Repository().bind(CommonEntityyDao.class, "entity_article");

		Map<String, Object> domain = dao.findById(1L);
		assertNotNull(domain);

		PageResult<Map<String, Object>> list = dao.findPagedList(0, 2, null);
		assertNotNull(list);

		domain = new HashMap<String, Object>();
		domain.put("name", "test");

		long newlyId = dao.create(domain);
		assertNotNull(newlyId);

		domain.put("name", "test22");
		domain.put("id",newlyId);

		int effect = dao.update(domain);
		assertEquals(1, effect);

		assertTrue(dao.delete(domain));
	}

	@Test
	public void testMapDao() {
		ArticleService.ArticleDao dao = new Repository().bind(ArticleService.ArticleDao.class);
		Map<String, Object> info = dao.findById(1L);

		List<Map<String, Object>> list = dao.findList(null);
		assertNotNull(list);

		info = new HashMap<String, Object>();
		info.put("name", "testMap");

		long newlyId = dao.create(info);
		assertNotNull(newlyId);

		info.put("name", "test22");
		info.put("id", newlyId);

		int effect = dao.update(info);
		assertEquals(1, effect);

		assertTrue(dao.delete(info));
	}

	@Test
	public void testMapService() {
	}
}
