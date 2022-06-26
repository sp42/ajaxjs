package com.ajaxjs.file_upload;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

import com.ajaxjs.data_service.api.Commander;
import com.ajaxjs.data_service.model.DataServiceConfig;
import com.ajaxjs.file_upload.s3.NsoHttpUpload;
import com.ajaxjs.util.spring.BaseWebMvcConfigurer;
import com.ajaxjs.util.spring.response.MyJsonConverter;

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

	@Value("${db.url}")
	private String url;

	@Value("${db.user}")
	private String user;

	@Value("${db.psw}")
	private String psw;
	
	@Bean
	public IFileUpload fileUpload() {
		return new NsoHttpUpload();
	}

//	@Bean
//	public OssUpload OssUpload() {
//		return new OssUpload();
//	}

	/**
	 * 数据服务配置
	 * 
	 * @return
	 */
	@Bean
	DataServiceConfig DataServiceConfig() {
		DataServiceConfig cfg = new DataServiceConfig();
		cfg.setEmbed(false);
		cfg.setDataSource(getDs());

		return cfg;
	}

	@Bean(value = "dataSource", destroyMethod = "close")
	DataSource getDs() {
		return Commander.setupJdbcPool("com.mysql.cj.jdbc.Driver", url, user, psw);
	}

	/**
	 * 要保护的资源（只有登录了才能访问）
	 */
	@Value("${User.resources}")
	private String[] protectPerfix;

	/**
	 * 要保护的资源（只有登录了才能访问）
	 */
	@Value("${User.excludeResources}")
	private String[] excludeResources;

//	@Autowired
//	SsoAccessTokenInterceptor tokenInterceptor;
//
//	/**
//	 * 加入拦截器
//	 */
//	@Override
//	public void addInterceptors(InterceptorRegistry registry) {
//		registry.addInterceptor(tokenInterceptor).addPathPatterns(protectPerfix).excludePathPatterns(excludeResources);
//	}

	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		// 统一返回 JSON
		converters.add(new MyJsonConverter());
	}
}
