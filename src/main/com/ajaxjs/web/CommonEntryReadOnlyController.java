/**
 * Copyright Sp42 frank@ajaxjs.com
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
package com.ajaxjs.web;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;

import com.ajaxjs.mvc.ModelAndView;
import com.ajaxjs.mvc.controller.IController;

/**
 * 常见的控制器，前端用，只读的
 * 
 * @author Sp42 frank@ajaxjs.com
 *
 */
public interface CommonEntryReadOnlyController extends IController {
	/**
	 * 读取记录列表
	 * 
	 * @param start
	 * @param limit
	 * @param model
	 *            数据实体转换到页面层的对象
	 * @return 列表模板路径 或 JSON 列表路径
	 */
	@GET
	public String list(@QueryParam("start") int start, @QueryParam("limit") int limit, ModelAndView model);

	/**
	 * 读取单个记录
	 * 
	 * @param id
	 *            ID 序号
	 * @param model
	 *            数据实体转换到页面层的对象
	 * @return 记模板路径 或 JSON 列表路径
	 */
	@GET
	@Path("{id}")
	public String info(@PathParam("id") Long id, ModelAndView model);
}
