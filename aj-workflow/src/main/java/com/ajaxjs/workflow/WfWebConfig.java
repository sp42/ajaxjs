package com.ajaxjs.workflow;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

import com.ajaxjs.data_service.model.DataServiceConfig;
import com.ajaxjs.data_service.mybatis.MSUtils;
import com.ajaxjs.spring.BaseWebMvcConfigurer;
import com.ajaxjs.util.cache.CacheManager;
import com.ajaxjs.util.cache.MemoryCacheManager;

@Configuration
public class WfWebConfig extends BaseWebMvcConfigurer {

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
		return MSUtils.setupJdbcPool("com.mysql.cj.jdbc.Driver", url, user, psw);
	}

	@Bean
	CacheManager MemoryCacheManager() {
		return new MemoryCacheManager();
	}
}
