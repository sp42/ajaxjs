package com.ajaxjs.base;

import lombok.Data;
import org.springframework.web.client.RestTemplate;

/**
 * Base 服务的 SDK
 */
@Data
public class Sdk {
    private RestTemplate restTemplate;

    /**
     * Base API 地址前缀
     */
    private String endPoint;

    /**
     * 发送邮件
     *
     * @param mail 邮件
     */
    public void sendEmail(MailVo mail) {

    }
}
