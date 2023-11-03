//package com.ajaxjs.user.service;
//
//import com.ajaxjs.model.*;
//import com.ajaxjs.sso.model.*;
//import com.ajaxjs.user.model.*;
//import com.ajaxjs.util.StrUtil;
//import com.ajaxjs.util.cache.ExpireCache;
//import com.ajaxjs.util.cryptography.Digest;
//import com.ajaxjs.util.date.LocalDateUtils;
//import com.ajaxjs.util.logger.LogHelper;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.util.StringUtils;
//
//import java.io.Serializable;
//import java.time.LocalDateTime;
//
//@Service
//public class SsoServiceImpl {
//    private static final LogHelper LOGGER = LogHelper.getLog(SsoServiceImpl.class);
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
//
//
//
//}
