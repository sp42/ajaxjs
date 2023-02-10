package com.ajaxjs.sso.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.servlet.ModelAndView;

import com.ajaxjs.sso.model.ClientDetails;

/**
 * OAuth 使用，有部分暂时未使用
 * 
 * @author Frank Cheung sp42@qq.com
 *
 */
public interface IOauthService extends IBaseService {
	/**
	 * 注册需要接入的客户端信息
	 * 
	 * @param client
	 * @return true 表示注册成功
	 */
	Boolean clientRegister(ClientDetails client);

	/**
	 * 授权页面
	 * 
	 * @param request     请求对象
	 * @param redirectUri 在页面同意授权后的回调地址
	 * @param client_id   客户端 ID
	 * @param scope       权限范围
	 * @return
	 */
	ModelAndView authorizePage(HttpServletRequest request, String redirectUri, String client_id, String scope);

	/**
	 * 同意授权
	 * 
	 * @param request   请求对象
	 * @param client_id 客户端 ID
	 * @param scope     权限范围
	 * @return
	 */
	Argee agree(HttpServletRequest request, String client_id, String scope);
}
