package com.ajaxjs.wechat.merchant;

import lombok.Data;

/**
 * 微信支付 商户配置
 */
@Data
public class MerchantConfig {
    /**
     * 商户号
     */
    private String mchId;

    /**
     * 商户证书序列号
     */
    private String mchSerialNo;

    /**
     * 商户私钥
     */
    private String privateKey;

    /**
     * V3 密钥（微信支付商户平台提供）
     */
    private String apiV3Key;

}
