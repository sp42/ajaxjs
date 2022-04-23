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
package com.ajaxjs.wechat.open_account;

import com.ajaxjs.net.http.Get;
import com.ajaxjs.util.logger.LogHelper;

public class OpenAccount {
	private static final LogHelper LOGGER = LogHelper.getLog(OpenAccount.class);
	/**
	 * App Id
	 */
	private String accessKeyId;

	/**
	 * App 密钥
	 */
	private String accessSecret;

	private final static String ACCESS_TOKEN_API = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s";

	public void getClientAccessToken() {
		LOGGER.info("获取 ClientApp AT");
		Get.api(String.format(ACCESS_TOKEN_API, accessKeyId, accessSecret));
	}
}
