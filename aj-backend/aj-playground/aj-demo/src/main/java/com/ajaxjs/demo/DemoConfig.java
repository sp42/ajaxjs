package com.ajaxjs.demo;

import com.ajaxjs.data.jdbc_helper.JdbcConn;
import com.ajaxjs.data.jdbc_helper.JdbcWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.sql.DataSource;

@Configuration
public class DemoConfig implements WebMvcConfigurer {
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

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public JdbcWriter jdbcWriter() {
        JdbcWriter jdbcWriter = new JdbcWriter();
        jdbcWriter.setIdField("id");
        jdbcWriter.setIsAutoIns(true);

        return jdbcWriter;
    }
}
