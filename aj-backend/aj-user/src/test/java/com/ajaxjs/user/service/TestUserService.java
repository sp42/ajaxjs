package com.ajaxjs.user.service;

import com.ajaxjs.util.TestHelper;
import com.ajaxjs.user.BaseTest;
import com.ajaxjs.user.common.UserConstants;
import com.ajaxjs.user.controller.UserController;
import com.ajaxjs.user.model.User;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class TestUserService extends BaseTest {
    @Autowired
    UserService userService;

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
        user.setUsername("TesdAdmin");
        user.setGender(UserConstants.Gender.MALE);
        user.setBirthday(new Date());
        user.setAvatar("https://example.com/avatar.jpg");
        user.setEmail("johndo@eexample.com");
        user.setPhone("1234567890");
        user.setIdCardNo("123456789012345678");
        assertNotNull(userService);

        Long userNewlyId = userController.create(user);
        assertNotNull(userNewlyId);
        System.out.println(userNewlyId);
    }

    @Test
    public void testUpdate() {
        User user = new User();
        user.setId(316L);
        user.setUsername("John Doe");
        user.setGender(UserConstants.Gender.MALE);
        user.setBirthday(new Date());
        user.setAvatar("https://example.com/avatar.jpg");
        user.setEmail("johndo@eexample.com");
        user.setPhone("1234567890");
        user.setIdCardNo("123456789012345678");
        assertNotNull(userService);

        assertTrue(userController.update(user));
    }
}
