package com.ajaxjs.user;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import com.ajaxjs.TestConfig;
import com.ajaxjs.auth.controller.DataServiceApiController;
import com.ajaxjs.user.service.RegisterService;

@ContextConfiguration(classes = TestConfig.class)
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
public class TestUser {
    MockMvc mockMvc;

    @Autowired
    WebApplicationContext wac;

    @Autowired
    private RegisterService registerService;

    @Autowired
    DataServiceApiController apiController;

    HttpServletRequest req = mock(HttpServletRequest.class);

    @Before
    public void init() {
        apiController.initCache();
    }

    //    @Test
    public void testGetAuthCode() {
        when(req.getRemoteAddr()).thenReturn("35.220.250.107");

        Map<String, Object> params = new HashMap<>();
        params.put("tenantId", 1);
        params.put("username", "Mike747");
        params.put("password", "asds555ads");

        Boolean register = registerService.register(params);
        assertTrue(register);
//        TestHelper.printJson(m);
    }

    @Test
    public void testRepeat() {
        Boolean repeat = registerService.checkRepeat("username", "Mike747");
        assertTrue(repeat);

        assertTrue(RegisterService.isRepeat("email", "sp42@qq.com", 1));
    }
}
