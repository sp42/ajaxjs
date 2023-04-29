package com.ajaxjs.cms.app_config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ajaxjs.security.google_captcha.GoogleCaptchaMvcInterceptor;
import com.ajaxjs.security.google_captcha.GoogleFilter;
import com.ajaxjs.security.google_captcha.GoolgeCaptchaConfig;

/**
 * 谷歌验证码配置
 * 
 * @author Frank Cheung
 *
 */
@Configuration
public class GoolgeCaptchaSpringConfig {
	/**
	 * 核心校验器
	 * 
	 * @return
	 */
	@Bean
	GoogleFilter googleFilter() {
		return new GoogleFilter();
	}

	@Value("${GoolgeCaptacha.accessKeyId}")
	private String goolgeCaptachaAccessKeyId;

	@Value("${GoolgeCaptacha.accessSecret}")
	private String goolgeCaptachaAccessSecret;

	/**
	 * Captcha 配置
	 * 
	 * @return
	 */
	@Bean
	GoolgeCaptchaConfig goolgeCaptachaConfig() {
		GoolgeCaptchaConfig g = new GoolgeCaptchaConfig();
		g.setEnable(true);
		g.setAccessKeyId(goolgeCaptachaAccessKeyId);
		g.setAccessSecret(goolgeCaptachaAccessSecret);

		return g;
	}

	/**
	 * 拦截器
	 * 
	 * @return
	 */
	@Bean
	GoogleCaptchaMvcInterceptor googleCaptchaMvcInterceptor() {
		return new GoogleCaptchaMvcInterceptor();
	}
}
