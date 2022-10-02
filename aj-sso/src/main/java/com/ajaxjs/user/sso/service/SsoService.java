package com.ajaxjs.user.sso.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.ModelAndView;

import com.ajaxjs.user.User;
import com.ajaxjs.user.UserCommonDAO;
import com.ajaxjs.user.common.util.UserUtils;
import com.ajaxjs.user.sso.SsoUtil;
import com.ajaxjs.user.sso.model.AccessToken;
import com.ajaxjs.user.sso.model.ClientDetails;
import com.ajaxjs.user.sso.model.ErrorCodeEnum;
import com.ajaxjs.user.sso.model.GrantTypeEnum;
import com.ajaxjs.user.sso.model.IssueToken;
import com.ajaxjs.user.sso.model.IssueTokenWithUser;
import com.ajaxjs.user.sso.model.RefreshToken;
import com.ajaxjs.util.cache.ExpireCache;
import com.ajaxjs.util.logger.LogHelper;

/**
 * 单点登录接口
 * 
 * @author Frank Cheung
 *
 */
@Service
public class SsoService implements ISsoService {
	private static final LogHelper LOGGER = LogHelper.getLog(SsoService.class);

	@Autowired
	private AuthorizationService authService;

	@Override
	public Object authorize(String client_id, String redirect_uri, String scope, String status, HttpServletRequest req) {
		LOGGER.info("获取 Authorization Code");

		User loginedUser = null;

		try {
			loginedUser = UserUtils.getLoginedUser(req);
		} catch (Throwable e) {
			LOGGER.warning(e);
			return SsoUtil.oauthError("invalid_request", e.getMessage());
		}

		// 生成 Authorization Code
		String authorizationCode = authService.createAuthorizationCode(client_id, scope, loginedUser);
		String params = "?code=" + authorizationCode;

		if (StringUtils.hasText(status))
			params += "&status=" + status;

		return new ModelAndView("redirect:" + redirect_uri + params);
	}

	@Override
	public IssueTokenWithUser issue(String client_id, String client_secret, String code, String grant_type, HttpServletRequest request) {
		LOGGER.info("通过 Authorization Code 获取 Access Token");

		// 校验授权方式
		if (!GrantTypeEnum.AUTHORIZATION_CODE.getType().equals(grant_type))
			return SsoUtil.oauthError(ErrorCodeEnum.UNSUPPORTED_GRANT_TYPE);

		ClientDetails savedClientDetails = findClientDetailsByClientId(client_id);

		// 校验请求的客户端秘钥和已保存的秘钥是否匹配
		if (!(savedClientDetails != null && savedClientDetails.getClientSecret().equals(client_secret))) {
			if (savedClientDetails == null)
				LOGGER.info("找不到客户端");

			if (!savedClientDetails.getClientSecret().equals(client_secret))
				LOGGER.info("密钥不匹配");

			return SsoUtil.oauthError(ErrorCodeEnum.INVALID_CLIENT);
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
			return SsoUtil.oauthError(ErrorCodeEnum.INVALID_GRANT);
	}

	@Override
	public IssueToken refreshToken(String refresh_token) {
		LOGGER.info("通过 Refresh Token 刷新 Access Token");
		RefreshToken authRefreshToken = RefreshTokenDAO.setWhereQuery("refreshToken", refresh_token).findOne();

		if (authRefreshToken == null)
			return SsoUtil.oauthError(ErrorCodeEnum.INVALID_GRANT);

		// 如果 Refresh Token 已经失效，则需要重新生成
		if (SsoUtil.checkIfExpire(authRefreshToken))
			return SsoUtil.oauthError(ErrorCodeEnum.EXPIRED_TOKEN);

		// 获取存储的 Access Token
		AccessToken authAccessToken = AcessTokenDAO.findById(authRefreshToken.getTokenId());
		// 获取对应的客户端信息
		ClientDetails savedClientDetails = ClientDetailDAO.findById(authAccessToken.getClientId());
		// 获取对应的用户信息
		User user = UserCommonDAO.UserDAO.findById(authAccessToken.getUserId());

		// 生成新的 Access Token
		AccessToken newAccessToken = authService.createAccessToken(user, savedClientDetails, authAccessToken.getGrantType(),
				authAccessToken.getScope());

		IssueToken token = new IssueToken(); // 返回数据
		token.setAccess_token(newAccessToken.getAccessToken());
		token.setRefresh_token(refresh_token);
		token.setExpires_in(newAccessToken.getExpiresIn());
		token.setScope(authAccessToken.getScope());

		return token;
	}
}
