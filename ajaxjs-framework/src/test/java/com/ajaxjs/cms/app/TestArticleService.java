package com.ajaxjs.cms.app;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.ajaxjs.cms.app.catalog.CatalogService;
import com.ajaxjs.cms.utils.CmsUtils;
import com.ajaxjs.framework.PageResult;
import com.ajaxjs.ioc.BeanContext;
import com.ajaxjs.mvc.controller.MvcRequest;
import com.ajaxjs.orm.JdbcConnection;
import com.ajaxjs.web.mock.MockRequest;

public class TestArticleService {

	@BeforeClass
	public static void initDb() {
		CmsUtils.initTestDbAndIoc("c:\\project\\wyzx-pc\\src\\resources\\site_config.json", "com.ajaxjs.cms");
	}

	@Test
	public void testGetAllListByParentId() {
		CatalogService catalogService = (CatalogService) BeanContext.getBean("闪亮杯国际少儿音乐大赛");
		catalogService.findAllListByParentId(12);

		ArticleService articleService = (ArticleService) BeanContext.getBean("ArticleService");
		PageResult<Map<String, Object>> r = articleService.findPagedListByCatelogId(15, 0, 5);
		
		

		assertNotNull(r.size());
		
		HttpServletRequest request = MockRequest.mockRequest("/test", "/foo?searchField=name&searchValue=店长");
		
		Map<String, String[]> inputMap = new HashMap<>();
		{
			inputMap.put("searchField", new String[] { "name", "content" });
			inputMap.put("searchValue", new String[] { "name", "jj" });
		}
		when(request.getParameterMap()).thenReturn(inputMap);
		
		MvcRequest.setHttpServletRequest(request);
		r = articleService.findPagedListByCatelogId(15, 0, 5);
		assertNotNull(r.size());
	}

	@Test
	public void getInfo() {
		ArticleService articleService = (ArticleService) BeanContext.getBean("ArticleService");
		articleService.findById(3L);
	}

	@AfterClass
	public static void closeDb() {
		try {
			JdbcConnection.getConnection().close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		JdbcConnection.clean();
	}
}