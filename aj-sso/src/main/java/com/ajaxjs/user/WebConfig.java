package com.ajaxjs.user;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;

import com.ajaxjs.message.sms.AliyunSmsEntity;
import com.ajaxjs.spring.BaseWebMvcConfigurer;
import com.ajaxjs.util.config.EasyConfig;

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

	@Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 将/static/**访问映射到classpath:/mystatic/
        registry.addResourceHandler("/common/**").addResourceLocations("/common/");
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
