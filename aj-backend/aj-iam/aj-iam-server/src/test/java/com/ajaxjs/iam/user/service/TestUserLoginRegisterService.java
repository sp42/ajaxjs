package com.ajaxjs.iam.user.service;

import com.ajaxjs.iam.server.BaseTest;
import com.ajaxjs.iam.user.model.User;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TestUserLoginRegisterService extends BaseTest {
    @Autowired
    UserLoginRegisterService userLoginRegisterService;

    HttpServletRequest request = mock(HttpServletRequest.class);

    @Autowired
    @Qualifier("passwordEncode")
    Function<String, String> passwordEncode;

    @Test
    public void testPassword() {
        String encodePassword = passwordEncode.apply("abc123");
        assertEquals("7eb0fa6e9ea61f42796feecdc6011000", encodePassword);
    }

    @Test
    public void testGetUserLoginByPassword() {
        User admin = userLoginRegisterService.getUserLoginByPassword("admin", "123123");
        assertNotNull(admin);

        HttpSession s = mock(HttpSession.class);
        when(request.getSession()).thenReturn(s);
        when(request.getRemoteAddr()).thenReturn("35.220.250.107");
//        TestHelper.request = request;

        userLoginRegisterService.login("admin", "123123", null, request, null);
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
