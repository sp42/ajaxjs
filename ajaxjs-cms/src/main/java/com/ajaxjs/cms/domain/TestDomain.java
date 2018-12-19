package com.ajaxjs.cms.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.sql.SQLException;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.ajaxjs.cms.app.nativeapp.AppUpdate;
import com.ajaxjs.cms.app.nativeapp.AppUpdateServiceImpl;
import com.ajaxjs.cms.dao.HrDao;
import com.ajaxjs.framework.EntityMap;
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

		DomainEntity domain = dao.findById(1L);
		assertNotNull(domain);

		PageResult<DomainEntity> list = dao.findPagedListByCatelogId_Cover(6, 0, 10);
		assertNotNull(list);

		domain = new DomainEntity();
		domain.setName("test");

		long newlyId = dao.create(domain);
		assertNotNull(newlyId);

		domain.setName("test22");
		domain.setId(newlyId);

		int effect = dao.update(domain);
		assertEquals(1, effect);

		assertTrue(dao.delete(domain));
	}

	@Test
	public void testMapDao() {
		HrDao dao = new Repository().bind(HrDao.class);
		EntityMap info = dao.findById(1L);
		System.out.println(info.getId());
		
		List<EntityMap> list = dao.findList();
		assertNotNull(list);
		
		info = new EntityMap();
		info.put("name","testMap");
		
		long newlyId = dao.create(info);
		assertNotNull(newlyId);
		
		info.put("name", "test22");
		info.setId(newlyId);
		
		int effect = dao.update(info);
		assertEquals(1, effect);

		assertTrue(dao.delete(info));
	}
	
	@Test
	public void testMapService() {}
}
