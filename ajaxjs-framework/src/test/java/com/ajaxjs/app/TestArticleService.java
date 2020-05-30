package com.ajaxjs.app;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.ajaxjs.app.catalog.CatalogService;
import com.ajaxjs.framework.PageResult;
import com.ajaxjs.framework.config.TestHelper;
import com.ajaxjs.ioc.BeanContext;
import com.ajaxjs.mvc.controller.MvcRequest;
import com.ajaxjs.orm.JdbcConnection;
import com.ajaxjs.web.mock.MockRequest;

public class TestArticleService {

	@BeforeClass
	public static void initDb() {
		TestHelper.initAll();
	}

	@Test
	public void testGetAllListByParentId() {
		CatalogService catalogService = (CatalogService) BeanContext.getBean("闪亮杯国际少儿音乐大赛");
		catalogService.findAllListByParentId(12);

		ArticleService articleService = (ArticleService) BeanContext.getBean("ArticleService");
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
		ArticleService articleService = (ArticleService) BeanContext.getBean("ArticleService");
		articleService.findById(3L);
	}

	@AfterClass
	public static void closeDb() {
		JdbcConnection.closeDb();
	}
}
