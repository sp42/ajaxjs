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
package com.ajaxjs.user.third.weibo;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.ajaxjs.net.http.Get;
import com.ajaxjs.net.http.Post;

/**
 * 微博第三方登录
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
public class Weibo {
	@Autowired
	private WeiboConfig cfg;

	/**
	 * 获取 AccessToken
	 * 
	 * @param code 授权码
	 * @return
	 */
	public Map<String, Object> getAccessToken(String code) {
//		LOGGER.info("获取 AccessToken ：" + code);

		StringBuilder sb = new StringBuilder();
		sb.append("grant_type=authorization_code");
		sb.append("&client_id=" + cfg.getAccessKeyId());
		sb.append("&client_secret=" + cfg.getAccessSecret());
		sb.append("&redirect_uri=" + cfg.getLoginUrl());
		sb.append("&code=" + code);

		Map<String, Object> result = Post.api("https://api.weibo.com/oauth2/access_token", sb.toString());

		/**
		 * 返回数据 { "access_token": "ACCESS_TOKEN", "expires_in": 1234,
		 * "remind_in":"798114", "uid":"12341234" }
		 */
		return result;
	}

	/**
	 * 根据用户 ID 获取用户信息 文档：https://open.weibo.com/wiki/2/users/show
	 * 
	 * @param accessToken
	 * @param uid
	 * @return
	 */
	public Map<String, Object> getUserInfo(String accessToken, String uid) {
		StringBuilder sb = new StringBuilder();
		sb.append("access_token=" + accessToken);
		sb.append("&uid=" + uid);

		Map<String, Object> result = Get.api("https://api.weibo.com/2/users/show.json?" + sb.toString());

		return result;
	}
}
