package com.ajaxjs.iam.server.config;

import com.ajaxjs.iam.jwt.JWebTokenMgr;
import com.ajaxjs.iam.user.common.PasswordEncoder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Function;

@Configuration
public class UserConfig {
    @Value("${User.register.pswMD5Salt:a#D2s!As12}")
    private String pswSalt;

    /**
     * 指定密码的加密规则
     */
    @Bean("passwordEncode")
    Function<String, String> passwordEncode() {
        PasswordEncoder e = new PasswordEncoder();
        e.salt = pswSalt;

        return e::md5salt;
    }

    @Value("${User.oidc.jwtSecretKey}")
    private String jwtSecretKey;

    @Bean
    JWebTokenMgr jWebTokenMgr() {
        JWebTokenMgr mgr = new JWebTokenMgr();
        mgr.setSecretKey(jwtSecretKey);

        return mgr;
    }
}
