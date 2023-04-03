package com.ajaxjs.test;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ajaxjs.data_service.DataSerivceUtils;
import com.ajaxjs.data_service.model.DataServiceConfig;
import com.ajaxjs.data_service.service.DataService;
import com.ajaxjs.framework.spring.BaseWebMvcConfigure;

@Configuration
public class AjFrameworkConfig extends BaseWebMvcConfigure {
	@Value("${db.url}")
	private String url;

	@Value("${db.user}")
	private String user;

	@Value("${db.psw}")
	private String psw;

	@Bean(value = "dataSource", destroyMethod = "close")
	DataSource getDs() {
		return DataSerivceUtils.setupJdbcPool("com.mysql.cj.jdbc.Driver", url, user, psw);
	}

	@Bean
	DataService dataService() {
		DataService ds = new DataService();
		DataServiceConfig cfg = new DataServiceConfig();
		cfg.setMultiDataSource(false);
		cfg.setDataSource(getDs());
		ds.setCfg(cfg);

		return ds;
	}
}
