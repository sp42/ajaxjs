package com.ajaxjs.data_service;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.ajaxjs.data_service.controller.BaseDataServiceApiController;
import com.ajaxjs.data_service.service.DataService;
import com.ajaxjs.util.map.JsonHelper;

@ContextConfiguration(locations = { "classpath*:applicationContext.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
public class TestApi {

	class Controller extends BaseDataServiceApiController {

	}

	@Autowired
	private DataService ds;

	Controller c = new Controller();

	HttpServletRequest request;

	@Before
	public void init() {
		c.setDataService(ds);
		ds.init();

		request = mock(HttpServletRequest.class);
		when(request.getContentType()).thenReturn("boo");
		when(request.getContextPath()).thenReturn("boo");
	}

	String result;
	Map<String, Object> map;

	@Test
	public void testInfo() {
		when(request.getRequestURI()).thenReturn("cms/user_login_log");
		when(request.getQueryString()).thenReturn("id=1");
		result = c.get(request).toString();
		System.out.println(result);
		map = JsonHelper.parseMap(result);
		assertNotNull(map.get("result"));
	}

	@Test
	@SuppressWarnings("unchecked")
	public void testList() {
		when(request.getRequestURI()).thenReturn("cms/user_login_log/list");
		result = c.get(request).toString();
		System.out.println(result);
		Map<String, Object> map = JsonHelper.parseMap(result);

		List<Map<String, Object>> list;
		list = (List<Map<String, Object>>) map.get("result");
		assertNotNull(list.get(0).get("userId"));

		when(request.getQueryString()).thenReturn("start=2&limit=10");
		result = c.get(request).toString();
		list = (List<Map<String, Object>>) map.get("result");
		assertNotNull(list.get(0).get("userId"));
	}

	@Test
	public void testWrite() {
		// create
		when(request.getRequestURI()).thenReturn("cms/user_login_log");
		Map<String, Object> params = new HashMap<>();
		params.put("userId", 11223);
		params.put("ipLocation", "广州");

		String result = c.post(params, request).toString();
		System.out.println(result);
		Map<String, Object> map = JsonHelper.parseMap(result);

		int newlyId = (int) map.get("newlyId");
		assertTrue((boolean) map.get("isOk"));

		// update
		Map<String, Object> params2 = new HashMap<>();
		params2.put("id", newlyId);
		params2.put("userId", 33333);
		result = c.put(null, request).toString();
		System.out.println(result);

		// delete
		when(request.getQueryString()).thenReturn("id=" + newlyId);
		when(request.getRequestURI()).thenReturn("cms/user_login_log");
		result = c.delete(request).toString();
		System.out.println(result);
	}
}
