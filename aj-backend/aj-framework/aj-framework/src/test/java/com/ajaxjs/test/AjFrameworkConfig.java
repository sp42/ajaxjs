package com.ajaxjs.test;

import com.ajaxjs.data.jdbc_helper.JdbcConn;
import com.ajaxjs.framework.embeded_tomcat.BaseWebMvcConfigure;
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
		return JdbcConn.setupJdbcPool("com.mysql.cj.jdbc.Driver", url, user, psw);
	}
}
