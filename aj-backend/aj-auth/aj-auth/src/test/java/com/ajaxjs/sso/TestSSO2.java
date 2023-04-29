package com.ajaxjs.sso;

import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.servlet.http.HttpServletRequest;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.context.WebApplicationContext;

import com.ajaxjs.TestConfig;
import com.ajaxjs.sso.controller.SsoController;
import com.ajaxjs.sso.controller.StateController;
import com.ajaxjs.sso.model.IssueToken;
import com.ajaxjs.sso.model.IssueTokenWithUser;
import com.ajaxjs.user.User;
import com.ajaxjs.user.UserConstant;
import com.ajaxjs.util.regexp.RegExpUtils;

@ContextConfiguration(classes = TestConfig.class)
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
public class TestSSO2 {
    MockMvc mockMvc;

    @Autowired
    WebApplicationContext wac;

//	@Autowired
//	ApiController apiController;

    @Before
    public void init() {
//		apiController.initCache();
//		mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    private String getAuthCode() throws Exception {
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

        String redirectedUrl = mockMvc.perform(req).andExpect(status().is3xxRedirection()).andDo(print()).andReturn().getResponse()
                .getRedirectedUrl();
//		String redirectedUrl = mockMvc.perform(req).andDo(print()).andReturn().getResponse().getRedirectedUrl();

        return redirectedUrl;
    }

    //	@Test
    public void testAuthCode() throws Exception {
        String redirectedUrl = getAuthCode();
        assertNotNull(redirectedUrl);
    }

    @Autowired
    SsoController oauthController;

    //	@Test
    public void testIssueToken() throws Exception {
        String redirectedUrl = getAuthCode();
        String authCode = RegExpUtils.regMatch("(?:code=)(\\w+)", redirectedUrl, 1);
        System.out.println(authCode);
        HttpServletRequest req = Mockito.mock(HttpServletRequest.class);

        IssueTokenWithUser issue = oauthController.issue("C2Oj5hKcwMmgxiKygwquLCSN", "1PtTvjmAvy2zSUZISdjeKBFJTgZM43BZ", authCode,
                "authorization_code");
        System.out.println(issue);
    }

    //	@Test
    public void testRefreshToken() throws Exception {
        IssueToken newToken = oauthController.refreshToken("2.7518a00025a7db7a324689466dcb76f17f7aca7d.31536000.1682057306");
        System.out.println(newToken);
    }

    @Autowired
    StateController stateController;

    //	@Test
    public void verify() {
        String token = "1.9e8832e4a30574ccb4e17d35b82f228099dc4530.2592000.1653536869";
        Boolean verify = stateController.verify(token);
        System.out.println(verify);
    }
}
