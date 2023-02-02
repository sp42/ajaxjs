package com.ajaxjs.auth;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.ajaxjs.spring.BaseWebInitializer;
import com.ajaxjs.spring.easy_controller.ServiceBeanDefinitionRegistry;

@Configuration
@EnableWebMvc
public class AuthWebInit extends BaseWebInitializer {
	@ComponentScan({ "com.ajaxjs.auth" })
	public static class ScanComponent {
	}

	@Bean
	ServiceBeanDefinitionRegistry ServiceBeanDefinitionRegistry() {
		return ServiceBeanDefinitionRegistry(getClass());
	}
}