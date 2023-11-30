package com.ajaxjs.iam.client;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import com.ajaxjs.iam.jwt.JwtAccessToken;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Data;

@Data
public abstract class BaseOidcClientUserController {
	@Value("${user.tokenApi}")
	private String tokenApi;

	@Autowired(required = false)
	RestTemplate restTemplate;

	public RestTemplate getRestTemplate() {
		if (restTemplate == null) {
			SkipSSL.init(); // 忽略 SSL 证书
			restTemplate = new RestTemplate();

			MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
			converter.setObjectMapper(new ObjectMapper());
			restTemplate.getMessageConverters().add(converter);
		}

		return restTemplate;
	}

	/**
	 * 用 AccessToken 可用的时候
	 */
	public abstract JwtAccessToken onAccessTokenGot(JwtAccessToken token, HttpSession session);
}
