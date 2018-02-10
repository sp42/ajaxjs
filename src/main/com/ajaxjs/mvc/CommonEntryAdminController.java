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
package com.ajaxjs.mvc;

import java.io.Serializable;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;

import com.ajaxjs.mvc.controller.IController;

/**
 * 常见的控制器，后端用
 * 
 * @author Sp42 frank@ajaxjs.com
 *
 * @param <E>
 *            记录实体，可以是 Bean 或 Map
 */
public interface CommonEntryAdminController<E, ID extends Serializable> extends IController {
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
	@Path("list")
	public String list(@QueryParam("start") int start, @QueryParam("limit") int limit, ModelAndView model);

	/**
	 * 新建界面
	 * 
	 * @param model
	 *            数据实体转换到页面层的对象
	 * @return 新建界面模板路径
	 */
	@GET
	public String createUI(ModelAndView model);

	/**
	 * 编辑界面
	 * 
	 * @param id
	 *            ID 序号
	 * @param model
	 *            数据实体转换到页面层的对象
	 * @return 编辑界面模板路径
	 */
	@GET
	@Path("{id}")
	public String editUI(@PathParam("id") ID id, ModelAndView model);

	/**
	 * 新建动作
	 * 
	 * @param entity
	 *            记录实体，可以是 Bean 或 Map
	 * @param model
	 *            数据实体转换到页面层的对象
	 * @return 创建后消息反馈之 JSON
	 */
	@POST
	public String create(E entity, ModelAndView model);

	/**
	 * 修改动作
	 * 
	 * @param entity
	 *            记录实体，可以是 Bean 或 Map
	 * @param model
	 *            数据实体转换到页面层的对象
	 * @return 创建后消息反馈之 JSON
	 */
	@PUT
	@Path("{id}")
	public String update(E entity, ModelAndView model);

	/**
	 * 删除一记录，注意是传入 id 参数
	 * 
	 * @param id
	 *            ID 序号
	 * @param model
	 *            数据实体转换到页面层的对象
	 * @return 创建后消息反馈之 JSON
	 */
	@DELETE
	@Path("{id}")
	public String delete(@PathParam("id") ID id, ModelAndView model);
}
