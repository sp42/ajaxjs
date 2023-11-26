package com.ajaxjs.cms.service.wechat.applet.payment.profit_sharing;

import com.ajaxjs.cms.service.wechat.applet.model.ResponseMessage;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 请求分账结果
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class RequestOrderResult extends ResponseMessage {
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

    /**
     * 分账单状态（每个接收方的分账结果请查看receivers中的result字段），枚举值：
     * 1、PROCESSING：处理中
     * 2、FINISHED：分账完成
     * 示例值：FINISHED
     */
    private String state;

    /**
     * 分账接收方列表
     */
    private List<RequestOrderReceivers> receivers;

//    /**
//     * 自定义的字段，不是微信返回的，只是方便得知来源
//     */
//    private RequestOrder order;
}
