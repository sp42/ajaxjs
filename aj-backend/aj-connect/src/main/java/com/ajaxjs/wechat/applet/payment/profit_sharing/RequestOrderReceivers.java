package com.ajaxjs.wechat.applet.payment.profit_sharing;

import com.ajaxjs.framework.IBaseModel;
import lombok.Data;

/**
 * 分账接收方
 */
@Data
public class RequestOrderReceivers implements IBaseModel {
    /**
     * 分账金额，单位为分，只能为整数，不能超过原订单支付金额及最大分账比例金额
     * 示例值：100
     */
    private Integer amount;

    /**
     * 分账的原因描述，分账账单中需要体现
     * 示例值：分给商户1900000110
     */
    private String description;

    /**
     * 接收方类型
     * 1、MERCHANT_ID：商户号
     * 2、PERSONAL_OPENID：个人 openid（由父商户 APPID 转换得到）
     * 示例值：MERCHANT_ID
     */
    private String type;

    /**
     * 接收方账号
     * 1、分账接收方类型为 MERCHANT_ID 时，分账接收方账号为商户号
     * 2、分账接收方类型为 PERSONAL_OPENID 时，分账接收方账号为个人 openid
     * 示例值：1900000109
     */
    private String account;

    /**
     * 分账结果
     * 枚举值：
     * 1、PENDING：待分账
     * 2、SUCCESS：分账成功
     * 3、CLOSED：已关闭
     * 示例值：SUCCESS
     */
    private String result;

    /**
     * 分账失败原因，当分账结果result为CLOSED（已关闭）时，返回该字段
     * 枚举值：
     * 1、ACCOUNT_ABNORMAL：分账接收账户异常
     * 2、NO_RELATION：分账关系已解除
     * 3、RECEIVER_HIGH_RISK：高风险接收方
     * 4、RECEIVER_REAL_NAME_NOT_VERIFIED：接收方未实名
     * 5、NO_AUTH：分账权限已解除
     * 6、RECEIVER_RECEIPT_LIMIT：接收方已达收款限额
     * 7、PAYER_ACCOUNT_ABNORMAL：分出方账户异常
     * 示例值：ACCOUNT_ABNORMAL
     */
    private String fail_reason;

    /**
     * 分账创建时间，遵循rfc3339标准格式，格式为yyyy-MM-DDTHH:mm:ss+TIMEZONE，yyyy-MM-DD表示年月日，T出现在字符串中，表示time元素的开头，HH:mm:ss表示时分秒，TIMEZONE表示时区（+08:00表示东八区时间，领先UTC 8小时，即北京时间）。例如：2015-05-20T13:29:35+08:00表示，北京时间2015年5月20日 13点29分35秒。
     * 示例值：2015-05-20T13:29:35+08:00
     */
    private String create_time;

    /**
     * 分账完成时间，遵循rfc3339标准格式，格式为yyyy-MM-DDTHH:mm:ss+TIMEZONE，yyyy-MM-DD表示年月日，T出现在字符串中，表示time元素的开头，HH:mm:ss表示时分秒，TIMEZONE表示时区（+08:00表示东八区时间，领先UTC 8小时，即北京时间）。例如：2015-05-20T13:29:35+08:00表示，北京时间2015年5月20日 13点29分35秒。
     * 示例值：2015-05-20T13:29:35+08:00
     */
    private String finish_time;

    /**
     * 微信分账明细单号，每笔分账业务执行的明细单号，可与资金账单对账使用
     * 示例值：36011111111111111111111
     */
    private String detail_id;

//    private String name;
}
