/**
 * Copyright 2015 Frank Cheung
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
package com.ajaxjs.mvc.controller;


/**
 * 控制器
 * 一个 Controller 通常拥有多个方法，每个方法负责处理一个 URL。
 * 直接把一个 URL 映射到一个方法，这样，Web 开发人员就可以将多个功能类似的方法放到一个 Controller 中
 * 
 * @author frank
 *
 * @param <T>
 */
public interface IController {
	/**
	 * 全局 json 模板路径 之前缀
	 */
	public static final String common_jsp_perfix = "/common_jsp/json/";
	
	public static final String json_not_ok_simple = "json::{\"isOk\": false}";
	
	public static final String json_not_ok = "json::{\"isOk\": false, \"msg\" : \"%s\"}";
	
	public static final String json_ok = "json::{\"isOk\": true, \"msg\" : \"%s\"}";

	/**
	 * json 路径常量（实体创建、修改）
	 */
	public static final String cud = common_jsp_perfix + "cud.jsp";
	
	/**
	 * json 路径常量（实体）
	 */
	public static final String showInfo = common_jsp_perfix + "showInfo.jsp";
	/**
	 * json 路径常量（分页列表）
	 */
	public static final String pagedList = common_jsp_perfix + "pagedList.jsp";

	/**
	 * 实体 jsp 模版路径之前缀
	 */
	public static final String jsp_perfix = "/WEB-INF/jsp/entry/";
	
	/**
	 * 保存错误信息的 key
	 */
	public static final String errMsg = "errMsg";
//	
//	/**
//	 * 分页查询
//	 * 
//	 * @param start
//	 *            起始行数，默认从零开始
//	 * @param limit
//	 *            偏量值，默认 8 笔记录
//	 * @param request
//	 *            请求对象，可考虑 //@RequestParam Map<String, String> allRequestParams？
//	 * @param ModelAndView
//	 *            Model 模型
//	 * @return JSP 路径。我们会提供一个默认路径，但你不一定要使用它，换别的也可以。
//	 */
//	public String list(int start, int limit, ModelAndView ModelAndView);
//
//	/**
//	 * 获取全部数据
//	 * 
//	 * @param request
//	 *            请求对象
//	 * @param ModelAndView
//	 *            Model 模型
//	 * @return JSP 路径。我们会提供一个默认路径，但你不一定要使用它，换别的也可以。
//	 */
//	public String list_all(ModelAndView ModelAndView);
//
//	/**
//	 * 读取单个记录，保存到 ModelAndView 中，供视图渲染用。
//	 * 
//	 * @param id
//	 *            id 序号
//	 * @param ModelAndView
//	 *            SpringMVC 模型
//	 * @param reqeust
//	 *            请求对象
//	 * @return JSP 路径。我们会提供一个默认路径，但你不一定要使用它，换别的也可以。
//	 */
//	public String getById(ID id, ModelAndView ModelAndView);
//
//	/**
//	 * 新建
//	 * 
//	 * @param entity
//	 *            带有值的实体
//	 * @param result
//	 *            数据验证的结果
//	 * @param ModelAndView
//	 *            SpringMVC 模型
//	 * @return JSP 路径，返回 JSON 格式的
//	 */
//	public String create(T entity, ModelAndView ModelAndView);
//
//	/**
//	 * 修改
//	 * 
//	 * @param id
//	 *            id 序号
//	 * @param entity
//	 *            带有值的实体
//	 * @param ModelAndView
//	 *            SpringMVC 模型
//	 * @return JSP 路径，返回 JSON 格式的
//	 */
//	public String update(T entity, ModelAndView ModelAndView);
//
//	/**
//	 * 删除
//	 * 
//	 * @param id
//	 *            id 序号
//	 * @param ModelAndView
//	 *            SpringMVC 模型
//	 * @return JSP 路径，返回 JSON 格式的
//	 */
//	public String delete(T id, ModelAndView ModelAndView);
}
