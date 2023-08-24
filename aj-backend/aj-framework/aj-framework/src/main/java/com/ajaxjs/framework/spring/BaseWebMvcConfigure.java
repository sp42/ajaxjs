package com.ajaxjs.framework.spring;

import com.ajaxjs.framework.spring.filter.GlobalExceptionHandler;
import com.ajaxjs.framework.spring.filter.ShowControllerInterceptor;
import com.ajaxjs.framework.spring.filter.dbconnection.DataBaseConnection;
import com.ajaxjs.framework.spring.response.MyJsonConverter;
import com.ajaxjs.framework.spring.validator.ValidatorInitializing;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;
import java.util.Objects;

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

    @Bean
    public ValidatorInitializing ValidatorContextAware() {
        return new ValidatorInitializing();
    }

    /**
     * YAML 配置文件
     *
     * @return YAML 配置文件
     */
    @Bean
    public PropertySourcesPlaceholderConfigurer properties() {
        PropertySourcesPlaceholderConfigurer cfg = new CustomPropertySources();
        cfg.setIgnoreUnresolvablePlaceholders(true);// Don't fail if @Value is not supplied in properties. Ignore if not found
        YamlPropertiesFactoryBean yaml = new YamlPropertiesFactoryBean();
        ClassPathResource c = new ClassPathResource("application.yml");

        if (c.exists()) {
            yaml.setResources(c);
            cfg.setProperties(Objects.requireNonNull(yaml.getObject()));
        } else System.err.println("未设置 YAML 配置文件");

        return cfg;
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
