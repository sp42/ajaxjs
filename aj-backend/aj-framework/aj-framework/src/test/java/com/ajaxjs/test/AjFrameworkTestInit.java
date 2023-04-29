package com.ajaxjs.test;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.ajaxjs.framework.spring.BaseWebInitializer;
import com.ajaxjs.framework.spring.easy_controller.ServiceBeanDefinitionRegistry;

@Configuration
//@EnableWebMvc
public class AjFrameworkTestInit extends BaseWebInitializer {
	@ComponentScan({ "com.ajaxjs.test" })
	public static class ScanComponent {
	}

	@Bean
	ServiceBeanDefinitionRegistry ServiceBeanDefinitionRegistry() {
		return new ServiceBeanDefinitionRegistry(getClass().getPackage().getName());
	}
}
