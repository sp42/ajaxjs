package com.ajaxjs.iam.server.service;

import com.ajaxjs.iam.server.BaseTest;
import org.junit.Test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class TestLoginRegister extends BaseTest {
    HttpServletRequest request = mock(HttpServletRequest.class);

//    @Autowired
//    LoginController loginController;

    @Test
    public void testLogin() throws SQLException {
//        assertNotNull(loginController);

        HttpSession s = mock(HttpSession.class);
        when(request.getSession()).thenReturn(s);
        when(request.getRemoteAddr()).thenReturn("35.220.250.107");
////        TestHelper.request = request;
//
//            boolean result = loginController.login("sp42@qq.com", "a123123abc");
////			System.out.println(result);
////			assertNotNull(result);
    }
//
//    @Autowired
//    LoginServiceImpl loginService;

    @Test
    public void testPassword() {
//        String encodePassword = loginService.encodePassword("abc123");
//        assertEquals("5c5f72e86743a7e532ab783a3795c3a6e7853f31", encodePassword);
//
//        assertTrue(loginService.isPswMatch("5c5f72e86743a7e532ab783a3795c3a6e7853f31", "abc123"));
    }

    @Test
    public void testRegister() {
        when(request.getRemoteAddr()).thenReturn("35.220.250.107");
//        TestHelper.request = req;

        Map<String, Object> params = new HashMap<>();
        params.put("tenantId", 1);
        params.put("username", "Mike7457");
        params.put("password", "asdsads");

//        registerController.register(params);
    }

    @Test
    public void testRepeat() {
        HttpSession s = mock(HttpSession.class);
        when(request.getSession()).thenReturn(s);
        when(request.getRemoteAddr()).thenReturn("35.220.250.107");
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

//        Boolean repeat = registerService.checkRepeat("username", "Mike747");
//        assertTrue(repeat);
    }
}
