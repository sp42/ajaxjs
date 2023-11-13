package com.ajaxjs.iam.server.service;

import com.ajaxjs.framework.spring.response.Result;
import com.ajaxjs.iam.jwt.JWebTokenMgr;
import com.ajaxjs.iam.jwt.Utils;
import com.ajaxjs.iam.server.common.IamConstants;
import com.ajaxjs.iam.server.common.IamUtils;
import com.ajaxjs.iam.server.controller.OidcController;
import com.ajaxjs.iam.server.model.JwtAccessToken;
import com.ajaxjs.iam.server.model.po.App;
import com.ajaxjs.iam.user.common.session.UserSession;
import com.ajaxjs.iam.user.model.User;
import com.ajaxjs.util.Digest;
import com.ajaxjs.util.StrUtil;
import com.ajaxjs.util.cache.Cache;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service
public class OidcService implements OidcController, IamConstants {
    private static final String NOT_LOGIN_TEXT = "<meta http-equiv=\"refresh\" content=\"2;url=%s\" /> 用户尚未登录，两秒后跳转到登录页面……";

    @Autowired
    UserSession userSession;

    @Autowired(required = false)
    Cache<String, Object> cache;

    @Autowired
    ClientService clientService;

    @Autowired
    OAuthService oAuthService;

    @Autowired
    JWebTokenMgr jWebTokenMgr;

    @Value("${User.oidc.jwtExpireHours}")
    int jwtExpireHours;

    @Override
    public void authorization(String responseType, String clientId, String redirectUri, String scope, String state, HttpServletRequest req, HttpServletResponse resp) {
        if (!"code".equals(responseType))
            throw new IllegalArgumentException("参数 response_type 只能是 code");

        User user = userSession.getUserFromSession();

        if (user == null) { // 未登录
            // 返回一段 HTML
            String html = String.format(NOT_LOGIN_TEXT, "../login?" + req.getQueryString());
            IamUtils.responseHTML(resp, html);
        } else {// 已登录，发送授权码
            StringBuilder sb = new StringBuilder();
            sb.append("?status=").append(state);
            // 生成授权码（Authorization Code）
            String code = Digest.getSHA1(clientId + System.currentTimeMillis());
            sb.append("&code=").append(code);

            if (!StringUtils.hasText(scope))
                scope = "DEFAULT_SCOPE";

            cache.put(code + ":user", user, AUTHORIZATION_CODE_TIMEOUT); // 保存本次请求所属的用户信息
            cache.put(code + ":scope", scope, AUTHORIZATION_CODE_TIMEOUT);// 保存本次请求的授权范围

            IamUtils.send303Redirect(resp, redirectUri + sb); // 跳转到 RP
        }
    }

    @Data
    static class TokenUser {
        Long userId;

        JwtAccessToken accessToken;
    }

    @Override
    public Result<JwtAccessToken> token(String authorization, String code, String state, String grantType) {
        if (!"authorization_code".equals(grantType))
            throw new IllegalArgumentException("参数 grant_type 只能是 authorization_code");

        authorization = authorization.replaceAll("Basic ", "");
        authorization = StrUtil.base64Decode(authorization);
        String[] arr = authorization.split(":");
        String clientId = arr[0], clientSecret = arr[1];
        App app = clientService.getApp(clientId, clientSecret);

        User user = cache.get(code + ":user", User.class);
        String scope = cache.get(code + ":scope", String.class);

        // 如果能够通过 Authorization Code 获取到对应的用户信息，则说明该 Authorization Code 有效
        if (StringUtils.hasText(scope) && user != null) {
            // 删除缓存
//            cache.remove(code + ":scope");
//            cache.remove(code + ":user");

            // 生成 Access Token
            JwtAccessToken accessToken = new JwtAccessToken();
            oAuthService.createToken(accessToken, app);

            // 保存 token 在缓存
            TokenUser tokenUser = new TokenUser();
            String key = TOKEN_USER_KEY + "-" + accessToken.getAccess_token();
            cache.put(key, tokenUser, oAuthService.getTokenExpires(app));

            // 生成 JWT Token
            String jWebToken = jWebTokenMgr.tokenFactory(String.valueOf(user.getId()), user.getName(), "admin, guest", Utils.setExpire(jwtExpireHours)).toString();
            accessToken.setId_token(jWebToken);

            return new Result<>(accessToken, true);
        } else
            throw new IllegalArgumentException("非法 code：" + code);
    }
}
