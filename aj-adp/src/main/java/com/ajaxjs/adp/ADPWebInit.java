package com.ajaxjs.adp;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.ajaxjs.spring.BaseWebInitializer;
import com.ajaxjs.spring.easy_controller.ServiceBeanDefinitionRegistry;

@Configuration
@EnableWebMvc
public class ADPWebInit extends BaseWebInitializer {
	@ComponentScan({ "com.ajaxjs.adp" })
	public static class ScanComponent {
	}

	@Override
	public String getMainConfig() {
		return "com.ajaxjs.adp.ADPWebInit.ScanComponent";
	}

	@Bean
	ServiceBeanDefinitionRegistry ServiceBeanDefinitionRegistry() {
		return new ServiceBeanDefinitionRegistry("com.ajaxjs.adp");
	}
}