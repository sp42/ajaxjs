package com.ajaxjs.cms.service.wechat.applet.model;

import lombok.Data;

/**
 * 登录状态
 *
 * @author Frank Cheung
 */
@Data
public class LoginSession {
    /**
     * 用户唯一标识
     */
    private String openId;

    /**
     * 会话密钥
     */
    private String sessionKey;

    /**
     * 用户的系统 id
     */
    private Long userId;

    /**
     * 自定义的会话 id，返回给小程序
     */
    private String sessionId;
}
