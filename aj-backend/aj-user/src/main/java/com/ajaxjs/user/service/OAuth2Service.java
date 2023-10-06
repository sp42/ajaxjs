package com.ajaxjs.user.service;

import com.ajaxjs.user.common.oauth_error.ErrorCodeEnum;
import com.ajaxjs.user.common.oauth_error.SsoError;
import com.ajaxjs.user.common.UserConstants;
import com.ajaxjs.user.controller.OAuth2Controller;
import com.ajaxjs.user.model.AccessToken;
import com.ajaxjs.user.model.Client;
import com.ajaxjs.user.model.User;
import com.ajaxjs.util.Digest;
import com.ajaxjs.util.cache.Cache;
import com.ajaxjs.util.logger.LogHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.ModelAndView;

@Service
public class OAuth2Service implements OAuth2Controller, UserConstants {
    private static final LogHelper LOGGER = LogHelper.getLog(OAuth2Service.class);

    @Autowired
    Cache<String, Object> cache;

    @Override
    public ModelAndView authorize(String clientId, String redirectUri, String scope, String status) {
        User user = new User();
        user.setId(1L);
        user.setUsername("admin");
        User loginedUser = user;

        if (!StringUtils.hasText(scope))
            scope = "DEFAULT_SCOPE";

        // 生成授权码（Authorization Code）
        String code = Digest.getSHA1(clientId + scope + System.currentTimeMillis());
        String params = "?code=" + code;

        if (StringUtils.hasText(status))
            params += "&status=" + status;

        cache.put(code + ":user", loginedUser, AUTHORIZATION_CODE_TIMEOUT); // 保存本次请求所属的用户信息
        cache.put(code + ":scope", scope, AUTHORIZATION_CODE_TIMEOUT);// 保存本次请求的授权范围

        return new ModelAndView("redirect:" + redirectUri + params);
    }

    @Autowired
    ClientService clientService;

    @Override
    public AccessToken getToken(String clientId, String clientSecret, String code) {
        Client client = clientService.getClient(clientId, clientSecret);

        if (client == null) {
            LOGGER.info("找不到客户端或者密钥不匹配");
            throw SsoError.oauthError(ErrorCodeEnum.INVALID_CLIENT);
        }

        String scope = cache.get(code + ":scope", String.class);
        User user = cache.get(code + ":user", User.class);

        // 如果能够通过 Authorization Code 获取到对应的用户信息，则说明该 Authorization Code 有效
        if (StringUtils.hasText(scope) && user != null) {
            // 生成 Access Token
            AccessToken accessToken = createAccessToken(user, client, scope);
            // 查询已经插入到数据库的 Access Token
//			AccessToken accessToken = AcessTokenDAO.setWhereQuery("accessToken", accessTokenStr).findOne();
//			LOGGER.info(accessToken.getExpiresIn());
            // 生成 Refresh Token
            accessToken.setRefresh_token(createRefreshToken(user, accessToken));

            return accessToken;
        } else
            throw SsoError.oauthError(ErrorCodeEnum.INVALID_GRANT);


    }

    String createRefreshToken(User user, AccessToken accessToken) {
        return null;
    }

    AccessToken createAccessToken(User user, Client client, String scope) {
        return null;
    }

    @Override
    public AccessToken refreshToken(String refreshToken) {
        return null;
    }

    @Override
    public String checkToken(String token) {
        return null;
    }

    @Override
    public Boolean revokeToken(String token) {
        return false;
    }
}
