package com.ajaxjs.cms.config;

import com.ajaxjs.data.jdbc_helper.JdbcConn;
import com.ajaxjs.data.jdbc_helper.JdbcWriter;
import com.ajaxjs.iam.resource_server.UserInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.sql.DataSource;

/**
 * 程序配置
 */
@Configuration
public class CmsConfiguration implements WebMvcConfigurer {
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

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor()).addPathPatterns("/**");
    }

    /**
     * 用户全局拦截器
     */
    @Bean
    UserInterceptor authInterceptor() {
        return new UserInterceptor();
    }

    /**
     * 数据验证框架
     *
     * @return 数据验证框架
     */
//    @Bean
//    LocalValidatorFactoryBean localValidatorFactoryBean() {
//        LocalValidatorFactoryBean v = new LocalValidatorFactoryBean();
//        v.setProviderClass(ApacheValidationProvider.class);
//
//        return v;
//    }

    /**
     * 跨域
     *
     * @param registry 注册跨域
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedHeaders("*").allowedMethods("POST", "GET", "PUT", "OPTIONS", "DELETE").allowedOrigins("*");
    }

}
