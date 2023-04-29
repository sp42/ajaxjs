package com.ajaxjs.adp;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.ajaxjs.framework.spring.BaseWebInitializer;

@Configuration
@EnableWebMvc
@ComponentScan({ "com.ajaxjs.adp" })
public class ADPWebInit extends BaseWebInitializer {
}