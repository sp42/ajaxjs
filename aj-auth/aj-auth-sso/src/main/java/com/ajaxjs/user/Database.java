package com.ajaxjs.user;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ajaxjs.data_service.DataSerivceUtils;
import com.ajaxjs.data_service.model.DataServiceConfig;
import com.ajaxjs.data_service.service.DataService;

/**
 * 数据库连接配置 & 数据服务
 * 
 * @author Frank Cheung
 *
 */
@Configuration
public class Database {
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
	DataService DataServiceConfig() {
		DataServiceConfig cfg = new DataServiceConfig();
		cfg.setEmbed(false);
		cfg.setDataSource(getDs());

		DataService ds = new DataService();
		ds.setCfg(cfg);

		return ds;
	}

	@Bean(value = "dataSource", destroyMethod = "close")
	DataSource getDs() {
		return DataSerivceUtils.setupJdbcPool("com.mysql.cj.jdbc.Driver", url, user, psw);
	}
}
