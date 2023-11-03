package com.ajaxjs.base.config;

import com.ajaxjs.base.service.message.ali_sms.AliyunSmsEntity;
import com.ajaxjs.base.service.message.email.Mail;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

/**
 * 消息配置
 */
@Configuration
public class MessageConfiguration {
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

    @Value("${Message.email.smtpServer}")
    private String smtpServer;

    @Value("${Message.email.port}")
    private int port;

    @Value("${Message.email.account}")
    private String account;

    @Value("${Message.email.password}")
    private String password;

    @Bean
    @Scope("prototype")
    Mail getMailConfig() {
        Mail mailCfg = new Mail();
        mailCfg.setMailServer(smtpServer);
        mailCfg.setPort(port);
        mailCfg.setAccount(account);
        mailCfg.setPassword(password);

        return mailCfg;
    }

}
