package com.ajaxjs.sso;

import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import com.ajaxjs.user.SsoServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.ModelAndView;

import com.ajaxjs.TestConfig;
import com.ajaxjs.data_service.service.DataService;
import com.ajaxjs.user.model.IssueToken;
import com.ajaxjs.user.model.IssueTokenWithUser;
import com.ajaxjs.user.User;
import com.ajaxjs.user.UserConstant;
import com.ajaxjs.util.TestHelper;

@ContextConfiguration(classes = TestConfig.class)
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
public class TestSSO {
	MockMvc mockMvc;

	@Autowired
	WebApplicationContext wac;

	@Autowired
	private SsoServiceImpl ssoService;

	@Autowired
	DataServiceApiController apiController;

	@Autowired
	DataService ds;

	@Before
	public void init() {
		ds.init();
	}

	@Test
	public void testGetAuthCode() {
		assertNotNull(ssoService);

		User user = new User();
		user.setId(1L);
		user.setUsername("admin");
		MockHttpSession sessionPub = new MockHttpSession();
		sessionPub.setAttribute(UserConstant.USER_SESSION_KEY, user);

		// @formatter:off
        MockHttpServletRequestBuilder req = get("/sso/authorize_code").
                param("redirect_uri", "https://www.qq.com").param("client_id", "dss23s").
                session(sessionPub);
        // @formatter:on

		ModelAndView m = ssoService.getAuthorizeCode("dss23s", "https://www.qq.com", "", "", req.buildRequest(wac.getServletContext()));
		TestHelper.printJson(m);
	}


}
