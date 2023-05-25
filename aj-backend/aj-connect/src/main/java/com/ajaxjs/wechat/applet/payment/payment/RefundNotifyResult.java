package com.ajaxjs.wechat.applet.payment.payment;


import lombok.Data;

/**
 * 退款回调结果
 */
@Data
public class RefundNotifyResult {
    /**
     * 直连商户号
     */
    private String mchid;

    /**
     * 商户订单号
     */
    private String out_trade_no;

    /**
     * 微信支付订单号
     */
    private String transaction_id;

    /**
     * 商户退款单号
     */
    private String out_refund_no;

    /**
     * 微信支付退款单号
     */
    private String refund_id;

    /**
     * 退款状态，枚举值：
     * SUCCESS：退款成功
     * CLOSED：退款关闭
     * ABNORMAL：退款异常，退款到银行发现用户的卡作废或者冻结了，导致原路退款银行卡失败，可前往【商户平台—>交易中心】，手动处理此笔退款
     * 示例值：SUCCESS
     */
    private String refund_status;

    /**
     * 1、退款成功时间，遵循rfc3339标准格式，格式为yyyy-MM-DDTHH:mm:ss+TIMEZONE，yyyy-MM-DD表示年月日，T出现在字符串中，表示time元素的开头，HH:mm:ss表示时分秒，TIMEZONE表示时区（+08:00表示东八区时间，领先UTC 8小时，即北京时间）。例如：2015-05-20T13:29:35+08:00表示，北京时间2015年5月20日13点29分35秒。
     * 2、当退款状态为退款成功时返回此参数。
     * 示例值：2018-06-08T10:34:56+08:00
     */
    private String success_time;

    /**
     * 取当前退款单的退款入账方。
     * 1、退回银行卡：{银行名称}{卡类型}{卡尾号}
     * 2、退回支付用户零钱: 支付用户零钱
     * 3、退还商户: 商户基本账户、商户结算银行账户
     * 4、退回支付用户零钱通：支付用户零钱通
     * 示例值：招商银行信用卡0403
     */
    private String user_received_account;

    /**
     * 订单总金额，单位为分，只能为整数
     */
    private Integer total;

    /**
     * 退款金额，币种的最小单位，只能为整数，不能超过原订单支付金额，如果有使用券，后台会按比例退。
     */
    private Integer refund;

    /**
     * 用户实际支付金额，单位为分，只能为整数
     */
    private Integer payer_total;

    /**
     * 退款给用户的金额，不包含所有优惠券金额
     */
    private Integer payer_refund;
}
