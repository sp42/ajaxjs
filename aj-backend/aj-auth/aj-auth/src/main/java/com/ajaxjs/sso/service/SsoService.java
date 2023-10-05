package com.ajaxjs.sso.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.servlet.ModelAndView;

import com.ajaxjs.sso.model.IssueToken;
import com.ajaxjs.sso.model.IssueTokenWithUser;

/**
 * 单点登录接口
 * 
 * @author Frank Cheung sp42@qq.com
 *
 */
public interface SsoService extends IBaseService {
	/**
	 * 获取 Authorization Code
	 * 
	 * @param client_id    客户端 ID
	 * @param redirect_uri 回调 URL
	 * @param scope        权限范围（非必填）
	 * @param status       用于防止C SRF 攻击（非必填）
	 * @param req          请求对象
	 * @return 返回给客户端一个 302 重定向
	 */
	ModelAndView getAuthorizeCode(String client_id, String redirect_uri, String scope, String status, HttpServletRequest req);

	/**
	 * 通过 Authorization Code 获取 Access Token
	 * 
	 * @param client_id     客户端 id
	 * @param client_secret 接入的客户端的密钥
	 * @param code          前面获取的 Authorization Code
	 * @param grant_type    授权方式
	 * @return
	 */
	IssueTokenWithUser issue(String client_id, String client_secret, String code, String grant_type);

	/**
	 * 通过 Refresh Token 刷新 Access Token
	 * 
	 * @param refresh_token
	 * @return
	 */
	IssueToken refreshToken(String refresh_token);
}
