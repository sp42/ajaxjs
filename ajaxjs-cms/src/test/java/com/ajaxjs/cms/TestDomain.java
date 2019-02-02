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

import com.ajaxjs.cms.app.nativeapp.AppUpdate;
import com.ajaxjs.cms.app.nativeapp.AppUpdateServiceImpl;
import com.ajaxjs.cms.domain.DomainEntityDao;
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

//	@Test
	public void testService() {
		AppUpdateServiceImpl service = new AppUpdateServiceImpl();
		PageResult<AppUpdate> list = service.findPagedList(0, 10);
		assertNotNull(list);

		AppUpdate entry = service.findById(60L);
		assertNotNull(entry);

	}

	@Test
	public void testDao() {
		DomainEntityDao dao = new Repository().bind(DomainEntityDao.class, "entity_article");

		Map<String, Object> domain = dao.findById(1L);
		assertNotNull(domain);

		PageResult<Map<String, Object>> list = dao.findPagedListByCatelogId_Cover(6, 0, 10);
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
		ArticleDao dao = new Repository().bind(ArticleDao.class);
		Map<String, Object> info = dao.findById(1L);
		System.out.println(info.get("id"));

		List<Map<String, Object>> list = dao.findList();
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
