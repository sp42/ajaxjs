/**
 * Copyright 2015 Sp42 frank@ajaxjs.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a
 * copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations
 * under the License.
 */
package com.ajaxjs.mvc;

/**
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
public interface Constant {
	/**
	 * MV 用的 key
	 */
	public static final String PAGE_RESULT = "PageResult";

	public static final String START = "start";

	public static final String LIMIT = "limit";

	public static final String ID = "id";

	public static final String LIST = "list";

	public static final String INFO = "info";

	public static final String ID_INFO = "{id}";

	public static final String CATALOG_ID = "catalogId";

	/**
	 * 搜索关键字
	 */
	public static final String KEYWORD = "keyword";

	/**
	 * 文档显示用
	 */
	public static final String JSON_TYPE = "application/json";

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
	public static final String JSON_NOT_OK = "json::{\"isOk\": false, \"msg\" : \"%s\"}";

	/**
	 * 操作成功，返回 msg 信息，可扩展字段的
	 */
	public static final String JSON_OK_EXTENSION = "json::{\"isOk\": true, \"msg\" : \"%s\", %s}";

	/**
	 * 操作成功，返回 msg 信息
	 */
	public static final String JSON_OK = "json::{\"isOk\": true, \"msg\" : \"%s\"}";
}
