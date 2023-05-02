package com.ajaxjs.user;

import com.ajaxjs.framework.spring.BaseWebMvcConfigure;
import com.ajaxjs.framework.spring.EmbeddedTomcatStarter;
import com.ajaxjs.framework.spring.easy_controller.ServiceBeanDefinitionRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebMvc
@ComponentScan("com.ajaxjs.user")
public class UserApp extends BaseWebMvcConfigure {
    public static void main(String[] args) {
        EmbeddedTomcatStarter.start(8300, UserApp.class, UserConfig.class);
    }
    @Bean
    ServiceBeanDefinitionRegistry ServiceBeanDefinitionRegistry() {
        return ServiceBeanDefinitionRegistry.init(getClass());
    }
}
