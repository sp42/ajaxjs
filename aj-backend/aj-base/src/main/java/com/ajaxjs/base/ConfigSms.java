package com.ajaxjs.base;

import com.ajaxjs.message.sms.ali.AliyunSmsEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

/**
 * 短信配置
 */
public class ConfigSms {
    @Value("${sms.accessKeyId}")
    private String accessKeyId;

    @Value("${sms.accessSecret}")
    private String accessSecret;

    @Value("${sms.signName}")
    private String signName;

    @Value("${sms.templateCode}")
    private String templateCode;

    @Bean
    AliyunSmsEntity AliyunSmsEntity() {
        AliyunSmsEntity sms = new AliyunSmsEntity();
        sms.setAccessKeyId(accessKeyId);
        sms.setAccessSecret(accessSecret);
        sms.setSignName(signName);
        sms.setTemplateCode(templateCode);

        return sms;
    }
}
