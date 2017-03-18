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

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;

import com.ajaxjs.framework.dao.QueryParams;
import com.ajaxjs.framework.model.ModelAndView;
import com.ajaxjs.framework.model.PageResult;
import com.ajaxjs.framework.model.Query;
import com.ajaxjs.framework.service.IService;
import com.ajaxjs.framework.service.ServiceException;
import com.ajaxjs.util.LogHelper;

/**
 * 只读输出，有 /{id} 和 /list 两个路由， 可以选择 json/html 输出。 不能复用 create/update 方法，这是因为 T
 * 泛型不能正确识别 Bean 类型的缘故
 * 
 * @author frank
 *
 * @param <T>
 */
public class ListAndInfoController<T, ID extends Serializable> implements IController {
	private static final LogHelper LOGGER = LogHelper.getLog(ListAndInfoController.class);

	/**
	 * 对应的业务类
	 */
	private IService<T, ID> service;

	/**
	 * 是否输出 json 格式
	 */
	private boolean JSON_output;

	/**
	 * 分页查询
	 * 
	 * @param start
	 *            起始行数，默认从零开始
	 * @param limit
	 *            偏量值，默认 8 笔记录
	 * @param model
	 *            Model 模型
	 * @return JSP 路径。缺省提供一个默认路径，但不一定要使用它，换别的也可以。
	 */
	@GET
	@Path("/list")
	public String list(@QueryParam("start") int start, @QueryParam("limit") int limit, ModelAndView model) {
		LOGGER.info("获取列表 GET list:{0}/{1}", start, limit);

		HttpServletRequest request = RequestHelper.getHttpServletRequest();
		PageResult<T> pageResult = null;
		QueryParams param = new QueryParams(start, limit);

		if (Query.isAnyMatch(request.getParameterMap())) {
			// 其他丰富的查询参数
			param.query = Query.getQueryFactory(request.getParameterMap());
		}

		try {
			pageResult = service.findPagedList(param);
			model.put("PageResult", pageResult);
		} catch (ServiceException e) {
			model.put(errMsg, e);
		}

		service.prepareData(model);

		return isJSON_output() ? pagedList : jsp_perfix + service.getName() + "/frontEnd_list";
	}

	@GET
	public String redirect() {
		return "redirect:list";
	}

	/**
	 * 读取单个记录，保存到 ModelAndView 中（供视图渲染用）。
	 * 
	 * @param id
	 *            ID 序号
	 * @param model
	 *            Model 模型
	 * @return JSP 路径。缺省提供一个默认路径，但不一定要使用它，换别的也可以。
	 */
	@GET
	@Path("/{id}")
	public String getById(@PathParam("id") ID id, ModelAndView model) {
		T entry;
		try {
			entry = service.findById(id);
			model.put("info", entry);
		} catch (ServiceException e) {
			model.put(errMsg, e);
		}

		// model.put("neighbor", EntityUtil.getNeighbor(service.getName(),
		// id));// 相邻记录

		service.prepareData(model);

		return isJSON_output() ? showInfo : jsp_perfix + service.getName() + "/frontEnd_info";
	}

	public String list_all(ModelAndView model) {
		LOGGER.info("----获取全部列表----");

		service.prepareData(model);
		return list(0, 999, model);
	}

	/**
	 * 保存到 request
	 * 
	 * @param request
	 *            请求对象
	 */
	public void saveToReuqest(ModelAndView mv, HttpServletRequest request) {
		for (String key : mv.keySet())
			request.setAttribute(key, mv.get(key));
	}

	/**
	 * 输出文档 GET /document
	 * 
	 * @param model
	 *            Model 模型
	 * @param entity
	 *            POJO
	 * @return
	 */
	// public String getDocument(ModelAndView model, T entity) {
	// String[] strs = DocumentRenderer.getEntityInfo(entity.getClass());
	// model.put("entityInfo", strs[0]);
	// if (strs[1] != null) { // 更多关于该实体的文档
	// model.put("moreDocument", strs[1]);
	// }
	//
	// model.put("meta", DocumentRenderer.getDocument(entity.getClass(),
	// service.getSQL_TableName()));
	//
	// return "common/entity/showDocument";
	// }

	public IService<T, ID> getService() {
		if (service == null)
			throw new NullPointerException("没有业务层对象！");
		return service;
	}

	public void setService(IService<T, ID> service) {
		this.service = service;
	}

	public boolean isJSON_output() {
		return JSON_output;
	}

	public void setJSON_output(boolean jSON_output) {
		JSON_output = jSON_output;
	}
}
