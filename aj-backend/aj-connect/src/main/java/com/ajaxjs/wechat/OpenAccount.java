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
package com.ajaxjs.wechat;

import com.ajaxjs.framework.ClientAccessFullInfo;
import com.ajaxjs.net.http.Get;
import com.ajaxjs.util.StrUtil;
import com.ajaxjs.util.cryptography.Digest;
import com.ajaxjs.util.logger.LogHelper;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 微信公众号
 */
public class OpenAccount extends ClientAccessFullInfo {
    private static final LogHelper LOGGER = LogHelper.getLog(OpenAccount.class);

    private final static String ACCESS_TOKEN_API = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s";

    public void getClientAccessToken() {
        LOGGER.info("获取 ClientApp AT");
        Get.api(String.format(ACCESS_TOKEN_API, getAccessKeyId(), getAccessSecret()));
    }

    public static boolean init(HttpServletRequest req) {
        String ua = req.getHeader("User-Agent");
        if (ua != null)
            ua = ua.toLowerCase();// 强制转换为小写字母

        if (ua.contains("micromessenger")) {
//			try {
//				if (TokenMgr.instance == null) {
//					String appId = ConfigService.getValueAsString("wx_open.appId");
//					String appSecret = ConfigService.getValueAsString("wx_open.appSecret");
//					TokenMgr.instance = new TokenMgr(appId, appSecret);
//				}
//
//				// 获取当前页面的 url
//				String url = (req.getRemotePort() != 80 ? "https" : req.getScheme()) + "://" + req.getServerName() + req.getRequestURI();
//				if (req.getQueryString() != null)
//					url += "?" + req.getQueryString();
//
//				Map<String, String> map = generateSignature(url, TokenMgr.instance.getTicket());
//				req.setAttribute("map", map);
//			} catch (Exception e) {
//				e.printStackTrace();
//			}

            return true;
        } else
            return false;
    }

    /**
     * @param url         页面地址
     * @param jsApiTicket 凭证
     * @return 页面用的数据
     */
    static Map<String, String> generateSignature(String url, String jsApiTicket) {
        Map<String, String> map = new HashMap<>();
        map.put("url", url);
        map.put("jsapi_ticket", jsApiTicket);
        map.put("noncestr", StrUtil.getRandomString(10));
        map.put("timestamp", String.valueOf(System.currentTimeMillis() / 1000));
        map.put("signature", generateSignature(map));

        return map; // 因为签名用的 noncestr 和 timestamp 必须与 wx.config中的nonceStr 和 timestamp
        // 相同，所以还需要使用这两个参数
    }

    /**
     * 字段名的 ASCII 码从小到大排序（字典序）后，使用 URL 键值对的格式（即key1=value1&key2=value2…）拼接成字符串
     *
     * @param data Map
     * @return 签名
     */
    private static String generateSignature(Map<String, String> data) {
        Set<String> keySet = data.keySet();
        String[] keyArray = keySet.toArray(new String[0]);
        Arrays.sort(keyArray);
        StringBuilder sb = new StringBuilder();

        int i = 0;
        for (String k : keyArray) {
            String v = data.get(k);
            if (StringUtils.hasText(v)) // 参数值为空，则不参与签名
                sb.append(k).append("=").append(v.trim()).append(++i < data.size() ? "&" : "");
        }

        return Digest.getSHA1(sb.toString());
    }
}
