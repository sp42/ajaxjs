package com.ajaxjs.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.ajaxjs.util.filter.DataBaseConnection;

/**
 * Spring MVC 的配置
 * 
 * @author Frank Cheung
 *
 */
public abstract class BaseWebMvcConfigurer implements WebMvcConfigurer {
	@Override
	public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
		configurer.enable();
	}

	/**
	 * MVC 注解
	 */
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(db());
	}

	/**
	 * 连接数据库
	 * 
	 * @return
	 */
	@Bean
	DataBaseConnection db() {
		return new DataBaseConnection();
	}
}
