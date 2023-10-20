package com.ajaxjs.wechat.applet.payment.profit_sharing;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 添加分账接收方
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ReceiverAddResult extends ReceiverResult {
    /**
     * 分账个人接收方姓名
     * <p>
     * 分账接收方类型是MERCHANT_ID时，是商户全称（必传），当商户是小微商户或个体户时，是开户人姓名 分账接收方类型是 PERSONAL_OPENID 时，是个人姓名（选传，传则校验）
     * 1、此字段需要加密，加密方法详见：敏感信息加密说明
     * 2、使用微信支付平台证书中的公钥：获取平台证书
     * 3、使用 RSAES-OAEP 算法进行加密
     * 4、将请求中HTTP头部的 Wechatpay-Serial 设置为证书序列号
     * 示例值：hu89ohu89ohu89o
     */
    private String name;

    /**
     * 与分账方的关系类型
     * <p>
     * 商户与接收方的关系。本字段值为枚举：
     * STORE：门店
     * STAFF：员工
     * STORE_OWNER：店主
     * PARTNER：合作伙伴
     * HEADQUARTER：总部
     * BRAND：品牌方
     * DISTRIBUTOR：分销商
     * USER：用户
     * SUPPLIER： 供应商
     * CUSTOM：自定义
     * 示例值：STORE
     */
    private String relation_type;

    /**
     * 自定义的分账关系
     * <p>
     * 子商户与接收方具体的关系，本字段最多10个字。
     * 当字段relation_type的值为CUSTOM时，本字段必填;
     * 当字段relation_type的值不为CUSTOM时，本字段无需填写。
     * 示例值：代理商
     */
    private String custom_relation;
}
