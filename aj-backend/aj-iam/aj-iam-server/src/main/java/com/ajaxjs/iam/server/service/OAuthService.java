package com.ajaxjs.iam.server.service;

import com.ajaxjs.data.CRUD;
import com.ajaxjs.framework.BusinessException;
import com.ajaxjs.framework.spring.filter.dbconnection.EnableTransaction;
import com.ajaxjs.iam.server.controller.OAuthController;
import com.ajaxjs.iam.server.model.AccessToken;
import com.ajaxjs.iam.server.model.po.AccessTokenPo;
import com.ajaxjs.iam.server.model.po.App;
import com.ajaxjs.iam.user.common.session.UserSession;
import com.ajaxjs.iam.user.model.User;
import com.ajaxjs.util.Digest;
import com.ajaxjs.util.StrUtil;
import com.ajaxjs.util.cache.Cache;
import com.ajaxjs.util.logger.LogHelper;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service
public class OAuthService extends OAuthCommon implements OAuthController {
    private static final LogHelper LOGGER = LogHelper.getLog(OAuthService.class);

    @Autowired(required = false)
    private Cache<String, Object> cache;

    @Autowired
    UserSession userSession;

    @Value("${oauth.token.user_expires: 120}")
    private Integer userExpires;

    @Override
    public ModelAndView agree(String clientId, String redirectUri, String scope, String status) {
        return null;
    }

    @Override
    public void authorization(String responseType, String clientId, String redirectUri, String scope, String state, HttpServletRequest req, HttpServletResponse resp) {
        if (!"code".equals(responseType))
            throw new IllegalArgumentException("参数 response_type 只能是 code");

        User user = userSession.getUserFromSession();


        if (!StringUtils.hasText(scope)) scope = "DEFAULT_SCOPE";

        // 生成授权码（Authorization Code）
        String code = Digest.getSHA1(clientId + scope + System.currentTimeMillis());
        String params = "?code=" + code;

        params += "&state=" + state;

        cache.put(code + ":user", loginedUser, AUTHORIZATION_CODE_TIMEOUT); // 保存本次请求所属的用户信息
        cache.put(code + ":scope", scope, AUTHORIZATION_CODE_TIMEOUT);// 保存本次请求的授权范围

        // 检测用户已经登录，如果没跳到登录页面让用户输入帐密
        // 如果已经登录，则提示转到一个页面，询问用户是否同意，授权可访问
        // 若是则生成 code，跳转到 redirectUri，那是一个回调

//        return new ModelAndView("redirect:" + redirectUri + params);
    }

    @Data
    static class TokenUser {
        Long userId;

        AccessToken accessToken;
    }

    @Override
    public AccessToken token(String authorization, String grantType, String code, String state) {
        if (!"authorization_code".equals(grantType))
            throw new IllegalArgumentException("参数 grant_type 只能是 authorization_code");

        String scope = cache.get(code + ":scope", String.class);
        User user = cache.get(code + ":user", User.class);

        // 如果能够通过 Authorization Code 获取到对应的用户信息，则说明该 Authorization Code 有效
        if (StringUtils.hasText(scope) && user != null) {
            App app = getAppByAuthHeader(authorization);

            // 生成 Access Token
            AccessToken accessToken = new AccessToken();
            createToken(accessToken, app, GrantType.OAUTH);

            // 保存 token 在缓存
            TokenUser tokenUser = new TokenUser();
            String key = TOKEN_USER_KEY + "-" + accessToken.getAccess_token();
            cache.put(key, tokenUser, getTokenExpires(app));

            // 删除缓存
            cache.remove(code + ":scope");
            cache.remove(code + ":user");

            return accessToken;
        } else
            throw new IllegalArgumentException("非法 code：" + code);
    }

    @Override
    @EnableTransaction
    public AccessToken refreshToken(String grantType, String clientId, String clientSecret, String refreshToken) {
        if (!GrantType.REFRESH_TOKEN.equals(grantType))
            throw new IllegalArgumentException("grantType must be 'refresh_token'");

        AccessTokenPo accessTokenPO = CRUD.info(AccessTokenPo.class, "SELECT * FROM access_token WHERE refresh_token = ?", refreshToken);

        if (accessTokenPO == null) throw new BusinessException("找不到 RefreshToken " + refreshToken);

        AccessToken accessToken = new AccessToken();
        accessToken.setAccess_token(StrUtil.uuid(false));
        accessToken.setRefresh_token(StrUtil.uuid(false));

        // 获取超时
        Integer minutes;

        if (GrantType.CLIENT_CREDENTIALS.equals(accessTokenPO.getGrantType())) {
            // 客户端的 token
            if (!clientId.equals(accessTokenPO.getClientId())) throw new BusinessException("ClientId 不一致");

            minutes = CRUD.queryOne(Integer.class, "SELECT expires FROM app WHERE client_id = ?", clientId);

            if (minutes == null) minutes = 120; // default value
        } else minutes = userExpires;// 用户

        accessToken.setExpires_in(minutes * 60);

        // 修改旧的
        AccessTokenPo updated = new AccessTokenPo();
        updated.setId(accessTokenPO.getId());
        updated.setAccessToken(accessToken.getAccess_token());
        updated.setRefreshToken(accessToken.getRefresh_token());
        updated.setExpiresDate(calculateExpirationDate(minutes));

        CRUD.updateWithIdField(updated);

        return accessToken;
    }

    @Override
    @EnableTransaction
    public AccessToken refreshTokenWithBasic(String grantType, String authorization, String refreshToken) {
        String[] arr = getClientInfo(authorization);

        return refreshToken(grantType, arr[0], arr[1], refreshToken);
    }

    @Override
    public AccessToken clientCredentials(String grantType, String clientId, String clientSecret) {
        return clientCredentials(getApp(clientId, clientSecret), grantType);
    }

    @Override
    public AccessToken clientCredentialsWithBasic(String grantType, String authorization) {
        return clientCredentials(getAppByAuthHeader(authorization), grantType);
    }

    @Override
    public void implicitAuthorization(String responseType, String clientId, String redirectUri, String scope, String state, HttpServletRequest req, HttpServletResponse resp) {
    }

    @Override
    public void passwordAuthorization(String grantType, String clientId, String clientSecret, String loginId, String password) {
    }

    private AccessToken clientCredentials(App app, String grantType) {
        if (!GrantType.CLIENT_CREDENTIALS.equals(grantType))
            throw new IllegalArgumentException("grantType must be 'clientCredentials'");

        AccessToken accessToken = new AccessToken();
        createToken(accessToken, app, GrantType.CLIENT_CREDENTIALS);

        return accessToken;
    }

    @Override
    public Boolean checkToken(String token) {
        return null;
    }

    @Override
    public Boolean revokeToken(String token) {
        return false;
    }
}
