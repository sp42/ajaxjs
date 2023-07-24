package com.ajaxjs.db_meta;

import com.ajaxjs.data.CRUD;
import com.ajaxjs.data.SmallMyBatis;
import com.ajaxjs.database_meta.tools.SqlTools;
import com.ajaxjs.framework.spring.filter.dbconnection.DataBaseConnection;
import com.ajaxjs.sql.JdbcConnection;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

/**
 * 程序配置
 */
@Configuration
public class DbMetaConfiguration implements WebMvcConfigurer {
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

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*") // 允许所有来源
                .allowedMethods("GET", "POST", "PUT", "DELETE") // 允许的请求方法
                .allowedHeaders("*"); // 允许所有请求头
    }

    @Bean
    SmallMyBatis smallMyBatis() {
        SmallMyBatis s = new SmallMyBatis();
        s.loadXML("sql.xml");

        return s;
    }

    @Bean
    SqlTools sqlTools() {
        return new SqlTools();
    }
}
