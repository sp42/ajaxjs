/**
 * Copyright 2015 Sp42 frank@ajaxjs.com
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
package com.ajaxjs.mvc;

import com.ajaxjs.js.JsonHelper;

/**
 * 
 * @author Sp42 frank@ajaxjs.com
 *
 */
public interface Constant {
	/**
	 * MV 用的 key
	 */
	public static final String PageResult = "PageResult";
	
	public static final String start = "start";
	
	public static final String limit = "limit";
	
	public static final String id = "id";
	
	public static final String catelogId = "catelogId";

	/**
	 * 文档显示用
	 */
	public static final String jsonType = "application/json";

	public static final String USER_SESSION_ID = "X-Ajaxjs-Token";

	public static final String USER_ID = "X-Ajaxjs-UserId";

	public static final String domainCatalog_Id = "domainCatalog_Id";

	/**
	 * 前端庫的位置
	 */
	public static final String ajajx_ui = "asset/ajaxjs-ui";

	/**
	 * 常见静态资源
	 */
	public static final String commonAsset = "asset/common";

	/**
	 * 全局 json 模板路径 之前缀
	 */
	public static final String jsp_perfix = "/jsp";

	/**
	 * 全局 json 模板路径 之 WEB-INF前缀
	 */
	public static final String jsp_perfix_webinf = "/WEB-INF/jsp";

	/**
	 * 操作失败，返回 msg 信息
	 */
	public static final String json_not_ok = "json::{\"isOk\": false, \"msg\" : \"%s\"}";

	/**
	 * 操作成功，返回 msg 信息，可扩展字段的
	 */
	public static final String json_ok_extension = "json::{\"isOk\": true, \"msg\" : \"%s\", %s}";

	/**
	 * 操作成功，返回 msg 信息
	 */
	public static final String json_ok = "json::{\"isOk\": true, \"msg\" : \"%s\"}";

	/**
	 * 输出 JSON OK
	 * 
	 * @param msg 输出信息
	 * @return JSON 字符串
	 */
	public static String jsonOk(String msg) {
		return String.format(json_ok, msg);
	}

	public static String jsonOk_Extension(Object... msg) {
		return String.format(json_ok_extension, msg);
	}

	/**
	 * 输出 JSON No OK
	 * 
	 * @param msg 输出信息
	 * @return JSON 字符串
	 */
	public static String jsonNoOk(String msg) {
		return String.format(json_not_ok, JsonHelper.jsonString_covernt(msg));
	}

	/**
	 * 显示 HTTP 405 禁止操作
	 */
	public static final String show405 = jsonNoOk("405， Request method not supported 禁止操作");

	/**
	 * 显示 HTTP 401 没有权限
	 */
	public static final String show401 = jsonNoOk("401， Request method not supported 没有权限");
}
