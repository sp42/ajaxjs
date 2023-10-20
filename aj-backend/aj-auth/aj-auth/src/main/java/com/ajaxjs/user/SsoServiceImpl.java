package com.ajaxjs.user;

import com.ajaxjs.model.*;
import com.ajaxjs.sso.model.*;
import com.ajaxjs.user.model.*;
import com.ajaxjs.util.cache.ExpireCache;
import com.ajaxjs.util.cryptography.Digest;
import com.ajaxjs.util.date.LocalDateUtils;
import com.ajaxjs.util.logger.LogHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.time.LocalDateTime;

@Service
public class SsoServiceImpl {
    private static final LogHelper LOGGER = LogHelper.getLog(SsoServiceImpl.class);

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

            assert savedClientDetails != null;
            if (!savedClientDetails.getClientSecret().equals(client_secret))
                LOGGER.info("密钥不匹配");

            throw SsoError.oauthError(ErrorCodeEnum.INVALID_CLIENT);
        }

        String scope = ExpireCache.CACHE.get(code + ":scope", String.class);
        User user = ExpireCache.CACHE.get(code + ":user", User.class);

        // 如果能够通过 Authorization Code 获取到对应的用户信息，则说明该 Authorization Code 有效
        if (StringUtils.hasText(scope) && user != null) {
            // 生成 Access Token
            AccessToken accessToken = createAccessToken(user, savedClientDetails, grant_type, scope);
            // 查询已经插入到数据库的 Access Token
//			AccessToken accessToken = AcessTokenDAO.setWhereQuery("accessToken", accessTokenStr).findOne();
//			LOGGER.info(accessToken.getExpiresIn());
            // 生成 Refresh Token
            String refreshTokenStr = createRefreshToken(user, accessToken);

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
        AccessToken newToken = createAccessToken(user, savedClientDetails, authAccessToken.getGrantType(), authAccessToken.getScope());

        IssueToken token = new IssueToken(); // 返回数据
        token.setAccess_token(newToken.getAccessToken());
        token.setRefresh_token(refresh_token);
        token.setExpires_in(newToken.getExpiresIn());
        token.setScope(authAccessToken.getScope());

        return token;
    }

    public Boolean verify(String access_token) {
        AccessToken token = findByAccessToken(access_token);// 查询数据库中的 Access Token

        if (token == null)
            throw SsoError.oauthError(ErrorCodeEnum.INVALID_GRANT);

        long savedExpiresAt = token.getExpiresIn();// 过期日期
        LocalDateTime expiresDateTime = LocalDateUtils.ofEpochSecond(savedExpiresAt);
        System.out.println(expiresDateTime);

        // 如果 Access Token 已经失效，则返回错误提示
        if (LocalDateUtils.isTimeout(expiresDateTime)) {
            // TODO 是否要删除过期 token？
            throw SsoError.oauthError(ErrorCodeEnum.EXPIRED_TOKEN);
        }

        return true;
    }

    /**
     * 生成 Access Token
     *
     * @param user      用户信息
     * @param client    接入的客户端信息
     * @param grantType 授权方式
     * @param scope     允许访问的用户权限范围
     * @return
     */
    public AccessToken createAccessToken(UserModel.User user, ClientDetails client, String grantType, String scope) {
        // 过期时间
        Long expiresIn = LocalDateUtils.dayToSecond(ExpireEnum.ACCESS_TOKEN.getTime());
        // 过期的时间戳
        Long expiresAt = LocalDateUtils.nextDaysSecond(ExpireEnum.ACCESS_TOKEN.getTime(), null);
        // 1. 拼装待加密字符串（username + clientId + 当前精确到毫秒的时间戳）
        String str = user.getUsername() + client.getClientId() + String.valueOf(System.currentTimeMillis());
        // 2. SHA1加密
        String accessTokenStr = "1." + Digest.getSHA1(str) + "." + expiresIn + "." + expiresAt;

        // 3. 保存Access Token
        long userId = user.getId();
        long clientId = client.getId();
        AccessToken at = findTokenByUserIdClientIdScope(userId, clientId, scope);

        // 如果存在userId + clientId + scope 匹配的记录，则更新原记录，否则向数据库中插入新记录
        if (at != null) {
            at.setAccessToken(accessTokenStr);
            at.setExpiresIn(expiresAt);
//			at.setUpdateUser(user.getId());
            AcessTokenDAO.update(at);
        } else {
            at = new AccessToken();
            at.setAccessToken(accessTokenStr);
            at.setUserId(user.getId());
            at.setUserName(user.getUsername());
            at.setClientId(client.getId());
            at.setExpiresIn(expiresAt);
            at.setScope(scope);
            at.setGrantType(grantType);
//			at.setCreateUser(user.getId());

            Serializable id = AcessTokenDAO.create(at);

            if (id == null)
                System.err.println("创建 AccessToken 失败");
            else
                at.setId((Long) id);
        }

        // 4. 返回Access Token
        return at;
    }

    public static void main(String[] args) {
        System.out.println(LocalDateUtils.nextDaysSecond(ExpireEnum.ACCESS_TOKEN.getTime(), null));
    }

    public String createRefreshToken(User user, AccessToken at) {
        // 过期时间
        Long expiresIn = LocalDateUtils.dayToSecond(ExpireEnum.REFRESH_TOKEN.getTime());
        // 过期的时间戳
        Long expiresAt = LocalDateUtils.nextDaysSecond(ExpireEnum.REFRESH_TOKEN.getTime(), null);

        // 1. 拼装待加密字符串（username + accessToken + 当前精确到毫秒的时间戳）
        String token = at.getAccessToken();
        String str = user.getUsername() + token + String.valueOf(System.currentTimeMillis());

        // 2. SHA1加密
        String refreshTokenStr = "2." + Digest.getSHA1(str) + "." + expiresIn + "." + expiresAt;

        // 3. 保存 Refresh Token
        RefreshToken savedRefreshToken = RefreshTokenDAO.setWhereQuery("tokenId", at.getId()).findOne();

        // 如果存在tokenId匹配的记录，则更新原记录，否则向数据库中插入新记录
        if (savedRefreshToken != null) {
            savedRefreshToken.setRefreshToken(refreshTokenStr);
            savedRefreshToken.setExpiresIn(expiresAt);
//			savedRefreshToken.setUpdateUser(user.getId());
            RefreshTokenDAO.update(savedRefreshToken);
        } else {
            savedRefreshToken = new RefreshToken();
            savedRefreshToken.setTokenId(at.getId());
            savedRefreshToken.setRefreshToken(refreshTokenStr);
            savedRefreshToken.setExpiresIn(expiresAt);
//			savedRefreshToken.setCreateUser(user.getId());
//			savedRefreshToken.setUpdateUser(user.getId());
            RefreshTokenDAO.create(savedRefreshToken);
        }

        // 4. 返回Refresh Token
        return refreshTokenStr;
    }

}
