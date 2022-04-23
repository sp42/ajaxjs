package com.ajaxjs.user;

import javax.servlet.ServletContext;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.support.GenericWebApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.ajaxjs.util.spring.BaseWebInitializer;

@Configuration
@EnableWebMvc
public class WebInitializer extends BaseWebInitializer {
	@Override
	public void initWeb(ServletContext servletCxt, GenericWebApplicationContext webCxt) {
	}

}