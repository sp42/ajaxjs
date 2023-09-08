package com.ajaxjs.login.weibo;

import lombok.Data;

/**
 * 从微博接口可以获取有用的用户信息
 *
 * @author sp42 frank@ajaxjs.com
 */
@Data
public class WeiboUserInfo {
    /**
     *
     */
    private String accessToken;

    /**
     *
     */
    private String name;

    /**
     *
     */
    private String uid;

    /**
     *
     */
    private String gender;

    /**
     *
     */
    private String avatar;
}
