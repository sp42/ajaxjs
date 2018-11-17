package com.ajaxjs.cms.dao;

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

import com.ajaxjs.cms.dao.ArticleDao;
import com.ajaxjs.config.ConfigService;
import com.ajaxjs.framework.dao.MockDataSource;
import com.ajaxjs.orm.JdbcConnection;
import com.ajaxjs.orm.dao.DaoHandler;
import com.ajaxjs.orm.dao.PageResult;
import com.ajaxjs.orm.dao.QueryParams;


public class TestDao {
	ArticleDao dao;

	@Before
	public void setUp() {
		ConfigService.load("C:\\project\\wyzx-pc\\src\\resources\\site_config.json");
		JdbcConnection.setConnection(MockDataSource.getTestMySqlConnection(ConfigService.getValueAsString("testServer.mysql.url"), ConfigService.getValueAsString("testServer.mysql.user"),
				ConfigService.getValueAsString("testServer.mysql.password")));
		
		dao = new DaoHandler<ArticleDao>().bind(ArticleDao.class);
	}

	@After
	public void setEnd() throws SQLException {
		JdbcConnection.clean();
	}

	@Test
	public void testFindById() {
		Map<String, Object> article = dao.findById(353L);
		assertNotNull(article);
	}
	
	Map<String, String[]> inputMap = new HashMap<>();
	{
		inputMap.put("filterField", new String[]{"status", "catelog"});
		inputMap.put("filterValue", new String[]{"2", "17"});
	}

	@Test
	public void testFindList() {
		List<Map<String, Object>> newsList;
		newsList = dao.findList();
		assertEquals(newsList.size(), 209);
		
		newsList = dao.findList(new QueryParams(inputMap));
		assertNotNull(newsList);
	}

	@Test
	public void testPageFindList() {
		PageResult<Map<String, Object>> newsList;
		
		newsList = dao.findPagedList(0, 10);
		assertEquals(newsList.size(), 10);
		
		newsList = dao.findPagedList(new QueryParams(), 0, 10);
		assertNotNull(newsList);
	}


//	@Test
	public void testCreateUpdateDelete() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("name", "test 123");
		Long newlyId = dao.create(map);
		assertNotNull(newlyId);

		map.put("name", "test 8888888");
		
		assertEquals(1, dao.update(map));
		assertTrue(dao.delete(map));
	}
}
