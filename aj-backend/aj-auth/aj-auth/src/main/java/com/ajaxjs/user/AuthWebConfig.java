package com.ajaxjs.user;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;


@Configuration
public class AuthWebConfig {
    @Value("${sms.accessKeyId}")
    private String accessKeyId;

    @Value("${sms.accessSecret}")
    private String accessSecret;

    @Value("${sms.signName}")
    private String signName;

    @Value("${sms.templateCode}")
    private String templateCode;
}
