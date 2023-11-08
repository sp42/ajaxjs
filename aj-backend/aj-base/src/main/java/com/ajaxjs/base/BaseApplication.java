package com.ajaxjs.base;

import com.ajaxjs.base.config.BaseConfiguration;
import com.ajaxjs.base.config.MessageConfiguration;
import com.ajaxjs.base.config.UploadConfiguration;
import com.ajaxjs.framework.embeded_tomcat.BaseWebMvcConfigure;
import com.ajaxjs.framework.embeded_tomcat.EmbeddedTomcatStarter;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebMvc
@ComponentScan("com.ajaxjs.base")
public class BaseApplication extends BaseWebMvcConfigure {
    public static void main(String[] args) {
        EmbeddedTomcatStarter.start(8300, BaseApplication.class, BaseConfiguration.class, MessageConfiguration.class, UploadConfiguration.class);
    }

}
