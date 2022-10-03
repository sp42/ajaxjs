package com.ajaxjs.user;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan({ "com.ajaxjs.data_service", "com.ajaxjs.user" })
public class TestConfig {

}
