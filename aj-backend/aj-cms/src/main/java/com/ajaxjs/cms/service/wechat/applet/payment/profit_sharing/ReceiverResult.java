package com.ajaxjs.cms.service.wechat.applet.payment.profit_sharing;

import com.ajaxjs.cms.service.wechat.applet.model.ResponseMessage;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * 分账接收方信息
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ReceiverResult extends ResponseMessage {
    /**
     * 分账接收方类型
     * <p>
     * 枚举值：
     * MERCHANT_ID：商户号
     * PERSONAL_OPENID：个人openid（由父商户APPID转换得到）
     * 示例值：MERCHANT_ID
     */
    private String type;

    /**
     * 分账接收方账号
     * <p>
     * 类型是MERCHANT_ID时，是商户号
     * 类型是PERSONAL_OPENID时，是个人openid
     * openid获取方法
     * 示例值：1900000109
     */
    private String account;
}
