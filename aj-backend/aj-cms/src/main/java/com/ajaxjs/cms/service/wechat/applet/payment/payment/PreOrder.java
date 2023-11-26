package com.ajaxjs.cms.service.wechat.applet.payment.payment;

import lombok.Data;

/**
 * JSAPI下单 请求对象
 * <p>
 * <a href="https://pay.weixin.qq.com/wiki/doc/apiv3/apis/chapter3_5_1.shtml">...</a>
 *
 * @author Frank Cheung
 */
@Data
public class PreOrder {
    /**
     * 应用 ID
     */
    private String appid;

    /**
     * 直连商户号
     */
    private String mchid;

    /**
     * 商品描述
     */
    private String description;

    /**
     * 商户订单号
     */
    private String out_trade_no;

    /**
     * 通知地址
     */
    private String notify_url;
}
