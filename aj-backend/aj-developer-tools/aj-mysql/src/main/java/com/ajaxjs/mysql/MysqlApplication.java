 package com.ajaxjs.mysql;

 import com.ajaxjs.framework.spring.BaseWebMvcConfigure;
 import com.ajaxjs.framework.spring.EmbeddedTomcatStarter;
 import org.springframework.context.annotation.ComponentScan;
 import org.springframework.context.annotation.Configuration;
 import org.springframework.web.servlet.config.annotation.EnableWebMvc;

 @Configuration
 @EnableWebMvc
 @ComponentScan(" com.ajaxjs.mysql")
 public class MysqlApplication extends BaseWebMvcConfigure {
     public static void main(String[] args) {
         EmbeddedTomcatStarter.start(8301, MysqlApplication.class, MysqlConfiguration.class);
     }
 }
