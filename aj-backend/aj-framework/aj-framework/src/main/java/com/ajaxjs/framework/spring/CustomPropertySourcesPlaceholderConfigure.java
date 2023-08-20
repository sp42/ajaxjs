package com.ajaxjs.framework.spring;

import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

import java.io.IOException;
import java.util.Properties;

/**
 * PropertySourcesPlaceholderConfigurer 是一个由 Spring 提供的用于解析属性占位符的配置类，
 * 它没有提供直接获取私有属性 localProperties 的公开方法。但是，可以通过以下步骤获取 localProperties 的值
 */
public class CustomPropertySourcesPlaceholderConfigure extends PropertySourcesPlaceholderConfigurer {
    private Properties localProperties;

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) {
        super.postProcessBeanFactory(beanFactory);

        try {
            localProperties = mergeProperties();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Properties getLocalProperties() {
        return localProperties;
    }
}
