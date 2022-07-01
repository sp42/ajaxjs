package com.ajaxjs.file_upload;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.ajaxjs.easy_controller.ServiceBeanDefinitionRegistry;
import com.ajaxjs.spring.BaseWebInitializer;

@Configuration
@EnableWebMvc
public class FileUploadWebInit extends BaseWebInitializer {
	@ComponentScan({ "com.ajaxjs.file_upload" })
	public static class ScanComponent {
	}

	@Override
	public String getMainConfig() {
		return "com.ajaxjs.file_upload.FileUploadWebInit.ScanComponent";
	}

	@Bean
	ServiceBeanDefinitionRegistry ServiceBeanDefinitionRegistry() {
		return new ServiceBeanDefinitionRegistry("com.ajaxjs.file_upload");
	}
}