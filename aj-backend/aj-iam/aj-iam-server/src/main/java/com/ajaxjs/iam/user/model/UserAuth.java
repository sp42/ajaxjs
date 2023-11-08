package com.ajaxjs.iam.user.model;

import com.ajaxjs.framework.BaseModel;
import com.ajaxjs.framework.IBaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 对应数据库口令表，普通密码登录
 *
 * @author sp42 frank@ajaxjs.com
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class UserAuth extends BaseModel implements IBaseModel {
    /**
     * 用户id
     */
    private Long userId;

    /**
     * 数据字典：登录类型
     */
    private Integer loginType;

    /**
     * 手机号码是否已验证
     */
    private Boolean phoneVerified;

    /**
     * 身份证是否已验证（是否实名认证）
     */
    private Boolean idCardNoVerified;

    /**
     * 邮箱是否已验证
     */
    private Boolean email_verified;

    /**
     * 注册ip
     */
    private String registerIp;

    /**
     * 第三方id
     */
    private String identifier;

    /**
     * 密码凭证（站内的保存密码，站外的不保存或保存token）
     */
    private String credential;

    /**
     * Token 过期时间
     */
    private String oauthExpires;

    /**
     * 数据字典：注册时的登录类型
     */
    private Integer registerType;

    /**
     * 修改密码时间
     */
    private Date updatePasswordDate;

    /**
     * 唯一 id
     */
    private Long uid;
}
