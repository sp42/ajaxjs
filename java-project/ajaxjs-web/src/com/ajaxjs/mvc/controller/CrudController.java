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

import com.ajaxjs.framework.model.ModelAndView;

/**
 * 为注入而设的对象标识。
 * 
 * @author frank
 *
 * @param <T>
 */
public interface CrudController<T> extends IController {
	/**
	 * 默认的模板路径
	 */
	public static final String perfix = "/common_jsp/json/";
	
	/**
	 * 分页查询
	 * 
	 * @param start
	 *            起始行数，默认从零开始
	 * @param limit
	 *            偏量值，默认 8 笔记录
	 * @param request
	 *            请求对象，可考虑 //@RequestParam Map<String, String> allRequestParams？
	 * @param ModelAndView
	 *            Model 模型
	 * @return JSP 路径。我们会提供一个默认路径，但你不一定要使用它，换别的也可以。
	 */
	public String list(int start, int limit, ModelAndView ModelAndView);

	/**
	 * 获取全部数据
	 * 
	 * @param request
	 *            请求对象
	 * @param ModelAndView
	 *            Model 模型
	 * @return JSP 路径。我们会提供一个默认路径，但你不一定要使用它，换别的也可以。
	 */
	public String list_all(ModelAndView ModelAndView);

	/**
	 * 读取单个记录，保存到 ModelAndView 中，供视图渲染用。
	 * 
	 * @param id
	 *            id 序号
	 * @param ModelAndView
	 *            SpringMVC 模型
	 * @param reqeust
	 *            请求对象
	 * @return JSP 路径。我们会提供一个默认路径，但你不一定要使用它，换别的也可以。
	 */
	public String getById(long id, ModelAndView ModelAndView);

	/**
	 * 新建
	 * 
	 * @param entity
	 *            带有值的实体
	 * @param result
	 *            数据验证的结果
	 * @param ModelAndView
	 *            SpringMVC 模型
	 * @return JSP 路径，返回 JSON 格式的
	 */
	public String create(T entity, ModelAndView ModelAndView);

	/**
	 * 修改
	 * 
	 * @param id
	 *            id 序号
	 * @param entity
	 *            带有值的实体
	 * @param ModelAndView
	 *            SpringMVC 模型
	 * @return JSP 路径，返回 JSON 格式的
	 */
	public String update(long id, T entity, ModelAndView ModelAndView);

	/**
	 * 删除
	 * 
	 * @param id
	 *            id 序号
	 * @param ModelAndView
	 *            SpringMVC 模型
	 * @return JSP 路径，返回 JSON 格式的
	 */
	public String delete(long id, ModelAndView ModelAndView);
}
