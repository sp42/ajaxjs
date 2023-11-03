package com.ajaxjs.base.config;

import org.springframework.context.annotation.Configuration;

@Configuration
public class WechatConfiguration {

//    @Value("${wechat.user.appId}")
//    private String wechatAppId;
//    @Value("${wechat.user.secret}")
//    private String wechatAppSecret;

//    @Bean
//    WeChatAppletConfig getWeChatAppletConfig() {
//        WeChatAppletConfig w = new WeChatAppletConfig();
//        w.setAccessKeyId(wechatAppId);
//        w.setAccessSecret(wechatAppSecret);
//
//        GetToken getToken = new GetToken();
//        getToken.appletCfg = w;
//        getToken.init();
//
//        return w;
//    }

//    @Value("${wechat.merchant.mchId}")
//    private String wechatMerchantId;
//
//    @Value("${wechat.merchant.privateKeyFile}")
//    private String wechatMerchantPrivateKeyFile;
//
//    @Value("${wechat.merchant.apiV3Key}")
//    private String wechatMerchantApiV3Key;
//    @Value("${wechat.merchant.mchSerialNo}")
//    private String wechatMerchantSerialNo;
//
//    @Bean
//    MerchantConfig getMerchantConfig() {
//        MerchantConfig m = new MerchantConfig();
//        m.setMchId(wechatMerchantId);
//        m.setApiV3Key(wechatMerchantApiV3Key);
//        m.setPrivateKey(wechatMerchantPrivateKeyFile);
//        m.setMchSerialNo(wechatMerchantSerialNo);
//
//        return m;
//    }
}
