package com.ajaxjs.iam.user.model;

import com.ajaxjs.framework.BaseModel;
import com.ajaxjs.framework.IBaseModel;
import com.ajaxjs.iam.user.common.UserConstants;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
public class User extends BaseModel implements UserConstants, IBaseModel {
    /**
     * 部门 ID
     */
    private Long orgId;

    /**
     * 租户 id
     */
    private Long tenantId;

    /**
     * 登录名、用户登录 id，不可重复
     */
    @NotNull
    private String loginId;

    /**
     * 用户真实姓名
     */
    private String realName;

    /**
     * 数据字典：性别
     */
    private Gender gender;

    /**
     * 出生日期
     */
    private Date birthday;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 邮件
     */
    @Email
    private String email;

    /**
     * 手机
     */
    private String phone;

    /**
     * 身份证号码
     */
    private String idCardNo;

    private String jobTitle;

    private String address;

    private Long locationProvince;

    private Long locationCity;

    private Long locationDistrict;

}
