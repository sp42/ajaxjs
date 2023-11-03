package com.ajaxjs.demo;

import com.ajaxjs.data.sql_controller.ServiceBeanDefinitionRegistry;
import com.ajaxjs.framework.spring.BaseWebMvcConfigure;
import com.ajaxjs.framework.spring.EmbeddedTomcatStarter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebMvc
@ComponentScan("com.ajaxjs.demo")
public class DemoApp extends BaseWebMvcConfigure {
    public static void main(String[] args) {
        EmbeddedTomcatStarter.start(8300, DemoApp.class, DemoConfig.class);
    }

    @Bean
    ServiceBeanDefinitionRegistry ServiceBeanDefinitionRegistry() {
        return ServiceBeanDefinitionRegistry.init(getClass());
    }

}
