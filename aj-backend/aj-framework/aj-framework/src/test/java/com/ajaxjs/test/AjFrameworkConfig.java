package com.ajaxjs.test;

import com.ajaxjs.framework.spring.BaseWebMvcConfigure;
import com.ajaxjs.sql.JdbcConnection;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

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
		return JdbcConnection.setupJdbcPool("com.mysql.cj.jdbc.Driver", url, user, psw);
	}
}