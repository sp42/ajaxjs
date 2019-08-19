package com.ajaxjs.web;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.ajaxjs.mvc.controller.MvcRequest;
import com.ajaxjs.net.http.NetUtil;
import com.ajaxjs.util.LRUCache;
import com.ajaxjs.util.map.JsonHelper;

public class IpBlock {
	static LRUCache<String, Boolean> cache = new LRUCache<>(20);

	public static boolean isChinaMainlandIp(String ip) {
		try {
			Map<String, Object> map = getIpLocation(ip);
			Object c = map.get("country");
			if (c != null && "中国".equals(c.toString())) {
				Object r = map.get("region");
				if (r != null && ("香港".equals(r.toString()) || "澳门".equals(r.toString()) || "台湾".equals(r.toString()))) {
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

	/**
	 * http://ip.taobao.com/instructions.html http://blog.zhukunqian.com/?p=1998
	 * http://pv.sohu.com/cityjson?ie=utf-8 https://gitee.com/meaktsui/ChinaIpSearch
	 * 
	 * @param ip
	 * @return
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Object> getIpLocation(String ip) throws IOException {
		String url = "http://ip.taobao.com/service/getIpInfo.php?ip=" + ip;
		System.out.println(url);
		String xml = NetUtil.simpleGET(url);
		Map<String, Object> map = JsonHelper.parseMap(xml);

		if (map != null && map.get("code") != null && (0 == (int) map.get("code"))) {
			Object obj = map.get("data");

			return (Map<String, Object>) obj;
		} else {
			throw new IOException("接口返回不成功 " + map);
		}

	}
}
