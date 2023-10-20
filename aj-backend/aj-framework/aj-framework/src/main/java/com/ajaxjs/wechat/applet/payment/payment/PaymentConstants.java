package com.ajaxjs.wechat.applet.payment.payment;

public interface PaymentConstants {
    /**
     * 退款渠道
     */
    public static enum RefundChannel {
        /**
         * 原路退款
         */
        ORIGINAL,

        /**
         * 退回到余额
         */
        BALANCE,

        /**
         * 原账户异常退到其他余额账户
         */
        OTHER_BALANCE,

        /**
         * 原银行卡异常退到其他银行卡
         */
        OTHER_BANKCARD
    }

    /**
     * 退款状态
     */
    public static enum RefundStatus {
        /**
         * 退款成功
         */
        SUCCESS,

        /**
         * 退款关闭
         */
        CLOSED,

        /**
         * 退款处理中
         */
        PROCESSING,

        /**
         * 退款异常
         */
        ABNORMAL
    }

    /**
     * 退款状态
     */
    public static enum FundsAccount {
        /**
         * 未结算资金
         */
        UNSETTLED,

        /**
         * 可用余额
         */
        AVAILABLE,

        /**
         * 不可用余额
         */
        UNAVAILABLE,

        /**
         * 运营户
         */
        OPERATION,

        /**
         * 基本账户（含可用余额和不可用余额）
         */
        BASIC;
    }
}
