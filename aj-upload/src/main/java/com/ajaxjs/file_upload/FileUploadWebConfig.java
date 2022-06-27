package com.ajaxjs.file_upload;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

import com.ajaxjs.file_upload.s3.NsoHttpUpload;
import com.ajaxjs.util.config.EasyConfig;
import com.ajaxjs.util.spring.BaseWebMvcConfigurer;

@Configuration
public class FileUploadWebConfig extends BaseWebMvcConfigurer {
	/**
	 * 跨域
	 *
	 * @return
	 */
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**").allowedHeaders("*").allowedMethods("POST", "GET", "PUT", "OPTIONS", "DELETE").allowedOrigins("*");
	}

	@Autowired
	private ServletContext servletCxt;

	@Bean
	EasyConfig EasyConfig() {
		EasyConfig e = new EasyConfig();
		servletCxt.setAttribute("EASY_CONFIG", e);

		return e;
	}

	@Bean
	public IFileUpload fileUpload() {
		return new NsoHttpUpload();
	}

//	@Bean
//	public OssUpload OssUpload() {
//		return new OssUpload();
//	}
}
