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
package com.ajaxjs.simpleApp;

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
	
	/**
	 * 前端庫的位置
	 */
	public static final String ajajx_ui = "asset/ajaxjs-ui";

	/**
	 * 常见静态资源
	 */
	public static final String commonAsset = "asset/common";

	/**
	 * 常见 jsp 页面
	 */
	public static final String commonJsp = "/asset/common/jsp";

	/**
	 * 全局 json 模板路径 之前缀
	 */
	public static final String jsp_perfix = "/asset/jsp";

	/**
	 * json 路径常量（实体创建、修改）
	 */
	public static final String cud = commonJsp + "/json/json-cud.jsp";

	/**
	 * json 路径常量（实体）
	 */
	public static final String show_json_info = commonJsp + "/json/showInfo.jsp";

	/**
	 * json 路径常量（分页列表）
	 */
	public static final String paged_json_List = commonJsp + "/json/json-pagedList.jsp";

	/**
	 * json 路径常量（错误信息）
	 */
	public static final String paged_json_error = commonJsp + "/json/json-err.jsp";

	/**
	 * 操作成功，返回 msg 信息
	 */
	public static final String json_ok = "json::{\"isOk\": true, \"msg\" : \"%s\"}";
	
	/**
	 * 操作成功，返回 msg 信息，可扩展字段的
	 */
	public static final String json_ok_extension = "json::{\"isOk\": true, \"msg\" : \"%s\", %s}";

	/**
	 * 操作失败，返回 msg 信息
	 */
	public static final String json_not_ok = "json::{\"isOk\": false, \"msg\" : \"%s\"}";

	/**
	 * 显示 HTTP 405 禁止操作
	 */
	public static final String show405 = String.format(json_not_ok, "405， Request method not supported 禁止操作");
}
