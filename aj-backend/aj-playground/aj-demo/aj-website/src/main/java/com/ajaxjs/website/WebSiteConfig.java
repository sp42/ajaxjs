package com.ajaxjs.website;

import com.ajaxjs.framework.entity.SmallMyBatis;
import com.ajaxjs.sql.JdbcConnection;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * 程序配置
 */
@Configuration
public class WebSiteConfig {
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

    @Bean
    SmallMyBatis getXmlSqlHelper() {
        SmallMyBatis my = new SmallMyBatis();
        my.loadXML();

        return my;
    }

}
