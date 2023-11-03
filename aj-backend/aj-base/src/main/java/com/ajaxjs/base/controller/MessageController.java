package com.ajaxjs.base.controller;

import com.ajaxjs.base.model.MailVo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 消息发送：SMTP 邮件发送/SMS 短信/微信
 */
@RestController
@RequestMapping("/msg")
public interface MessageController {
    /**
     * 邮件发送
     *
     * @return 是否成功
     */
    @PostMapping("/email")
    boolean email(MailVo mail);
}
