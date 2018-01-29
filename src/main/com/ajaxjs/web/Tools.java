package com.ajaxjs.web;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.ajaxjs.util.collection.MapHelper;

public class Tools {
	// 手机调试
	public static String remoteConsole(HttpServletRequest request) {

		String msg = "";
		Map<String, String> requestMap = new MapHelper().setParameterMapRaw(request.getParameterMap()).toMap()
				.ignoreField("url").ignoreField("action").getParameterMap_String();

		if (requestMap.size() > 0) {
			msg = com.ajaxjs.util.collection.MapHelper.join(requestMap);
		} else {
			msg = "没有参数";
		}

		System.out.println("手机调试参数：" + msg);
		// 可能是 jsonp
		return String.format("(function(){console.log('%s');})();", msg);
	}

	/**
	 * http 代理，请谨慎外显，为了不带来不必要的流量和运算
	 * 
	 * @return
	 */
	public static String httpProxy(HttpServletRequest request) {
		String url = request.getParameter("url");
		new MapHelper().setParameterMapRaw(request.getParameterMap()).ignoreField("url");
		String params = MapHelper.join(new MapHelper().setParameterMapRaw(request.getParameterMap()).toMap()
				.ignoreField("url").getParameterMap_String(), "&"); // 不要 url 参数
		return url + '?' + params;
	}

	/*
	 * 根据请求 ip 显示所在区域 https://www.ipip.net/
	 * http://int.dpool.sina.com.cn/iplookup/iplookup.php?format=js&ip=123.123.
	 * 123.12
	 */
	//String json = Client.GET("http://int.dpool.sina.com.cn/iplookup/iplookup.php?format=js&ip=" + MvcRequest.getIp(request));
	//out.println(json.replace("var remote_ip_info = ", "").replaceAll(";$", ""));
}
