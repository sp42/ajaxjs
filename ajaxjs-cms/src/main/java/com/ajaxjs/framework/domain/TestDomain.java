package com.ajaxjs.framework.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.sql.SQLException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.ajaxjs.cms.app.nativeapp.AppUpdate;
import com.ajaxjs.cms.app.nativeapp.AppUpdateServiceImpl;
import com.ajaxjs.framework.Repository;
import com.ajaxjs.orm.JdbcConnection;
import com.ajaxjs.orm.dao.PageResult;

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
	public void testService() {
		AppUpdateServiceImpl service = new AppUpdateServiceImpl();
		PageResult<AppUpdate> list = service.findPagedList(0, 10);
		assertNotNull(list);
		
		AppUpdate entry = service.findById(60L);
		assertNotNull(entry);
		
		entry = new AppUpdate();
		entry.setName("test");
		
		long newlyId = service.create(entry);
		assertNotNull(newlyId);

		entry.setName("test22");
		entry.setId(newlyId);
		
		int effect = service.update(entry);
		assertEquals(1, effect);

		assertTrue(service.delete(entry));
		
	}

	@Test
	public void testDao() {
		DomainRepository dao = new Repository().bind(DomainRepository.class);

		Domain domain = dao.findById(60L);
		assertNotNull(domain);

		PageResult<Domain> list = dao.findPagedListByCatelogId_Cover(6, 0, 10);
		assertNotNull(list);

		domain = new Domain();
		domain.setName("test");

		long newlyId = dao.create(domain);
		assertNotNull(newlyId);

		domain.setName("test22");
		domain.setId(newlyId);

		int effect = dao.update(domain);
		assertEquals(1, effect);

		assertTrue(dao.delete(domain));
	}
}
