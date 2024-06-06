package com.ajaxjs.iam.server.service;


import com.ajaxjs.framework.spring.response.Result;
import com.ajaxjs.iam.jwt.JWebTokenMgr;
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
        user.setLoginId("admin");

//        ModelAndView mv = OidcService.authorization("C2Oj5hKcwMmgxiKygwquLCSN", "http://qq.com", null, null);
//        System.out.println(mv);
    }

    public static String encodeClient(String clientId, String clientSecret) {
        String clientAndSecret = clientId + ":" + clientSecret;

        return "Basic " + JwtUtils.encodeBase64(clientAndSecret);
    }

    @Test
    public void testClient() {
        String s = encodeClient("G5IFeG7Eesbny3f", "J1Bb4zhchfziuDipKI7sgo6iyk");

        Result<JwtAccessToken> jwtAccessTokenResult = OidcService.clientCredentials(IamConstants.GrantType.CLIENT_CREDENTIALS, s);
        System.out.println(jwtAccessTokenResult.getResult().getAccess_token());
        System.out.println(jwtAccessTokenResult.getResult().getId_token());
    }

    @Test
    public void createToken() {
        User user = new User();
        user.setId(1L);
        user.setName("admin");
        user.setLoginId("admin");

        OidcService.getCache().put("1:user", user, 0);
        OidcService.getCache().put("1:scope", "user", 0);

        String s = encodeClient("G5IFeG7Eesbny3f", "J1Bb4zhchfziuDipKI7sgo6iyk");
        Result<JwtAccessToken> token = OidcService.token(s, "authorization_code", "1", "57458", "kjkkk");
        System.out.println(token.getResult().getId_token());


        JWebTokenMgr mgr = new JWebTokenMgr();
        mgr.setSecretKey("aEsD65643vb2");
        System.out.println(mgr.isValid(token.getResult().getId_token()));
    }
}
