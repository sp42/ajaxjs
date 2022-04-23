package com.ajaxjs.user.sso.service;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.ajaxjs.user.User;
import com.ajaxjs.user.sso.model.AccessToken;
import com.ajaxjs.user.sso.model.ClientDetails;
import com.ajaxjs.user.sso.model.ExpireEnum;
import com.ajaxjs.user.sso.model.RefreshToken;
import com.ajaxjs.util.cache.ExpireCache;
import com.ajaxjs.util.cryptography.Digest;
import com.ajaxjs.util.date.LocalDateUtils;

/**
 * 授权操作
 *
 * @author Frank Cheung<sp42@qq.com>
 *
 */
@Component
public class AuthorizationService implements SsoDAO {
	/**
	 * 根据 clientId、scope 以及当前时间戳生成 AuthorizationCode（有效期为10分钟）
	 *
	 * @param clientId 客户端ID
	 * @param scope
	 * @param user     用户信息
	 * @return
	 */
	public String createAuthorizationCode(String clientId, String scope, User user) {
		if (!StringUtils.hasText(scope))
			scope = "DEFAULT_SCOPE";

		// 1. 拼装待加密字符串（clientId + scope + 当前精确到毫秒的时间戳）
		String str = clientId + scope + String.valueOf(System.currentTimeMillis());

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

	/**
	 * 生成 Access Token
	 *
	 * @param user      用户信息
	 * @param client    接入的客户端信息
	 * @param grantType 授权方式
	 * @param scope     允许访问的用户权限范围
	 * @param expiresIn 过期时间
	 * @return
	 */
	public String createAccessToken(User user, ClientDetails client, String grantType, String scope, Long expiresIn) {
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

			AcessTokenDAO.create(at);
		}

		// 4. 返回Access Token
		return accessTokenStr;
	}

	/**
	 * 
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
