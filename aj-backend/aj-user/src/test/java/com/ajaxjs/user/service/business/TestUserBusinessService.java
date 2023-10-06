package com.ajaxjs.user.service.business;

import com.ajaxjs.user.BaseTest;
import com.ajaxjs.user.model.User;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertNotNull;

public class TestUserBusinessService extends BaseTest {
    @Autowired
    UserBusinessService userBusinessService;

    @Test
    public void testGetUserLoginByPassword() {
        User admin = userBusinessService.getUserLoginByPassword("admin", "123123");
        assertNotNull(admin);
    }
}
