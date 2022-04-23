package com.ajaxjs.user.sso.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.ajaxjs.framework.BaseController;
import com.ajaxjs.user.User;
import com.ajaxjs.user.UserCommonDAO;
import com.ajaxjs.user.common.util.UserUtils;
import com.ajaxjs.user.sso.SsoUtil;
import com.ajaxjs.user.sso.model.AccessToken;
import com.ajaxjs.user.sso.model.ClientDetails;
import com.ajaxjs.user.sso.model.ErrorCodeEnum;
import com.ajaxjs.user.sso.model.ExpireEnum;
import com.ajaxjs.user.sso.model.GrantTypeEnum;
import com.ajaxjs.user.sso.model.RefreshToken;
import com.ajaxjs.user.sso.model.IssueToken;
import com.ajaxjs.user.sso.service.AuthorizationService;
import com.ajaxjs.user.sso.service.SsoDAO;
import com.ajaxjs.util.cache.ExpireCache;
import com.ajaxjs.util.date.LocalDateUtils;
import com.ajaxjs.util.logger.LogHelper;
import com.ajaxjs.util.map.JsonHelper;

/**
 * 单点登录接口
 * 
 * @author Frank Cheung<sp42@qq.com>
 *
 */
@RestController
@RequestMapping("/sso")
public class SsoController implements SsoDAO {
	private static final LogHelper LOGGER = LogHelper.getLog(SsoController.class);

	@Autowired
	private AuthorizationService authService;

	/**
	 * 获取 Authorization Code
	 * 
	 * @param client_id    客户端 ID
	 * @param redirect_uri 回调 URL
	 * @param scope        权限范围
	 * @param status       用于防止CSRF攻击（非必填）
	 * @param req          请求对象
	 * @return
	 */
	@RequestMapping(value = "/authorize_code", produces = BaseController.JSON)
	public Object authorize(@RequestParam(required = true) String client_id,
// @formatter:off
	@RequestParam(required = true) String redirect_uri,
	@RequestParam(required = false) String scope,
	@RequestParam(required = false) String status,
	HttpServletRequest req) {
// @formatter:on
		LOGGER.info("获取 Authorization Code");

		User loginedUser = null;
		try {
			loginedUser = UserUtils.getLoginedUser(req);
		} catch (Throwable e) {
			LOGGER.warning(e);
			return SsoUtil.oauthError(ErrorCodeEnum.INVALID_CLIENT);
		}

		// 生成 Authorization Code
		String authorizationCode = authService.createAuthorizationCode(client_id, scope, loginedUser);
		String params = "?code=" + authorizationCode;

		if (StringUtils.hasText(status))
			params += "&status=" + status;

		return new ModelAndView("redirect:" + redirect_uri + params);
	}

	/**
	 * 通过 Authorization Code 获取 Access Token
	 * 
	 * @param client_id     客户端 id
	 * @param client_secret 接入的客户端的密钥
	 * @param code          前面获取的 Authorization Code
	 * @param grant_type    授权方式
	 * @param request       请求对象
	 * @return
	 */
	@RequestMapping("/authorize")
	public String issue(@RequestParam(required = true) String client_id,
// @formatter:off
	@RequestParam(required = true) String client_secret,
	@RequestParam(required = true) String code,
	@RequestParam(required = true) String grant_type,
	HttpServletRequest request) {
// @formatter:on
		LOGGER.info("通过 Authorization Code 获取 Access Token");

		// 校验授权方式
		if (!GrantTypeEnum.AUTHORIZATION_CODE.getType().equals(grant_type))
			return SsoUtil.oauthError(ErrorCodeEnum.UNSUPPORTED_GRANT_TYPE);

		ClientDetails savedClientDetails = findClientDetailsByClientId(client_id);
		// 校验请求的客户端秘钥和已保存的秘钥是否匹配
		if (!(savedClientDetails != null && savedClientDetails.getClientSecret().equals(client_secret)))
			return SsoUtil.oauthError(ErrorCodeEnum.INVALID_CLIENT);

		String scope = ExpireCache.CACHE.get(code + ":scope", String.class);
		User user = ExpireCache.CACHE.get(code + ":user", User.class);

		// 如果能够通过 Authorization Code 获取到对应的用户信息，则说明该 Authorization Code 有效
		if (StringUtils.hasText(scope) && user != null) {
			// 过期时间
			Long expiresIn = LocalDateUtils.dayToSecond(ExpireEnum.ACCESS_TOKEN.getTime());

			// 生成 Access Token
			String accessTokenStr = authService.createAccessToken(user, savedClientDetails, grant_type, scope, expiresIn);
			// 查询已经插入到数据库的 Access Token
			AccessToken authAccessToken = AcessTokenDAO.setWhereQuery("accessToken", accessTokenStr).findOne();
			// 生成 Refresh Token
			String refreshTokenStr = authService.createRefreshToken(user, authAccessToken);

			IssueToken token = new IssueToken(); // 返回数据
			token.setAccess_token(authAccessToken.getAccessToken());
			token.setRefresh_token(refreshTokenStr);
			token.setExpires_in(expiresIn);
			token.setScope(authAccessToken.getScope());

			return JsonHelper.toJson(token);
		} else
			return SsoUtil.oauthError(ErrorCodeEnum.INVALID_GRANT);
	}

	/**
	 * 通过 Refresh Token 刷新 Access Token
	 * 
	 * @param refresh_token
	 * @return
	 */
	@RequestMapping(value = "/refreshToken", produces = MediaType.APPLICATION_JSON_VALUE)
	public String refreshToken(@RequestParam(required = true) String refresh_token) {
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
		// 新的过期时间
		Long expiresIn = LocalDateUtils.dayToSecond(ExpireEnum.ACCESS_TOKEN.getTime());
		// 生成新的 Access Token
		String newAccessTokenStr = authService.createAccessToken(user, savedClientDetails, authAccessToken.getGrantType(), authAccessToken.getScope(), expiresIn);

		IssueToken token = new IssueToken(); // 返回数据
		token.setAccess_token(newAccessTokenStr);
		token.setRefresh_token(refresh_token);
		token.setExpires_in(expiresIn);
		token.setScope(authAccessToken.getScope());

		return JsonHelper.toJson(token);
	}
}
