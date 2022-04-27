package com.ajaxjs.user.sso;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ajaxjs.framework.BaseController;
import com.ajaxjs.net.http.Post;

@RestController
@RequestMapping("/sso")
public class SsoClientController extends BaseController {
	@Value("${OAuth.api}")
	private String api;
	@Value("${OAuth.clientId}")
	private String clientId;
	@Value("${OAuth.clientSecret}")
	private String clientSecret;

	@GetMapping(value = "clientLogin", produces = JSON)
	public String clientLogin(@RequestParam String code) {
		Map<String, Object> params = new HashMap<>();
		params.put("code", code);
		params.put("grant_type", SsoClientConstanst.GRANT_TYPE);
		params.put("client_id", clientId);
		params.put("client_secret", clientSecret);

		Map<String, Object> result = Post.api(api + "/sso/authorize", params);

		return toJson(result);
	}

}
