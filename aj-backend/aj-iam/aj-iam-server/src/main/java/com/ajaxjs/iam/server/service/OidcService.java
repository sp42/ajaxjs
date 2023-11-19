package com.ajaxjs.iam.server.service;

import com.ajaxjs.framework.spring.DiContextUtil;
import com.ajaxjs.framework.spring.response.Result;
import com.ajaxjs.iam.jwt.JWebTokenMgr;
import com.ajaxjs.iam.jwt.Utils;
import com.ajaxjs.iam.server.controller.OidcController;
import com.ajaxjs.iam.server.model.JwtAccessToken;
import com.ajaxjs.iam.server.model.po.App;
import com.ajaxjs.iam.user.common.session.UserSession;
import com.ajaxjs.iam.user.model.User;
import com.ajaxjs.util.cache.Cache;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;

@Service
public class OidcService extends OAuthCommon implements OidcController {
    @Autowired
    UserSession userSession;

    @Autowired(required = false)
    Cache<String, Object> cache;

    @Autowired
    JWebTokenMgr jWebTokenMgr;

    @Value("${User.oidc.jwtExpireHours}")
    int jwtExpireHours;

    @Override
    public void authorization(String responseType, String clientId, String redirectUri, String scope, String state,
                              String webUrl, HttpServletRequest req, HttpServletResponse resp) {
        sendAuthCode(responseType, clientId, redirectUri, scope, state, webUrl, req, resp, cache);
    }

    @Data
    static class TokenUser {
        Long userId;

        JwtAccessToken accessToken;
    }

    @Override
    public Result<JwtAccessToken> token(String authorization, String grantType, String code, String state, String webUrl) {
        if (!"authorization_code".equals(grantType))
            throw new IllegalArgumentException("参数 grant_type 只能是 authorization_code");

        User user = cache.get(code + ":user", User.class);
        String scope = cache.get(code + ":scope", String.class);

        // 如果能够通过 Authorization Code 获取到对应的用户信息，则说明该 Authorization Code 有效
        if (StringUtils.hasText(scope) && user != null) {
            App app = getAppByAuthHeader(authorization);

            // 生成 Access Token
            JwtAccessToken accessToken = new JwtAccessToken();
            createToken(accessToken, app, GrantType.OIDC);

            // 保存 token 在缓存
            TokenUser tokenUser = new TokenUser();
            String key = JWT_TOKEN_USER_KEY + "-" + accessToken.getAccess_token();
            cache.put(key, tokenUser, getTokenExpires(app));

            // 生成 JWT Token
            String jWebToken = jWebTokenMgr.tokenFactory(String.valueOf(user.getId()), user.getName(), scope, Utils.setExpire(jwtExpireHours)).toString();
            accessToken.setId_token(jWebToken);

            // 删除缓存
            cache.remove(code + ":scope");
            cache.remove(code + ":user");

            return new Result<>(accessToken, true);
        } else
            throw new IllegalArgumentException("非法 code：" + code);
    }
}
