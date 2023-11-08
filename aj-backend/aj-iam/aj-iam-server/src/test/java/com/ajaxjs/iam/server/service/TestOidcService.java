package com.ajaxjs.iam.server.service;


import com.ajaxjs.iam.server.BaseTest;
import com.ajaxjs.iam.user.model.User;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TestOidcService extends BaseTest {
    @Autowired
    OidcService OidcService;

    @Test
    public void testCreateAuthorizationCode() {
        User user = new User();
        user.setId(1L);
        user.setUsername("admin");

//        ModelAndView mv = OidcService.authorization("C2Oj5hKcwMmgxiKygwquLCSN", "http://qq.com", null, null);
//        System.out.println(mv);
    }
}
