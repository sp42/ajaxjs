package com.ajaxjs.payment.alipay.model;

import lombok.Data;

@Data
public class AlipayConfig {
    private String partnerId;

    private String md5key;

    private String sellerAccount;

    private String myPrivateKey;

    /**
     *
     */
    private String alipayPublicKey;
}
