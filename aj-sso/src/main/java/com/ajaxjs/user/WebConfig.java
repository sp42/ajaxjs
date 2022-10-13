package com.ajaxjs.user;

import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;

import com.ajaxjs.spring.BaseWebMvcConfigurer;
import com.ajaxjs.spring.response.MyJsonConverter;

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

	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		// 统一返回 JSON
		converters.add(new MyJsonConverter());
	}
}
