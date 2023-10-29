package com.ajaxjs.iam.server.service;

import com.ajaxjs.iam.server.BaseTest;
import com.ajaxjs.iam.server.model.AccessToken;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TestOAuthService extends BaseTest {
    @Autowired
    OAuthService oAuthService;

    @Test
    public void testClientCredentials() {
        AccessToken accessToken = oAuthService.clientCredentials("clientCredentials", "G5IFeG7Eesbny3f", "J1Bb4zhchfziuDipKI7sgo6iyk");
        System.out.println(accessToken);
    }
}
