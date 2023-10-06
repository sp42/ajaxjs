package com.ajaxjs.user.service;

import com.ajaxjs.user.BaseTest;
import com.ajaxjs.user.model.User;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;

public class TestOAuth2Service extends BaseTest {
    @Autowired
    OAuth2Service oAuth2Service;

    @Test
    public void testCreateAuthorizationCode() {
        User user = new User();
        user.setId(1L);
        user.setUsername("admin");

        ModelAndView mv = oAuth2Service.authorize("C2Oj5hKcwMmgxiKygwquLCSN", "http://qq.com", null, null);
        System.out.println(mv);
    }
}
