package com.ajaxjs.web.website;

import org.springframework.context.annotation.Bean;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

public abstract class BaseWebSiteConfig implements WebMvcConfigurer {
	@Override
	public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
		configurer.enable();
	}
	
	/**
	 * YAML 配置文件
	 *
	 * @return YAML 配置文件
	 */
	@Bean
	public PropertySourcesPlaceholderConfigurer properties() {
		return JspSpringStarter.yaml();
	}
}