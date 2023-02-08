package com.ajaxjs.util;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.ajaxjs.net.http.Get;

public class JspBack {
	public static void getInfo(HttpServletRequest request, String namespace) {
		String id = request.getParameter("id");
		boolean isCreate = id == null;
		request.setAttribute("isCreate", isCreate);

		if (!isCreate) {
//			String url = WebHelper.getLocalService(request);
//			Map<String, Object> info = Get.api(url + "/data_service/" + namespace + "?id=" + id);
//			request.setAttribute("info", info.get("data"));
			// System.out.println(info.get("data"));
		}
	}

	public static void getList(HttpServletRequest request, String _url, String key) {
		String url = com.ajaxjs.util.WebHelper.getLocalService(request);
		java.util.Map<String, Object> info = com.ajaxjs.net.http.Get.api(url + _url);
		request.setAttribute(key, info.get("data"));
	}

	public static void list(HttpServletRequest request, String namespace, String namespaceChS) {
		getList(request, "/data_service/" + namespace + "/list", "list");

		request.setAttribute("namespace", namespace);
		request.setAttribute("namespace_chs", namespaceChS);
	}

}
