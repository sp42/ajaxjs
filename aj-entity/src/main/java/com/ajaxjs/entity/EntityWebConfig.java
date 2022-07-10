package com.ajaxjs.entity;

import java.util.List;

import javax.servlet.ServletContext;
import javax.sql.DataSource;

import org.apache.bval.jsr.ApacheValidationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

import com.ajaxjs.data_service.api.Commander;
import com.ajaxjs.data_service.model.DataServiceConfig;
import com.ajaxjs.spring.BaseWebMvcConfigurer;
import com.ajaxjs.spring.response.MyJsonConverter;
import com.ajaxjs.spring.response.RestResponseEntityExceptionHandler;
import com.ajaxjs.util.config.EasyConfig;

@Configuration
public class EntityWebConfig extends BaseWebMvcConfigurer {
	/**
	 * 跨域
	 *
	 * @return
	 */
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**").allowedHeaders("*").allowedMethods("POST", "GET", "PUT", "OPTIONS", "DELETE").allowedOrigins("*");
	}

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

	@Value("${db.url}")
	private String url;

	@Value("${db.user}")
	private String user;

	@Value("${db.psw}")
	private String psw;

	@Bean(value = "dataSource", destroyMethod = "close")
	DataSource getDs() {
		return Commander.setupJdbcPool("com.mysql.cj.jdbc.Driver", url, user, psw);
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
	RestResponseEntityExceptionHandler RestResponseEntityExceptionHandler() {
		return new RestResponseEntityExceptionHandler();
	}

//	@Bean
//	RestResponseStatusExceptionResolver RestResponseStatusExceptionResolver() {
//		return new RestResponseStatusExceptionResolver();
//	}
	/**
	 * 数据验证框架
	 * 
	 * @return
	 */
	@Bean
	LocalValidatorFactoryBean localValidatorFactoryBean() {
		LocalValidatorFactoryBean v = new LocalValidatorFactoryBean();
		v.setProviderClass(ApacheValidationProvider.class);

		return v;
	}

	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		// 统一返回 JSON
		converters.add(new MyJsonConverter());
//		converters.add(new FormHttpMessageConverter());
	}
}
