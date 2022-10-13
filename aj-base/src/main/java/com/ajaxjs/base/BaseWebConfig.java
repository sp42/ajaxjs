package com.ajaxjs.base;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

import com.ajaxjs.file_upload.IFileUpload;
import com.ajaxjs.file_upload.s3.NsoHttpUpload;
import com.ajaxjs.message.sms.AliyunSmsEntity;
import com.ajaxjs.spring.BaseWebMvcConfigurer;
import com.ajaxjs.util.config.EasyConfig;

@Configuration
public class BaseWebConfig extends BaseWebMvcConfigurer {
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

	@Value("${sms.accessKeyId}")
	private String accessKeyId;

	@Value("${sms.accessSecret}")
	private String accessSecret;

	@Value("${sms.signName}")
	private String signName;

	@Value("${sms.templateCode}")
	private String templateCode;

	@Bean
	AliyunSmsEntity AliyunSmsEntity() {
		AliyunSmsEntity sms = new AliyunSmsEntity();
		sms.setAccessKeyId(accessKeyId);
		sms.setAccessSecret(accessSecret);
		sms.setSignName(signName);
		sms.setTemplateCode(templateCode);

		return sms;
	}
}
