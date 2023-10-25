package com.ajaxjs.wechat.applet.payment.payment;

import lombok.Data;

/**
 * 小程序调起支付 wx.requestPayment() 所需的参数
 */
@Data
public class RequestPayment {
    /**
     * 时间戳
     */
    private String timeStamp;

    /**
     * 随机字符串
     */
    private String nonceStr;

    /**
     * 订单详情扩展字符串
     */
    private String prepayIdPackage;

    /**
     * 随机字符串
     */
    private String signType = "RSA";

    /**
     * 签名
     */
    private String paySign;
}
