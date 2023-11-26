package com.ajaxjs.cms.service.wechat.applet.model;

import lombok.Data;

/**
 * 小程序执行 wx.getUserInfo() 返回的用户信息
 *
 * @author sp42 frank@ajaxjs.com
 */
@Data
public class UserInfo {
    private String nickName;

    private String avatarUrl;

    private int gender;

    private String province;

    private String city;

    private String country;

    /**
     * 转为系统的用户
     *
     * @return 标准用户类型
     */
//    public User toSystemUser() {
//        User user = new User();
//        user.setUsername(nickName);
//        user.setGender(gender);
//        user.setAvatar(avatarUrl);
//        user.setLocation(country + " " + province + " " + city);
//
//        return user;
//    }
}
