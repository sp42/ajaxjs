package com.ajaxjs.website;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.ajaxjs.TestHelper;
import com.ajaxjs.sql.JdbcConnection;
import com.ajaxjs.sql.orm.PageResult;
import com.ajaxjs.util.ioc.ComponentMgr;
import com.ajaxjs.web.mock.MockRequest;
import com.ajaxjs.web.mvc.MvcRequest;
import com.ajaxjs.website.service.ArticleService;

public class TestArticleService {

	@BeforeClass
	public static void initDb() {
		TestHelper.initAll();
	}

	@Test
	public void testGetAllListByParentId() {
		ArticleService articleService = (ArticleService) ComponentMgr.get("ArticleService");
		PageResult<Map<String, Object>> r = articleService.list(15, 0, 5, 1);

		assertNotNull(r.size());

		HttpServletRequest request = MockRequest.mockRequest("/test", "/foo?searchField=name&searchValue=店长");

		Map<String, String[]> inputMap = new HashMap<>();
		{
			inputMap.put("searchField", new String[] { "name", "content" });
			inputMap.put("searchValue", new String[] { "name", "jj" });
		}

		when(request.getParameterMap()).thenReturn(inputMap);

		MvcRequest.setHttpServletRequest(request);
		r = articleService.list(15, 0, 5, 1);
		assertNotNull(r.size());
	}

	@Test
	public void getInfo() {
		ArticleService articleService = (ArticleService) ComponentMgr.get("ArticleService");
		articleService.findById(3L);
	}

	@AfterClass
	public static void closeDb() {
		JdbcConnection.closeDb();
	}
}