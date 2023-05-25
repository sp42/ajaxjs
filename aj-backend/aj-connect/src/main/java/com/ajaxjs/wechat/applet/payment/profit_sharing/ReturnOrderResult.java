package com.ajaxjs.wechat.applet.payment.profit_sharing;

import com.toway.newfleet.business.wx.applet_pay.model.ResponseMessage;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 分账回退结果
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ReturnOrderResult extends ResponseMessage {
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
     * 微信回退单号
     * <p>
     * 微信分账回退单号，微信支付系统返回的唯一标识
     * 示例值：3008450740201411110007820472
     */
    private String return_id;

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

    /**
     * 回退结果
     * 如果请求返回为处理中，则商户可以通过调用回退结果查询接口获取请求的最终处理结果。
     * 如果查询到回退结果在处理中，请勿变更商户回退单号，使用相同的参数再次发起分账回退，否则会出现资金风险。
     * 在处理中状态的回退单如果5天没有成功，会因为超时被设置为已失败。
     * 枚举值：
     * PROCESSING：处理中
     * SUCCESS：已成功
     * FAILED：已失败
     * 示例值：SUCCESS
     */
    private String result;

    /**
     * 分账回退失败原因
     * 包含以下枚举值：
     * ACCOUNT_ABNORMAL：原分账接收方账户异常
     * TIME_OUT_CLOSED：超时关单
     * PAYER_ACCOUNT_ABNORMAL：原分账分出方账户异常
     * INVALID_REQUEST: 描述参数设置失败
     * 示例值：TIME_OUT_CLOSED
     */
    private String fail_reason;

    /**
     * 分账回退创建时间，遵循rfc3339标准格式，格式为yyyy-MM-DDTHH:mm:ss+TIMEZONE，yyyy-MM-DD表示年月日，T出现在字符串中，表示time元素的开头，HH:mm:ss表示时分秒，TIMEZONE表示时区（+08:00表示东八区时间，领先UTC 8小时，即北京时间）。例如：2015-05-20T13:29:35+08:00表示，北京时间2015年5月20日 13点29分35秒。
     * 示例值：2015-05-20T13:29:35+08:00
     */
    private String create_time;

    /**
     * 分账回退完成时间，遵循rfc3339标准格式，格式为yyyy-MM-DDTHH:mm:ss+TIMEZONE，yyyy-MM-DD表示年月日，T出现在字符串中，表示time元素的开头，HH:mm:ss表示时分秒，TIMEZONE表示时区（+08:00表示东八区时间，领先UTC 8小时，即北京时间）。例如：2015-05-20T13:29:35+08:00表示，北京时间2015年5月20日 13点29分35秒。
     * 示例值：2015-05-20T13:29:35+08:00
     */
    private String finish_time;
}
