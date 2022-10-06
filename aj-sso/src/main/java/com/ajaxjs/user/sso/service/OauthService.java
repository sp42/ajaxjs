package com.ajaxjs.user.sso.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.ModelAndView;

import com.ajaxjs.user.User;
import com.ajaxjs.user.common.util.UserUtils;
import com.ajaxjs.user.sso.common.SsoConstants;
import com.ajaxjs.user.sso.model.ClientDetails;
import com.ajaxjs.user.sso.model.ClientUser;
import com.ajaxjs.user.sso.model.Scope;
import com.ajaxjs.util.StrUtil;

/**
 * OAuth 使用，有部分暂时未使用
 * 
 * @author Frank Cheung
 *
 */
@Service
public class OauthService implements IOauthService {
	@Override
	public Boolean clientRegister(ClientDetails client) {
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
		return ClientDetailDAO.create(client) == null;
	}

	@Override
	public ModelAndView authorizePage(HttpServletRequest request, String redirectUri, String client_id, String scope) {
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

	@Override
	public Argee agree(HttpServletRequest request, String client_id, String scope) {
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

			Argee agree = new Argee();
			agree.code = 200;

			// 授权成功之后的回调地址
			HttpSession session = request.getSession();
			String redirectUrl = (String) session.getAttribute(SsoConstants.SESSION_AUTH_REDIRECT_URL);
			session.removeAttribute(SsoConstants.SESSION_AUTH_REDIRECT_URL);

			if (StringUtils.hasText(redirectUrl))
				agree.redirect_uri = redirectUrl;
			return agree;
		} else
			throw new IllegalStateException("授权失败！");
	}
}
