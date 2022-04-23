package com.ajaxjs.user;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

import com.ajaxjs.message.sms.AliyunSmsEntity;
import com.ajaxjs.util.config.EasyConfig;
import com.ajaxjs.util.spring.BaseWebMvcConfigurer;

@Configuration
public class WebConfig extends BaseWebMvcConfigurer {
	/**
	 * 跨域
	 *
	 * @return
	 */
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**").allowedHeaders("*").allowedMethods("POST", "GET", "PUT", "OPTIONS", "DELETE").allowedOrigins("*");
	}

	@Autowired
	private ServletContext servletCxt;

	/**
	 * 配置器
	 * 
	 * @return
	 */
	@Bean
	EasyConfig EasyConfig() {
		EasyConfig e = new EasyConfig();
		servletCxt.setAttribute("EASY_CONFIG", e);

		return e;
	}

	@Value("${sms.accessKeyId}")
	private String accessKeyId;

	@Value("${sms.accessSecret}")
	private String accessSecret;

	@Value("${sms.signName}")
	private String signName;

	@Value("${sms.templateCode}")
	private String templateCode;

	@Bean
	AliyunSmsEntity AliyunSmsEntity() {
		AliyunSmsEntity sms = new AliyunSmsEntity();
		sms.setAccessKeyId(accessKeyId);
		sms.setAccessSecret(accessSecret);
		sms.setSignName(signName);
		sms.setTemplateCode(templateCode);

		return sms;
	}
}
