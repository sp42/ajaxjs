package com.ajaxjs.cms.service.wechat.applet.payment.profit_sharing;

import lombok.Data;

/**
 * 分账回调结果
 */
@Data
public class ResultNotify {
    /**
     * 服务商模式分账发起商户。
     */
    private String sp_mchid;

    /**
     * 服务商模式分账出资商户。
     */
    private String sub_mchid;

    /**
     * 微信支付订单号
     * 示例值：4208450740201411110007820472
     */
    private String transaction_id;

    /**
     * 商户系统内部的分账单号，在商户系统内部唯一，同一分账单号多次请求等同一次。只能是数字、大小写字母_-|*@
     * 示例值：P20150806125346
     */
    private String out_order_no;

    /**
     * 微信分账单号，微信支付系统返回的唯一标识
     * 示例值：3008450740201411110007820472
     */
    private String order_id;


    private RequestOrderReceivers receiver;

    private String success_time;
}
