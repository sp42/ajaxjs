/**
 * Copyright Sp42 frank@ajaxjs.com Licensed under the Apache License, Version
 * 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law
 * or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package com.ajaxjs.login.weibo;

import com.ajaxjs.net.http.Get;
import com.ajaxjs.net.http.Post;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 * 微博第三方登录
 *
 * @author sp42 frank@ajaxjs.com
 */
public class Weibo {
    @Autowired
    private WeiboConfig cfg;

    /**
     * 获取 AccessToken
     *
     * @param code 授权码
     * @return AccessToken
     */
    public Map<String, Object> getAccessToken(String code) {
        String sb = "grant_type=authorization_code" +
                "&client_id=" + cfg.getAccessKeyId() +
                "&client_secret=" + cfg.getAccessSecret() +
                "&redirect_uri=" + cfg.getLoginUrl() +
                "&code=" + code;

        /*
         * 返回数据 { "access_token": "ACCESS_TOKEN", "expires_in": 1234,
         * "remind_in":"798114", "uid":"12341234" }
         */
        return Post.api("https://api.weibo.com/oauth2/access_token", sb);
    }

    /**
     * 根据用户 ID 获取用户信息 文档：<a href="https://open.weibo.com/wiki/2/users/show">...</a>
     */
    public Map<String, Object> getUserInfo(String accessToken, String uid) {
        String sb = "access_token=" + accessToken + "&uid=" + uid;

        return Get.api("https://api.weibo.com/2/users/show.json?" + sb);
    }
}
