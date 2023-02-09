package com.ajaxjs.user;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.SQLException;

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

import com.ajaxjs.auth.controller.DataServiceApiController;
import com.ajaxjs.sql.JdbcConnection;
import com.ajaxjs.user.controller.LoginController;
import com.ajaxjs.user.service.LoginService;

@ContextConfiguration(locations = { "classpath:applicationContext.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
public class TestLogin {
	HttpServletRequest request = mock(HttpServletRequest.class);

	@Autowired
	LoginController loginController;

	@Autowired
	DataServiceApiController apiController;

	@Autowired
	DataSource ds;

	@Before
	public void init() {
		apiController.initCache();
	}

	@Test
	public void testLogin() throws SQLException {
		assertNotNull(loginController);

		HttpSession s = mock(HttpSession.class);
		when(request.getSession()).thenReturn(s);
		when(request.getRemoteAddr()).thenReturn("35.220.250.107");

		try (Connection connection = ds.getConnection()) {
			JdbcConnection.setConnection(connection);

//			String result = loginController.login("sp42@qq.com", "a123123abc", 1, request);
//			System.out.println(result);
//			assertNotNull(result);
		}
	}

	@Autowired
	LoginService loginService;

//	@Test
	public void testPassword() {
		String encodePassword = loginService.encodePassword("abc123");
		assertEquals("5c5f72e86743a7e532ab783a3795c3a6e7853f31", encodePassword);

		assertTrue(loginService.isPswMatch("5c5f72e86743a7e532ab783a3795c3a6e7853f31", "abc123"));
	}
}
