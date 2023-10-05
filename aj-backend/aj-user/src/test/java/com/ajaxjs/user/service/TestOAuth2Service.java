package com.ajaxjs.user.service;

import com.ajaxjs.user.model.User;
import org.junit.Test;

public class TestOAuth2Service {
    OAuth2Service oAuth2Service = new OAuth2Service();

    @Test
    public void testCreateAuthorizationCode() {
        User user = new User();
        user.setId(1L);
        user.setUsername("admin");

        String code = oAuth2Service.createAuthorizationCode("C2Oj5hKcwMmgxiKygwquLCSN", null, user);
        System.out.println(code);
    }
}
