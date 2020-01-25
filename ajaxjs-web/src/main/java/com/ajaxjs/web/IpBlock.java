/**
 * Copyright Sp42 frank@ajaxjs.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ajaxjs.web;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.ajaxjs.mvc.controller.MvcRequest;
import com.ajaxjs.net.http.Tools;
import com.ajaxjs.util.cache.LRUCache;

/**
 * IP 拦截
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
public class IpBlock {
	static LRUCache<String, Boolean> cache = new LRUCache<>(20);

	public static boolean isChinaMainlandIp(String ip) {
		try {
			Map<String, Object> map = Tools.getIpLocation(ip);
			Object c = map.get("country");

			if (c != null && "中国".equals(c.toString())) {
				Object r = map.get("region");

				if (r != null
						&& ("香港".equals(r.toString()) || "澳门".equals(r.toString()) || "台湾".equals(r.toString()))) {
					return false;
				}

				return true;
			} else {
				return false;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return true;
	}

	public static boolean isChinaMainlandIp_Cache(String ip) {
		Boolean isChinaMainlandIp = cache.get(ip);

		if (isChinaMainlandIp == null) {
			isChinaMainlandIp = isChinaMainlandIp(ip);
			cache.put(ip, isChinaMainlandIp);
		}

		return isChinaMainlandIp;
	}

	public static boolean isChinaMainlandIp_Cache(HttpServletRequest r) {
		String ip;
		if (r instanceof MvcRequest) {
			ip = ((MvcRequest) r).getIp();
		} else {
			ip = new MvcRequest(r).getIp();
		}

		return isChinaMainlandIp_Cache(ip);
	}

}
