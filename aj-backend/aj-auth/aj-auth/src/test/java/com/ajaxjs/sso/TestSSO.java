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

	@Test
	public void testIssueToken() throws Exception {
		User user = new User();
		user.setId(1L);
		user.setUsername("admin");

		String authCode = ssoService.createAuthorizationCode("dss23s", "https://www.qq.com", user);
		System.out.println(authCode);

		IssueTokenWithUser issue = ssoService.issue("C2Oj5hKcwMmgxiKygwquLCSN", "1PtTvjmAvy2zSUZISdjeKBFJTgZM43BZ", authCode, "authorization_code");
		TestHelper.printJson(issue);
	}

	@Test
	public void testRefreshToken() throws Exception {
		IssueToken newToken = ssoService.refreshToken("2.7518a00025a7db7a324689466dcb76f17f7aca7d.31536000.1682057306");
		TestHelper.printJson(newToken);
	}

//    @Autowired
//    StateController stateController;
//
//    @Test
//    public void verify() {
//        String token = "1.9e8832e4a30574ccb4e17d35b82f228099dc4530.2592000.1653536869";
//        Boolean verify = stateController.verify(token);
//        System.out.println(verify);
//    }
}
