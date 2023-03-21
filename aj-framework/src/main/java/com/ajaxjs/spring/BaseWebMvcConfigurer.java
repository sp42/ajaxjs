package com.ajaxjs.spring;

import com.ajaxjs.spring.response.MyJsonConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.ajaxjs.util.filter.DataBaseConnection;

import javax.servlet.*;
import java.io.IOException;
import java.util.List;

/**
 * Spring MVC 的配置
 *
 * @author Frank Cheung
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
        registry.addInterceptor(new ShowControllerInterceptor());
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {

        // 统一返回 JSON
        converters.add(new MyJsonConverter());
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
