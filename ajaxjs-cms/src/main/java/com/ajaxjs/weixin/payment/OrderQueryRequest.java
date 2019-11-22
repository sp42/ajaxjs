package com.ajaxjs.weixin.payment;

/**
 * @borball on 5/15/2016.
 */
public class OrderQueryRequest {

    private String transactionId;

    private String tradeNumber;

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getTradeNumber() {
        return tradeNumber;
    }

    public void setTradeNumber(String tradeNumber) {
        this.tradeNumber = tradeNumber;
    }
}
