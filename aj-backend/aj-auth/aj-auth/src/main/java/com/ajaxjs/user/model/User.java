package com.ajaxjs.user.model;

import com.ajaxjs.framework.BaseModel;
import com.ajaxjs.framework.IBaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
public class User extends BaseModel implements UserConstants, IBaseModel {
    /**
     * 用户名、登录名
     */
    private String username;

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
