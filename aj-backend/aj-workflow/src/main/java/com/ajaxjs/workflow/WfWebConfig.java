package com.ajaxjs.workflow;

import javax.sql.DataSource;

import com.ajaxjs.sql.JdbcConnection;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;


import com.ajaxjs.util.cache.CacheManager;
import com.ajaxjs.util.cache.MemoryCacheManager;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WfWebConfig implements WebMvcConfigurer {
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

    @Bean(value = "dataSource", destroyMethod = "close")
    DataSource getDs() {
        return JdbcConnection.setupJdbcPool("com.mysql.cj.jdbc.Driver", url, user, psw);
    }

    @Bean
    CacheManager MemoryCacheManager() {
        return new MemoryCacheManager();
    }
}
