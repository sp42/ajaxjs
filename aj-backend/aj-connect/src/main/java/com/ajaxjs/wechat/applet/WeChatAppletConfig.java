package com.ajaxjs.wechat.applet;

import lombok.Data;

/**
 * 微信小程序服务端配置
 *
 * @author Frank Cheung
 */
@Data
public class WeChatAppletConfig {
    /**
     * App ID
     */
    private String accessKeyId;

    /**
     * App 密钥
     */
    private String accessSecret;

    /**
     * 访问令牌
     */
    private String accessToken;
}
