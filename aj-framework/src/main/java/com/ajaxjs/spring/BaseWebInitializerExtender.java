package com.ajaxjs.spring;

import java.util.List;

import javax.servlet.ServletContext;

import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

/**
 * WebInitializer 配置的扩展器
 * 
 * @author Frank Cheung sp42@qq.com
 *
 */
public interface BaseWebInitializerExtender {
	/**
	 * 对于 contextConfigLocation 的配置
	 * 
	 * @return
	 */
	default String getMainConfig() {
		return "";
	}

	/**
	 * 子类可以配置的方法
	 * 
	 * @param servletContext Servlet 上下文
	 * @param webCxt         如果不想在 WebApplicationInitializer 当前类中注入，可以另设一个类专门注入组件
	 */
	default void initWeb(ServletContext servletContext, AnnotationConfigWebApplicationContext webCxt) {
	}

	/**
	 * 设置是否跨域
	 * 
	 * @param registry
	 */
	default void addCorsMapping(CorsRegistry registry) {
	}

	/**
	 * 设置拦截器
	 * 
	 * @param registry
	 */
	default void addInterceptor(InterceptorRegistry registry) {
	}

	/**
	 * 设置统一返回
	 * 
	 * @param converters
	 */
	default void configureMessageConverter(List<HttpMessageConverter<?>> converters) {
	}
}
