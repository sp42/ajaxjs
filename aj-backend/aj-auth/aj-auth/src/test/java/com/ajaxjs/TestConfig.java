package com.ajaxjs;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan({"com.ajaxjs.auth", "com.ajaxjs.sso", "com.ajaxjs.user" })
public class TestConfig {

}
