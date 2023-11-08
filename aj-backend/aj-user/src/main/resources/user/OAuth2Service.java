package com.ajaxjs.user.controller;

import com.ajaxjs.data.CRUD;
import com.ajaxjs.user.common.oauth_error.ErrorCodeEnum;
import com.ajaxjs.user.common.oauth_error.SsoError;
import com.ajaxjs.user.model.AccessToken;
import com.ajaxjs.user.model.po.Client;
import com.ajaxjs.user.model.User;
import com.ajaxjs.util.Digest;
import com.ajaxjs.util.cache.Cache;
import com.ajaxjs.util.logger.LogHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.ModelAndView;

@Service
public class  OAuth2Service implements OAuth2Controller, UserConstants {
    private static final LogHelper LOGGER = LogHelper.getLog(OAuth2Service.class);

    public Client getClient(String clientId, String clientSecret) {
        return CRUD.info(Client.class, "SELECT * FROM auth_client_details WHERE client_id = ? AND client_secret = ?", clientId, clientSecret);
    }

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

    //    public Boolean clientRegister(ClientDetails client) {
//        if (!StringUtils.hasText(client.getName()))
//            throw new IllegalArgumentException("客户端的名称和回调地址不能为空");
//
//        String clientId = StrUtil.getRandomString(24);// 生成24位随机的 clientId
//        ClientDetails savedClientDetails = findClientDetailsByClientId(clientId);
//
//        // 生成的 clientId 必须是唯一的，尝试十次避免有重复的 clientId
//        for (int i = 0; i < 10; i++) {
//            if (savedClientDetails == null)
//                break;
//            else {
//                clientId = StrUtil.getRandomString(24);
//                savedClientDetails = findClientDetailsByClientId(clientId);
//            }
//        }
//
//        client.setClientId(clientId);
//        client.setClientSecret(StrUtil.getRandomString(32));
//
//        // 保存到数据库
//        return ClientDetailDAO.create(client) == null;
//    }
//
//    public IssueTokenWithUser issue(String client_id, String client_secret, String code, String grant_type) {
//        LOGGER.info("通过 Authorization Code 获取 Access Token");
//
//        // 校验授权方式
//        if (!GrantTypeEnum.AUTHORIZATION_CODE.getType().equals(grant_type))
//            throw SsoError.oauthError(ErrorCodeEnum.UNSUPPORTED_GRANT_TYPE);
//
//        ClientDetails savedClientDetails = findClientDetailsByClientId(client_id);
//
//        // 校验请求的客户端秘钥和已保存的秘钥是否匹配
//        if (!(savedClientDetails != null && savedClientDetails.getClientSecret().equals(client_secret))) {
//            if (savedClientDetails == null)
//                LOGGER.info("找不到客户端");
//
//            assert savedClientDetails != null;
//            if (!savedClientDetails.getClientSecret().equals(client_secret))
//                LOGGER.info("密钥不匹配");
//
//            throw SsoError.oauthError(ErrorCodeEnum.INVALID_CLIENT);
//        }
//
//        String scope = ExpireCache.CACHE.get(code + ":scope", String.class);
//        User user = ExpireCache.CACHE.get(code + ":user", User.class);
//
//        // 如果能够通过 Authorization Code 获取到对应的用户信息，则说明该 Authorization Code 有效
//        if (StringUtils.hasText(scope) && user != null) {
//            // 生成 Access Token
//            AccessToken accessToken = createAccessToken(user, savedClientDetails, grant_type, scope);
//            // 查询已经插入到数据库的 Access Token
////			AccessToken accessToken = AcessTokenDAO.setWhereQuery("accessToken", accessTokenStr).findOne();
////			LOGGER.info(accessToken.getExpiresIn());
//            // 生成 Refresh Token
//            String refreshTokenStr = createRefreshToken(user, accessToken);
//
//            IssueTokenWithUser token = new IssueTokenWithUser(); // 返回数据
//            token.setAccess_token(accessToken.getAccessToken());
//            token.setRefresh_token(refreshTokenStr);
//            token.setExpires_in(accessToken.getExpiresIn());
//            token.setScope(accessToken.getScope());
//            token.setUser(user);
//
//            return token;
//        } else
//            throw SsoError.oauthError(ErrorCodeEnum.INVALID_GRANT);
//    }
}
