package com.ajaxjs.wechat.applet.payment.payment;

import lombok.Data;

/**
 * 退款结果-金额
 */
@Data
public class RefundResultAmount {
    /**
     * 订单总金额，单位为分，只能为整数
     */
    private Integer total;

    /**
     * 退款金额，币种的最小单位，只能为整数，不能超过原订单支付金额，如果有使用券，后台会按比例退。
     */
    private Integer refund;

    /**
     * 出资账户类型
     * 下面枚举值多选一。
     * 枚举值：
     * AVAILABLE : 可用余额
     * UNAVAILABLE : 不可用余额
     */
    private String formAccount;

    /**
     * 出资账户对应账户出资金额
     */
    private Integer formAmount;


    /**
     * 用户实际支付金额，单位为分，只能为整数
     */
    private Integer payer_total;

    /**
     * 退款给用户的金额，不包含所有优惠券金额
     */
    private Integer payer_refund;

    /**
     * 应结退款金额
     * 去掉非充值代金券退款金额后的退款金额，单位为分，退款金额=申请退款金额-非充值代金券退款金额，退款金额<=申请退款金额
     */
    private Integer settlement_refund;

    /**
     * 应结订单金额=订单金额-免充值代金券金额，应结订单金额<=订单金额，单位为分
     */
    private Integer settlement_total;

    /**
     * 优惠退款金额<=退款金额，退款金额-代金券或立减优惠退款金额为现金，说明详见代金券或立减优惠，单位为分
     */
    private Integer discount_refund;

    /**
     * 退款币种 符合ISO 4217标准的三位字母代码，目前只支持人民币：CNY。
     */
    private String currency;

    /**
     * 手续费退款金额
     */
    private Integer refund_fee;
}
