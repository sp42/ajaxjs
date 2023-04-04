package com.ajaxjs.adp;

import java.util.List;

import javax.servlet.ServletContext;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

import com.ajaxjs.data_service.DataSerivceUtils;
import com.ajaxjs.data_service.model.DataServiceConfig;
import com.ajaxjs.data_service.service.DataService;
import com.ajaxjs.framework.config.EasyConfig;
import com.ajaxjs.framework.spring.BaseWebMvcConfigure;
import com.ajaxjs.spring.response.MyJsonConverter;

@Configuration
public class ADPWebConfig extends BaseWebMvcConfigure {
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

	@Value("${db.url}")
	private String url;

	@Value("${db.user}")
	private String user;

	@Value("${db.psw}")
	private String psw;

	/**
	 * 数据服务配置
	 * 
	 * @return
	 */
	@Bean
	DataService dataService() {
		DataService ds = new DataService();
		DataServiceConfig cfg = new DataServiceConfig();
		cfg.setMultiDataSource(true);
		cfg.setDataSource(getDs());
		ds.setCfg(cfg);

		return ds;
	}

	@Bean(value = "dataSource", destroyMethod = "close")
	DataSource getDs() {
		return DataSerivceUtils.setupJdbcPool("com.mysql.cj.jdbc.Driver", url, user, psw);
	}

	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		// 统一返回 JSON
		converters.add(new MyJsonConverter());
	}

	@Value("${sms.accessKeyId}")
	private String accessKeyId;

	@Value("${sms.accessSecret}")
	private String accessSecret;

	@Value("${sms.signName}")
	private String signName;

	@Value("${sms.templateCode}")
	private String templateCode;
}
