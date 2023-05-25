package com.ajaxjs.wechat.applet.payment.profit_sharing;

import com.ajaxjs.framework.IBaseModel;
import lombok.Data;

import java.util.List;

/**
 * 请求分账参数
 */
@Data
public class RequestOrder implements IBaseModel {
    /**
     * 微信分配的商户 appid
     */
    private String appid;

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
     * 分账接收方列表，可以设置出资商户作为分账接受方，最多可有50个分账接收方
     */
    private List<RequestOrderReceivers> receivers;

    /**
     * 1、如果为 true，该笔订单剩余未分账的金额会解冻回分账方商户；
     * 2、如果为 false，该笔订单剩余未分账的金额不会解冻回分账方商户，可以对该笔订单再次进行分账。
     * 示例值：true
     */
    private Boolean unfreeze_unsplit;
}
