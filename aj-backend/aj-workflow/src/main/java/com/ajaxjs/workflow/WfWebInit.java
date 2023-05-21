package com.ajaxjs.workflow;

import com.ajaxjs.framework.spring.BaseWebMvcConfigure;
import com.ajaxjs.framework.spring.EmbeddedTomcatStarter;
import com.ajaxjs.framework.spring.easy_controller.ServiceBeanDefinitionRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebMvc
@ComponentScan("com.ajaxjs.workflow")
public class WfWebInit extends BaseWebMvcConfigure {
    public static void main(String[] args) {
        EmbeddedTomcatStarter.start(8301, WfWebInit.class, WfWebConfig.class);
    }

    @Bean
    ServiceBeanDefinitionRegistry ServiceBeanDefinitionRegistry() {
        return ServiceBeanDefinitionRegistry.init(getClass());
    }
}