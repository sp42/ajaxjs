package com.ajaxjs.user.sso.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.ajaxjs.framework.BaseController;
import com.ajaxjs.user.User;
import com.ajaxjs.user.common.util.UserUtils;
import com.ajaxjs.user.sso.SsoConstants;
import com.ajaxjs.user.sso.model.ClientDetails;
import com.ajaxjs.user.sso.model.ClientUser;
import com.ajaxjs.user.sso.model.Scope;
import com.ajaxjs.user.sso.service.SsoDAO;
import com.ajaxjs.util.StrUtil;
import com.ajaxjs.util.map.JsonHelper;

/**
 * OAuth 使用，有部分暂时未使用
 * 
 * @author Frank Cheung<sp42@qq.com>
 *
 */
@RestController
@RequestMapping("/oauth")
public class OauthController implements SsoDAO {
	/**
	 * 注册需要接入的客户端信息
	 * 
	 * @param client
	 * @return
	 */
	@PostMapping(value = "/clientRegister", produces = MediaType.APPLICATION_JSON_VALUE)
	public String clientRegister(@RequestParam ClientDetails client) {
		if (!StringUtils.hasText(client.getName()))
			throw new IllegalArgumentException("客户端的名称和回调地址不能为空");

		String clientId = StrUtil.getRandomString(24);// 生成24位随机的 clientId
		ClientDetails savedClientDetails = findClientDetailsByClientId(clientId);

		// 生成的 clientId 必须是唯一的，尝试十次避免有重复的 clientId
		for (int i = 0; i < 10; i++) {
			if (savedClientDetails == null)
				break;
			else {
				clientId = StrUtil.getRandomString(24);
				savedClientDetails = findClientDetailsByClientId(clientId);
			}
		}

		client.setClientId(clientId);
		client.setClientSecret(StrUtil.getRandomString(32));

		// 保存到数据库
		return ClientDetailDAO.create(client) == null ? BaseController.jsonNoOk() : BaseController.jsonOk();
	}

	/**
	 * 授权页面
	 * 
	 * @param request     请求对象
	 * @param redirectUri 在页面同意授权后的回调地址
	 * @param client_id   客户端 ID
	 * @param scope       权限范围
	 * @return
	 */
	@RequestMapping("/authorizePage")
	public ModelAndView authorizePage(HttpServletRequest request, @RequestParam String redirectUri, @RequestParam String client_id, @RequestParam String scope) {
		ModelAndView modelAndView = new ModelAndView("authorize");

		// 将回调地址添加到 session 中
		if (StringUtils.hasText(redirectUri))
			request.getSession().setAttribute(SsoConstants.SESSION_AUTH_REDIRECT_URL, redirectUri);

		// 查询请求授权的客户端信息
		ClientDetails clientDetails = findClientDetailsByClientId(client_id);
		modelAndView.addObject("clientId", client_id);
		modelAndView.addObject("clientName", clientDetails.getName());
		modelAndView.addObject("scope", scope);

		return modelAndView;
	}

	/**
	 * 同意授权
	 * 
	 * @param request   请求对象
	 * @param client_id 客户端 ID
	 * @param scope     权限范围
	 * @return
	 */
	@PostMapping(value = "/agree", produces = MediaType.APPLICATION_JSON_VALUE)
	public String agree(HttpServletRequest request, @RequestParam String client_id, @RequestParam String scope) {
		if (!StringUtils.hasText(client_id) || !StringUtils.hasText(scope))
			throw new IllegalArgumentException("请求参数不能为空！");

		User user = (User) UserUtils.getLoginedUser(request);
		long userId = user.getId();
		ClientDetails clientDetails = findClientDetailsByClientId(client_id);
		Scope scopeInfo = ScopeDAO.setWhereQuery("name", scope).findOne();

		if (clientDetails != null && scopeInfo != null) {
			ClientUser clientUser = ClientUserDAO.findByClientId(clientDetails.getId(), userId, scopeInfo.getId());

			if (clientUser == null) {// 如果数据库中不存在记录，则插入
				clientUser = new ClientUser();
				clientUser.setUserId(userId);
				clientUser.setAuthClientId(clientDetails.getId());
				clientUser.setAuthScopeId(scopeInfo.getId());
				ClientUserDAO.create(clientUser);
			}

			Map<String, Object> result = new HashMap<>(2);
			result.put("code", 200);

			// 授权成功之后的回调地址
			HttpSession session = request.getSession();
			String redirectUrl = (String) session.getAttribute(SsoConstants.SESSION_AUTH_REDIRECT_URL);
			session.removeAttribute(SsoConstants.SESSION_AUTH_REDIRECT_URL);

			if (StringUtils.hasText(redirectUrl))
				result.put("redirect_uri", redirectUrl);

			return JsonHelper.toJson(result);
		} else
			throw new IllegalStateException("授权失败！");
	}
}
