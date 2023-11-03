package com.ajaxjs.base;

import com.ajaxjs.base.config.BaseConfiguration;
import com.ajaxjs.base.config.ConfigSms;
import com.ajaxjs.base.config.ConfigUpload;
import com.ajaxjs.framework.spring.BaseWebMvcConfigure;
import com.ajaxjs.framework.spring.EmbeddedTomcatStarter;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebMvc
@ComponentScan("com.ajaxjs.base")
public class BaseApplication extends BaseWebMvcConfigure {
    public static void main(String[] args) {
        EmbeddedTomcatStarter.start(8300, BaseApplication.class, BaseConfiguration.class, ConfigSms.class, ConfigUpload.class);
    }

//    @Autowired
//    private ServletContext servletCxt;
//
//    @Bean
//    EasyConfig EasyConfig() {
//        EasyConfig e = new EasyConfig();
//        servletCxt.setAttribute("EASY_CONFIG", e);
//
//        return e;
//    }


}
