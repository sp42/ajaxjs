package com.ajaxjs.user.sso.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.ajaxjs.spring.easy_controller.InterfaceBasedController;
import com.ajaxjs.user.sso.model.ClientDetails;
import com.ajaxjs.user.sso.service.IOauthService;
import com.ajaxjs.user.sso.service.OauthService;

/**
 * OAuth 使用，有部分暂时未使用
 * 
 * @author Frank Cheung
 *
 */
@RestController
@RequestMapping("/oauth")
@InterfaceBasedController(serviceClass = OauthService.class)
public interface OauthController extends IOauthService {
	@PostMapping("/clientRegister")
	@Override
	Boolean clientRegister(@RequestParam ClientDetails client);

	@RequestMapping("/authorizePage")
	@Override
	public ModelAndView authorizePage(HttpServletRequest request, @RequestParam String redirectUri, @RequestParam String client_id,
			@RequestParam String scope);

	/**
	 * 同意授权
	 * 
	 * @param request   请求对象
	 * @param client_id 客户端 ID
	 * @param scope     权限范围
	 * @return
	 */
	@PostMapping("/agree")
	@Override
	public Argee agree(HttpServletRequest request, @RequestParam String client_id, @RequestParam String scope);
}
