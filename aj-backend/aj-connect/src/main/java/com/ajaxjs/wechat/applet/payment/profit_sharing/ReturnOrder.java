package com.ajaxjs.wechat.applet.payment.profit_sharing;

import lombok.Data;

@Data
public class ReturnOrder {
    /**
     * 微信分账单号
     * <p>
     * 原发起分账请求时，微信返回的微信分账单号，与商户分账单号一一对应。
     * 示例值：3008450740201411110007820472
     */
    private String order_id;

    /**
     * 商户分账单号
     * <p>
     * 原发起分账请求时使用的商户系统内部的分账单号
     * 示例值：P20150806125346
     */
    private String out_order_no;

    /**
     * 商户回退单号
     * <p>
     * 调用回退接口提供的商户系统内部的回退单号
     * 示例值：R20190516001
     */
    private String out_return_no;

    /**
     * 回退商户号
     * <p>
     * 只能对原分账请求中成功分给商户接收方进行回退
     * 示例值：86693852
     */
    private String return_mchid;

    /**
     * 回退金额
     * <p>
     * 需要从分账接收方回退的金额，单位为分，只能为整数
     * 示例值：10
     */
    private Integer amount;

    /**
     * 回退描述
     * <p>
     * 分账回退的原因描述
     * 示例值：用户退款
     */
    private String description;
}
