package com.ajaxjs.user.service;

import com.ajaxjs.framework.TestHelper;
import com.ajaxjs.user.BaseTest;
import com.ajaxjs.user.TestConfig;
import com.ajaxjs.user.common.UserConstants;
import com.ajaxjs.user.controller.UserController;
import com.ajaxjs.user.model.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.Date;

import static org.junit.Assert.assertNotNull;

public class TestUserService extends BaseTest {
    @Autowired
    UserServiceImpl userService;

    @Autowired
    UserController userController;

    @Test
    public void testInfo() {
        assertNotNull(userService);
        User user = userService.info(1L);
        TestHelper.print(user);

        assertNotNull(userController);
        user = userController.info(1L);
        TestHelper.print(user);
    }

    @Test
    public void testCreate() {
        User user = new User();
        user.setUsername("John Doe");
        user.setGender(UserConstants.Gender.MALE);
        user.setBirthday(new Date());
        user.setAvatar("https://example.com/avatar.jpg");
        user.setEmail("johndo@eexample.com");
        user.setPhone("1234567890");
        user.setIdCardNo("123456789012345678");
        assertNotNull(userService);

        userController.create(user);
    }
}
