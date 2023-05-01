package com.ajaxjs.base;

import com.ajaxjs.framework.config.EasyConfig;
import com.ajaxjs.framework.spring.BaseWebMvcConfigure;
import com.ajaxjs.framework.spring.EmbeddedTomcatStarter;
import com.ajaxjs.framework.spring.easy_controller.ServiceBeanDefinitionRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.servlet.ServletContext;

@Configuration
@EnableWebMvc
@ComponentScan("com.ajaxjs.base")
public class BaseApplication extends BaseWebMvcConfigure {
    public static void main(String[] args) {
        EmbeddedTomcatStarter.start(8300, BaseApplication.class, BaseConfiguration.class, ConfigSms.class, ConfigUpload.class);
    }

//    @Autowired
//    private ServletContext servletCxt;
//
//    @Bean
//    EasyConfig EasyConfig() {
//        EasyConfig e = new EasyConfig();
//        servletCxt.setAttribute("EASY_CONFIG", e);
//
//        return e;
//    }

    @Bean
    ServiceBeanDefinitionRegistry ServiceBeanDefinitionRegistry() {
        return ServiceBeanDefinitionRegistry.init(getClass());
    }
}
