 package com.ajaxjs.database_meta;

 import com.ajaxjs.framework.spring.BaseWebMvcConfigure;
 import com.ajaxjs.framework.spring.EmbeddedTomcatStarter;
 import org.springframework.context.annotation.ComponentScan;
 import org.springframework.context.annotation.Configuration;
 import org.springframework.web.servlet.config.annotation.EnableWebMvc;

 @Configuration
 @EnableWebMvc
 @ComponentScan(" com.ajaxjs.db_meta")
 public class DbMetaApplication extends BaseWebMvcConfigure {
     public static void main(String[] args) {
         EmbeddedTomcatStarter.start(8301, DbMetaApplication.class, DbMetaConfiguration.class);
     }
 }
