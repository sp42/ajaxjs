package com.ajaxjs.iam.user.service;

import com.ajaxjs.iam.server.BaseTest;
import com.ajaxjs.iam.user.model.User;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertNotNull;

public class TestUserLoginRegisterService extends BaseTest {
    @Autowired
    UserLoginRegisterService userLoginRegisterService;

    @Test
    public void testGetUserLoginByPassword() {
        User admin = userLoginRegisterService.getUserLoginByPassword("admin", "123123");
        assertNotNull(admin);
    }
}
