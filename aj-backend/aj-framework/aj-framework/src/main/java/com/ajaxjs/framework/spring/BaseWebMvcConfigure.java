package com.ajaxjs.framework.spring;

import com.ajaxjs.framework.spring.filter.BeanValidation;
import com.ajaxjs.framework.spring.filter.RestResponseEntityExceptionHandler;
import com.ajaxjs.framework.spring.filter.dbconnection.DataBaseConnection;
import com.ajaxjs.framework.spring.filter.GlobalExceptionHandler;
import com.ajaxjs.framework.spring.filter.ShowControllerInterceptor;
import com.ajaxjs.framework.spring.response.MyJsonConverter;
import com.ajaxjs.framework.spring.response.MyResponseBodyAdvice;
import org.apache.bval.jsr.ApacheValidationProvider;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.MethodParameter;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

/**
 * Spring MVC 的配置
 *
 * @author Frank Cheung
 */
public abstract class BaseWebMvcConfigure implements WebMvcConfigurer {
    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configure) {
        configure.enable();
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
        converters.add(new MyJsonConverter()); // 统一返回 JSON
    }

    @RestControllerAdvice
    private static class _MyResponseBodyAdvice extends MyResponseBodyAdvice {
    }

    /**
     * 数据验证框架
     *
     * @return
     */
//    @Bean
//    LocalValidatorFactoryBean localValidatorFactoryBean() {
//        LocalValidatorFactoryBean v = new LocalValidatorFactoryBean();
//        v.setProviderClass(ApacheValidationProvider.class);
//
//        return v;
//    }

    // Bean 验证前置拦截器
    @Bean
    BeanValidation beanValidation() {
        return new BeanValidation();
    }

// 没抛出 BindException 异常
//    @Bean
//    RestResponseEntityExceptionHandler restResponseEntityExceptionHandler() {
//        return new RestResponseEntityExceptionHandler();
//    }

    /**
     * YAML 配置文件
     *
     * @return YAML 配置文件
     */
    @Bean
    public PropertySourcesPlaceholderConfigurer properties() {
        PropertySourcesPlaceholderConfigurer cfger = new PropertySourcesPlaceholderConfigurer();
        cfger.setIgnoreUnresolvablePlaceholders(true);// Don't fail if @Value is not supplied in properties. Ignore if not found
        YamlPropertiesFactoryBean yaml = new YamlPropertiesFactoryBean();
        ClassPathResource c = new ClassPathResource("application.yml");

        if (c.exists()) {
            yaml.setResources(c);
            cfger.setProperties(Objects.requireNonNull(yaml.getObject()));
        } else System.err.println("未设置 YAML 配置文件");

        return cfger;
    }

    /**
     * 全局异常拦截器
     *
     * @return 全局异常拦截器
     */
    @Bean
    public GlobalExceptionHandler GlobalExceptionHandler() {
        return new GlobalExceptionHandler();
    }

    /**
     * Spring IoC 工具
     *
     * @return IoC 工具
     */
    @Bean
    public DiContextUtil DiContextUtil() {
        return new DiContextUtil();
    }

    /**
     * 连接数据库
     *
     * @return 创建数据连接的拦截器
     */
    @Bean
    DataBaseConnection db() {
        return new DataBaseConnection();
    }
}
