package com.ajaxjs;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan({ "com.ajaxjs.data_service", "com.ajaxjs.sso" })
public class TestConfig {

}
