package com.ajaxjs.sso.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.ModelAndView;

import com.ajaxjs.auth.service.UserUtils;
import com.ajaxjs.sso.common.SsoError;
import com.ajaxjs.sso.model.AccessToken;
import com.ajaxjs.sso.model.ClientDetails;
import com.ajaxjs.sso.model.ErrorCodeEnum;
import com.ajaxjs.sso.model.ExpireEnum;
import com.ajaxjs.sso.model.GrantTypeEnum;
import com.ajaxjs.sso.model.IssueToken;
import com.ajaxjs.sso.model.IssueTokenWithUser;
import com.ajaxjs.sso.model.RefreshToken;
import com.ajaxjs.user.User;
import com.ajaxjs.user.UserCommonDAO;
import com.ajaxjs.user.sso.common.SsoUtil;
import com.ajaxjs.util.cache.ExpireCache;
import com.ajaxjs.util.cryptography.Digest;
import com.ajaxjs.util.logger.LogHelper;

@Service
public class SsoService implements ISsoService {
    private static final LogHelper LOGGER = LogHelper.getLog(SsoService.class);

    @Autowired
    private AuthorizationService authService;

    @Override
    public ModelAndView getAuthorizeCode(String clientId, String redirectUri, String scope, String status, HttpServletRequest req) {
        LOGGER.info("获取 Authorization Code");

        User loginedUser = null;

        try {
            loginedUser = UserUtils.getLoginedUser(req);
        } catch (Throwable e) {
            LOGGER.warning(e);
            throw SsoError.oauthError("invalid_request", e);
        }

        // 生成 Authorization Code
        String authorizationCode = createAuthorizationCode(clientId, scope, loginedUser);
        String params = "?code=" + authorizationCode;

        if (StringUtils.hasText(status))
            params += "&status=" + status;

        return new ModelAndView("redirect:" + redirectUri + params);
    }

    /**
     * 根据 clientId、scope 以及当前时间戳生成 AuthorizationCode（有效期为10分钟）
     *
     * @param clientId 客户端ID
     * @param scope    权限范围（非必填）
     * @param user     用户信息
     * @return AuthorizationCode
     */
    public String createAuthorizationCode(String clientId, String scope, User user) {
        if (!StringUtils.hasText(scope))
            scope = "DEFAULT_SCOPE";

        // 1. 拼装待加密字符串（clientId + scope + 当前精确到毫秒的时间戳）
        String str = clientId + scope + System.currentTimeMillis();
        // 2. SHA1 加密
        String encryptedStr = Digest.getSHA1(str);
        int timeout = ExpireEnum.AUTHORIZATION_CODE.getTime() * 60;
        // 3.1 保存本次请求的授权范围
        ExpireCache.CACHE.put(encryptedStr + ":scope", scope, timeout);
        // 3.2 保存本次请求所属的用户信息
        ExpireCache.CACHE.put(encryptedStr + ":user", user, timeout);

        // 4. 返回Authorization Code
        return encryptedStr;
    }

    @Override
    public IssueTokenWithUser issue(String client_id, String client_secret, String code, String grant_type) {
        LOGGER.info("通过 Authorization Code 获取 Access Token");

        // 校验授权方式
        if (!GrantTypeEnum.AUTHORIZATION_CODE.getType().equals(grant_type))
            throw SsoError.oauthError(ErrorCodeEnum.UNSUPPORTED_GRANT_TYPE);

        ClientDetails savedClientDetails = findClientDetailsByClientId(client_id);

        // 校验请求的客户端秘钥和已保存的秘钥是否匹配
        if (!(savedClientDetails != null && savedClientDetails.getClientSecret().equals(client_secret))) {
            if (savedClientDetails == null)
                LOGGER.info("找不到客户端");

            if (!savedClientDetails.getClientSecret().equals(client_secret))
                LOGGER.info("密钥不匹配");

            throw SsoError.oauthError(ErrorCodeEnum.INVALID_CLIENT);
        }

        String scope = ExpireCache.CACHE.get(code + ":scope", String.class);
        User user = ExpireCache.CACHE.get(code + ":user", User.class);

        // 如果能够通过 Authorization Code 获取到对应的用户信息，则说明该 Authorization Code 有效
        if (StringUtils.hasText(scope) && user != null) {
            // 生成 Access Token
            AccessToken accessToken = authService.createAccessToken(user, savedClientDetails, grant_type, scope);
            // 查询已经插入到数据库的 Access Token
//			AccessToken accessToken = AcessTokenDAO.setWhereQuery("accessToken", accessTokenStr).findOne();
//			LOGGER.info(accessToken.getExpiresIn());
            // 生成 Refresh Token
            String refreshTokenStr = authService.createRefreshToken(user, accessToken);

            IssueTokenWithUser token = new IssueTokenWithUser(); // 返回数据
            token.setAccess_token(accessToken.getAccessToken());
            token.setRefresh_token(refreshTokenStr);
            token.setExpires_in(accessToken.getExpiresIn());
            token.setScope(accessToken.getScope());
            token.setUser(user);

            return token;
        } else
            throw SsoError.oauthError(ErrorCodeEnum.INVALID_GRANT);
    }

    @Override
    public IssueToken refreshToken(String refresh_token) {
        LOGGER.info("通过 Refresh Token 刷新 Access Token");
        RefreshToken authRefreshToken = RefreshTokenDAO.setWhereQuery("refreshToken", refresh_token).findOne();

        if (authRefreshToken == null)
            throw SsoError.oauthError(ErrorCodeEnum.INVALID_GRANT);

        // 如果 Refresh Token 已经失效，则需要重新生成
        if (SsoUtil.checkIfExpire(authRefreshToken))
            throw SsoError.oauthError(ErrorCodeEnum.EXPIRED_TOKEN);

        // 获取存储的 Access Token
        AccessToken authAccessToken = AcessTokenDAO.findById(authRefreshToken.getTokenId());
        // 获取对应的客户端信息
        ClientDetails savedClientDetails = ClientDetailDAO.findById(authAccessToken.getClientId());
        // 获取对应的用户信息
        User user = UserCommonDAO.UserDAO.findById(authAccessToken.getUserId());

        // 生成新的 Access Token
        AccessToken newToken = authService.createAccessToken(user, savedClientDetails, authAccessToken.getGrantType(), authAccessToken.getScope());

        IssueToken token = new IssueToken(); // 返回数据
        token.setAccess_token(newToken.getAccessToken());
        token.setRefresh_token(refresh_token);
        token.setExpires_in(newToken.getExpiresIn());
        token.setScope(authAccessToken.getScope());

        return token;
    }
}
