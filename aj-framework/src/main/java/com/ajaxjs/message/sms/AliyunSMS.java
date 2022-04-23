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
package com.ajaxjs.message.sms;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.SimpleTimeZone;
import java.util.TreeMap;

import org.springframework.util.AlternativeJdkIdGenerator;

import com.ajaxjs.net.http.Get;
import com.ajaxjs.util.StrUtil;
import com.ajaxjs.util.cryptography.Digest;

/**
 * 阿里云发送短信
 */
public class AliyunSMS {

	/**
	 * @param entity
	 * @return true 表示发送成功
	 */
	public static String send(AliyunSmsEntity entity) {
		// 1. 初始化请求参数
		Map<String, String> paras = new HashMap<>();
		paras.put("SignatureMethod", "HMAC-SHA1");
		paras.put("SignatureNonce", new AlternativeJdkIdGenerator().generateId().toString());
		paras.put("SignatureVersion", "1.0");
		paras.put("Timestamp", getTimestamp());
		paras.put("Format", "JSON");
		paras.put("Action", "SendSms");
		paras.put("Version", "2017-05-25");
		paras.put("AccessKeyId", entity.getAccessKeyId()); // 2. 业务 API 参数
		paras.put("PhoneNumbers", entity.getPhoneNumbers());
		paras.put("SignName", entity.getSignName());
		paras.put("TemplateParam", entity.getTemplateParam());
		paras.put("TemplateCode", entity.getTemplateCode());

		// 3. 去除签名关键字 Key
		if (paras.containsKey("Signature"))
			paras.remove("Signature");

		String sortQueryStringTmp = sort(paras);
		String signature = makeSignature(sortQueryStringTmp, entity.getAccessSecret());

		// 最终打印出合法 GET 请求的 URL
		Map<String, Object> map = Get.api(SMS_API + signature + sortQueryStringTmp);

		return "OK".equals(map.get("Code")) ? "OK" : map.get("Message").toString();
	}

	/**
	 * 请求的时间戳。按照 ISO8601 标准表示，并需要使用 UTC 时间，格式为 yyyy-MM-ddTHH:mm:ssZ。
	 */
	static String getTimestamp() {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		df.setTimeZone(new SimpleTimeZone(0, "GMT"));// 这里一定要设置 GMT 时区

		return df.format(new Date());
	}

	/**
	 * 根据参数Key排序（顺序）
	 *
	 * @param paras
	 * @return
	 */
	private static String sort(Map<String, String> paras) {
		// 4. 参数 KEY 排序
		TreeMap<String, String> sortParas = new TreeMap<>();
		sortParas.putAll(paras);

		// 5. 构造待签名的字符串
		Iterator<String> it = sortParas.keySet().iterator();
		StringBuilder sb = new StringBuilder();

		while (it.hasNext()) {
			String key = it.next();
			sb.append("&").append(StrUtil.urlEncodeQuery(key)).append("=").append(StrUtil.urlEncodeQuery(paras.get(key)));
		}

		return sb.toString();
	}

	/**
	 * 构造待签名的请求串
	 *
	 * @param sortQueryStringTmp 测试
	 * @param accessSecret       测试
	 * @return
	 */
	private static String makeSignature(String sortQueryStringTmp, String accessSecret) {
		StringBuilder stringToSign = new StringBuilder();
		stringToSign.append("GET").append("&");
		stringToSign.append(StrUtil.urlEncodeQuery("/")).append("&");
		stringToSign.append(StrUtil.urlEncodeQuery(sortQueryStringTmp.substring(1)));// 去除第一个多余的&符号

		String sign = Digest.doHmacSHA1(accessSecret + "&", stringToSign.toString());

		// 6. 签名最后也要做特殊 URL 编码
		return StrUtil.urlEncodeQuery(sign);
	}

	private final static String SMS_API = "http://dysmsapi.aliyuncs.com/?Signature=";
}