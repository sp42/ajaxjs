package com.github.chaijunkun.wechat.common;

import lombok.Data;

/**
 * 微信API接入配置
 */
@Data
public class WeChatAPIConfig {
    /**
     * appId
     */
    private String appId;

    /**
     * 密钥
     */
    private String secret;

    /**
     * 回调时使用的token
     */
    private String callbackToken;

    /**
     * 消息加密密钥
     */
    private String encodingAESKey;
}
