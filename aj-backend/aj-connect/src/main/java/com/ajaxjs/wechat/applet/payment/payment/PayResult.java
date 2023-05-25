package com.ajaxjs.wechat.applet.payment.payment;

import lombok.Data;

/**
 * 由微信支付通知过来的支付结果
 *
 * @author Frank Cheung
 */
@Data
public class PayResult {
    /**
     * 商户订单号
     */
    private String out_trade_no;

    /**
     * 微信支付订单号
     */
    private String transaction_id;

    /**
     * 交易类型
     * <p>
     * JSAPI：公众号支付 NATIVE：扫码支付 APP：APP支付 MICROPAY：付款码支付 MWEB：H5支付 FACEPAY：刷脸支付
     */
    private String trade_type;

    /**
     * 交易状态
     * <p>
     * SUCCESS：支付成功 REFUND：转入退款 NOTPAY：未支付 CLOSED：已关闭 REVOKED：已撤销（付款码支付）
     * USERPAYING：用户支付中（付款码支付） PAYERROR：支付失败(其他原因，如银行返回失败)
     */
    private String trade_state;

    /**
     * 交易状态描述
     */
    private String trade_state_desc;

    /**
     * 付款银行
     */
    private String bank_type;

    /**
     * 附加数据
     */
    private String attach;

    /**
     * 支付完成时间
     */
    private String success_time;

    /**
     * 支付完成时间
     */
    private String payerOpenId;

    /**
     * 总金额
     */
    private Integer total;

    /**
     * 用户支付金额
     */
    private Integer payer_total;

}
