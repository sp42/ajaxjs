package com.ajaxjs.user;

import java.io.Serializable;

import org.springframework.stereotype.Component;

import com.ajaxjs.model.AccessToken;
import com.ajaxjs.model.ClientDetails;
import com.ajaxjs.model.ExpireEnum;
import com.ajaxjs.model.RefreshToken;
import com.ajaxjs.util.cryptography.Digest;
import com.ajaxjs.util.date.LocalDateUtils;

/**
 * 授权操作
 *
 * @author Frank Cheung
 */
@Component
public class AuthorizationService implements SsoDAO {


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

    /**
     * @param user
     * @param at
     * @return
     */
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
