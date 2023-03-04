package com.ajaxjs.zinc.api;

import java.io.Serializable;
import java.net.HttpURLConnection;
import java.util.Map;
import java.util.function.Consumer;

import com.ajaxjs.util.StrUtil;
import com.ajaxjs.zinc.model.ZincResponse;

import lombok.Data;

@Data
public abstract class BaseService {
	/**
	 * Zinc 服务 API 地址
	 */
	private String api;

	/**
	 * 用户名
	 */
	private String user;

	/**
	 * 密码
	 */
	private String password;

	/**
	 * 设置请求头
	 */
	Consumer<HttpURLConnection> SET_HEAD = (head) -> {
		String encode = user + ":" + password;
		encode = StrUtil.base64Encode(encode);

		head.setRequestProperty("Content-Type", "application/json");
		head.setRequestProperty("Authorization", "Basic " + encode);
	};

	ZincResponse result(Map<String, Object> result) {
		ZincResponse response = new ZincResponse();

		if (result.containsKey("error")) {
			response.setHasError(true);
			response.setErrMsg(result.get("error").toString());
		} else {
			response.setHasError(false);
		}

		return response;
	}

	String apiWithId(String target, Serializable id) {
		String url = getApi() + "/api/" + target;

		if (id != null)
			url += "/" + id;

		return url;
	}
}
