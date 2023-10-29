package com.ajaxjs.user;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.ajaxjs.data_service.service.DataService;
import com.ajaxjs.user.controller.RegisterController;
import com.ajaxjs.util.TestHelper;

@ContextConfiguration(locations = { "classpath:applicationContext.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
public class TestRegister {
	HttpServletRequest req = mock(HttpServletRequest.class);

	@Autowired
	RegisterController registerController;

	@Autowired
	DataService ds2;

	@Autowired
	DataSource ds;

	String result;

	@Before
	public void init() {
		ds2.init();
	}

	@Test
	public void testRegister() {
		when(req.getRemoteAddr()).thenReturn("35.220.250.107");
		TestHelper.request = req;

		Map<String, Object> params = new HashMap<>();
		params.put("tenantId", 1);
		params.put("username", "Mike7457");
		params.put("password", "asdsads");
		registerController.register(params);
	}

//	@Test
	public void testRepeat() {
		assertNotNull(registerController);

		HttpSession s = mock(HttpSession.class);
		when(req.getSession()).thenReturn(s);
		when(req.getRemoteAddr()).thenReturn("35.220.250.107");
//		assertTrue(RegisterController.isRepeat("email", "sp42@qq.com", 1));

//		result = registerController.checkRepeat("email", "sp42@qq.com", 1);
//		System.out.println(result);

//		try (Connection connection = ds.getConnection()) {
//			JdbcConnection.setConnection(connection);
//
//			String result = loginController.login("sp42@qq.com", "abc123", request);
//			System.out.println(result);
//			assertNotNull(result);
//		}
	}

}
