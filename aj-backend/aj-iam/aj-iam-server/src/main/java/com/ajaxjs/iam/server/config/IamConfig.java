package com.ajaxjs.iam.server.config;

import com.ajaxjs.data.jdbc_helper.JdbcConn;
import com.ajaxjs.data.jdbc_helper.JdbcWriter;
import com.ajaxjs.iam.user.common.PasswordEncoder;
import com.ajaxjs.iam.user.common.session.ServletUserSession;
import com.ajaxjs.iam.user.common.session.UserSession;
import com.ajaxjs.util.cache.Cache;
import com.ajaxjs.util.cache.expiry.ExpiryCache;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.sql.DataSource;
import java.util.function.Function;

@Configuration
public class IamConfig implements WebMvcConfigurer {
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

    @Bean
    Cache<String, Object> simpleJvmCache() {
        return ExpiryCache.getInstance();
    }

    @Bean
    UserSession UserSession() {
        return new ServletUserSession();
    }

    /**
     * 指定密码的加密规则
     */
    @Bean("passwordEncode")
    Function<String, String> passwordEncode() {
        return PasswordEncoder::md5salt;
    }
}
