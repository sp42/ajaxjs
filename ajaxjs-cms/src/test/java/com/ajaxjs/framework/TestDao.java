package com.ajaxjs.framework;

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
import com.ajaxjs.mock.DBConnection;
import com.ajaxjs.orm.JdbcConnection;
import com.ajaxjs.orm.dao.DaoHandler;
import com.ajaxjs.orm.dao.PageResult;
import com.ajaxjs.orm.dao.QueryParams;

public class TestDao {
	ArticleDao dao;

	@Before
	public void setUp() {
		DBConnection.initTestConnection("C:\\project\\wyzx-pc\\src\\resources\\site_config.json");
		dao = new DaoHandler<ArticleDao>().bind(ArticleDao.class);
	}

	@After
	public void setEnd() throws SQLException {
		JdbcConnection.clean();
	}

	

	Map<String, String[]> inputMap = new HashMap<>();
	{
		inputMap.put("filterField", new String[] { "status", "catelog" });
		inputMap.put("filterValue", new String[] { "2", "17" });
	}

	@Test
	public void testFindList() {
		List<Map<String, Object>> newsList;
		newsList = dao.findList();
		assertEquals(214, newsList.size());

		newsList = dao.findList(new QueryParams(inputMap));
		assertNotNull(newsList);
	}

	@Test
	public void testPageFindList() {
		PageResult<Map<String, Object>> newsList;

		newsList = dao.findPagedList(0, 10);
		assertEquals(newsList.size(), 10);

//		newsList = dao.findPagedList(new QueryParams(), 0, 10);
//		assertNotNull(newsList);
	}


}
