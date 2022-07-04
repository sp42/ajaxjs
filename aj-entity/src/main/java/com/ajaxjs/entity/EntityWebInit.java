package com.ajaxjs.entity;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.ajaxjs.spring.BaseWebInitializer;
import com.ajaxjs.spring.easy_controller.ServiceBeanDefinitionRegistry;

@Configuration
@EnableWebMvc
public class EntityWebInit extends BaseWebInitializer {
	@ComponentScan({ "com.ajaxjs.rpc", "com.ajaxjs.data_service"  })
	public static class ScanComponent {
	}

	@Override
	public String getMainConfig() {
		return "com.ajaxjs.EntityWebInit.ScanComponent";
	}

	@Bean
	ServiceBeanDefinitionRegistry ServiceBeanDefinitionRegistry() {
		return new ServiceBeanDefinitionRegistry("com.ajaxjs.rpc");
	}
}