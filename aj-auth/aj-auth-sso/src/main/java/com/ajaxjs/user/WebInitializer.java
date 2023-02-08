package com.ajaxjs.user;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.ajaxjs.spring.BaseWebInitializer;
import com.ajaxjs.spring.easy_controller.ServiceBeanDefinitionRegistry;

@Configuration
@EnableWebMvc
public class WebInitializer extends BaseWebInitializer {
	@ComponentScan({ "com.ajaxjs.data_service", "com.ajaxjs.user" })
	public static class ScanComponent {
	}

	@Override
	public String getMainConfig() {
		return "com.ajaxjs.user.WebInitializer.ScanComponent";
	}

	@Bean
	ServiceBeanDefinitionRegistry ServiceBeanDefinitionRegistry() {
		return new ServiceBeanDefinitionRegistry("com.toway.droneswarm");
	}

}