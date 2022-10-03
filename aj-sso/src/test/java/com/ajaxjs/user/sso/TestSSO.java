package com.ajaxjs.user.sso;

import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

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

import com.ajaxjs.data_service.api.ApiController;
import com.ajaxjs.user.TestConfig;
import com.ajaxjs.user.User;
import com.ajaxjs.user.UserConstant;
import com.ajaxjs.user.sso.model.IssueTokenWithUser;
import com.ajaxjs.user.sso.service.SsoService;
import com.ajaxjs.util.TestHelper;

@ContextConfiguration(classes = TestConfig.class)
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
public class TestSSO {
	MockMvc mockMvc;

	@Autowired
	WebApplicationContext wac;

	@Autowired
	private SsoService ssoService;
	
	@Autowired
	ApiController apiController;

	@Before
	public void init() {
		apiController.initCache();
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

		Object authorize = ssoService.getAuthorizeCode("dss23s", "https://www.qq.com", "", "", req.buildRequest(wac.getServletContext()));
		TestHelper.printJson(authorize);
	}

	@Test
	public void testIssueToken() throws Exception {
		User user = new User();
		user.setId(1L);
		user.setUsername("admin");

		String authCode = ssoService.createAuthorizationCode("dss23s", "https://www.qq.com", user);
		System.out.println(authCode);

		IssueTokenWithUser issue = ssoService.issue("C2Oj5hKcwMmgxiKygwquLCSN", "1PtTvjmAvy2zSUZISdjeKBFJTgZM43BZ", authCode, "authorization_code");
		System.out.println(issue);
	}

}
