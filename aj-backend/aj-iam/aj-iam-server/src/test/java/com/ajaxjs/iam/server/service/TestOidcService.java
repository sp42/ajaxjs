package com.ajaxjs.iam.server.service;


import com.ajaxjs.framework.spring.response.Result;
import com.ajaxjs.iam.jwt.JwtUtils;
import com.ajaxjs.iam.server.BaseTest;
import com.ajaxjs.iam.server.common.IamConstants;
import com.ajaxjs.iam.server.model.JwtAccessToken;
import com.ajaxjs.iam.user.model.User;
import com.ajaxjs.util.StrUtil;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TestOidcService extends BaseTest {
    @Autowired
    OidcService OidcService;

    @Test
    public void testCreateAuthorizationCode() {
        User user = new User();
        user.setId(1L);
        user.setUsername("admin");

//        ModelAndView mv = OidcService.authorization("C2Oj5hKcwMmgxiKygwquLCSN", "http://qq.com", null, null);
//        System.out.println(mv);
    }

    public static String encodeClient(String clientId, String clientSecret) {
        String clientAndSecret = clientId + ":" + clientSecret;

        return "Basic " + JwtUtils.encodeBase64(clientAndSecret);
    }

    @Test
    public void testClient() {
        String s = encodeClient("y5agHzACGrIpg46", "zKvmMA4KmJ2CIijlBubqbBpHm1");

        Result<JwtAccessToken> jwtAccessTokenResult = OidcService.clientCredentials(IamConstants.GrantType.CLIENT_CREDENTIALS, s);
        System.out.println(jwtAccessTokenResult.getResult().getAccess_token());
        System.out.println(jwtAccessTokenResult.getResult().getId_token());
    }
}
