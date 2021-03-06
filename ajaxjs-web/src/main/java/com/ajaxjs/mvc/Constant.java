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

import com.ajaxjs.keyvalue.MappingHelper;

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
	 * 全局 json 模板路径 之前缀
	 */
	public static final String jsp_perfix = "/jsp";
	
	/**
	 * 全局 json 模板路径 之 WEB-INF前缀
	 */
	public static final String jsp_perfix_webinf = "/WEB-INF/jsp";

	/**
	 * json 路径常量（实体创建、修改）
	 */
	public static final String cud = jsp_perfix + "/json/json-cud.jsp";

	/**
	 * json 路径常量（实体）
	 */
	public static final String show_json_info = jsp_perfix + "/json/showInfo.jsp";

	/**
	 * json 路径常量（分页列表）
	 */
	public static final String paged_json_List = jsp_perfix + "/json/json-pagedList.jsp";

	/**
	 * json 路径常量（错误信息）
	 */
	public static final String paged_json_error = jsp_perfix + "/json/json-err.jsp";

	/**
	 * 显示 HTTP 405 禁止操作
	 */
	public static final String show405 = String.format(MappingHelper.json_not_ok, "405， Request method not supported 禁止操作");
}

