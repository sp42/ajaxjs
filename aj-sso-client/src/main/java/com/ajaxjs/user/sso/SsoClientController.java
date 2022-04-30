package com.ajaxjs.user.sso;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ajaxjs.framework.BaseController;
import com.ajaxjs.net.http.Post;
import com.ajaxjs.user.User;
import com.ajaxjs.user.sso.model.AccessToken;
import com.ajaxjs.user.sso.model.UserSession;
import com.ajaxjs.util.map.MapTool;

@RestController
@RequestMapping("/sso")
public class SsoClientController extends BaseController {
	@Value("${OAuth.api}")
	private String api;

	@Value("${OAuth.clientId}")
	private String clientId;

	@Value("${OAuth.clientSecret}")
	private String clientSecret;

	@Value("${User.home}")
	private String userHome;

	static final String GRANT_TYPE = "authorization_code";

	/**
	 * 发起客户端认证
	 * 
	 * @param code
	 * @param req
	 * @return
	 */
	@GetMapping(value = "clientLogin", produces = JSON)
	public String clientLogin(@RequestParam String code, HttpServletRequest req) {
		Map<String, Object> params = new HashMap<>();
		params.put("code", code);
		params.put("grant_type", GRANT_TYPE);
		params.put("client_id", clientId);
		params.put("client_secret", clientSecret);

		Map<String, Object> result = Post.api(api + "/sso/authorize", params);

		UserSession saveSession = saveSession(result);
		// 存入 session
		req.getSession().setAttribute(saveSession.accessToken.getAccessToken(), saveSession);

		return "${User.home}".equals(userHome) ? toJson(result) : "redirect:/" + userHome;
	}

	/**
	 * JSON 结果转换为 Session 存储，形成本地登录状态
	 * 
	 * @param result
	 * @return
	 */
	static UserSession saveSession(Map<String, Object> result) {
		AccessToken accessToken = new AccessToken();
		accessToken.setAccessToken(result.get("access_token").toString());
		accessToken.setRefreshToken(result.get("refresh_token").toString());
		accessToken.setScope(result.get("scope").toString());
		accessToken.setExpiresIn(((Integer) result.get("expires_in")).longValue());

		@SuppressWarnings("unchecked")
		Map<String, Object> userJson = (Map<String, Object>) result.get("user");
		User user = MapTool.map2Bean(userJson, User.class, true);

		UserSession userSession = new UserSession();
		userSession.accessToken = accessToken;
		userSession.user = user;

		return userSession;
	}
}
