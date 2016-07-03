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
 

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import com.ajaxjs.framework.exception.ServiceException;
import com.ajaxjs.framework.service.IService;
import com.ajaxjs.mvc.model.BaseModel;
import com.ajaxjs.mvc.view.PageResult;
import com.ajaxjs.mvc.model.Query;
import com.ajaxjs.util.LogHelper;

/**
 * 基础的控制器公共类，该类不能直接使用，必须继承之。
 * 一般推荐前台一套接口，后台一套接口，分开来，例如 NewsController、NewsAdminController。
 * 各自的接口都集成于这个抽象控制器类。
 * @author frank
 *
 * @param <T>
 *            实体
 */
public abstract class AbstractCrudController<T extends BaseModel> implements CrudController<T> {
	private static final LogHelper LOGGER = LogHelper.getLog(AbstractCrudController.class);
	
	/**
	 * 对应的业务类
	 */
	private IService<T> service;
	
	/**
	 * 是否允许写操作，例如 POST/DELETE
	 */
	private boolean isEnableDefaultWrite = true;

	/**
	 * jsp路径的模版，这是默认的
	 */
	public final static String[] jspPath_tpl = {
		"entity/%s/frontEnd_list",	"entity/%s/frontEnd_info",
		"entity/%s/adminList", 		"entity/%s/adminInfo"
	};
	
	@Override
	public String list(int start, int limit, ModelAndView model) {
		LOGGER.info("获取列表 GET list:{0}/{1}", start, limit);
		HttpServletRequest request = RequestHelper.getHttpServletRequest();
		PageResult<T> pageResult = null;
		try {
			if(request.getParameter("filterField") != null 
				|| request.getParameter("searchField") != null
				|| request.getParameter("matchField")  != null
				|| request.getParameter("orderField")  != null) {
				// 其他丰富的查询参数
				pageResult = getService().getPageRows(start, limit, Query.getQueryFactory(request));
			} else {
				pageResult = getService().getPageRows(start, limit, null);
			}

			model.put("PageResult", pageResult);
		} catch (ServiceException e) {
			model.put("ServiceException", e);
		}
		
		prepareData(request, model);
		
		if(request.getRequestURI().endsWith(".list")) {
			return "common/list/simple";		// return html
		} else {
			return "common/list/pageList_json";	// return JSON
		}
	}

	@Override
	public String list_all(HttpServletRequest request, ModelAndView model) {
		LOGGER.info("获取全部列表 ");
		return list(0, 999,  model);
	}
	
	@Override
	public String getById(long id, ModelAndView model) {
		IService<T> service = getService();
		
		try {
			BaseModel bean = service.getById(id);
			model.put("info", bean);
//			System.out.println(service.getModelAndView());
//			System.out.println(model);
//			BaseService.toSpringMVC_model(service.getModelAndView(), model);
		} catch (ServiceException e) {
			model.put("ServiceException", e);
		}
				
		// 相邻记录
//		try {
//			model.put("neighbor", EntityUtil.getNeighbor(service.getTableName(), id));
//		} catch (DaoException e) {
//			LOGGER.warning(e);
//			model.put("DaoException", e);
//		}
		
		return formatDectect(null, null);
	}
	
	/**
	 * 
	 * @param request
	 * @param whichTpl
	 * @return
	 */
	public String formatDectect(HttpServletRequest request, int whichTpl) {
		return formatDectect(request, String.format(jspPath_tpl[whichTpl], getService().getTableName()));
	}

	/**
	 *  一个实体可以由不同的 uniform 决定其外观。
	 * @param request
	 * @param jspPath
	 * @return
	 */
	public String formatDectect(HttpServletRequest request, String jspPath) {
		if(request ==  null)return getService().getTableName();
		
		String uri = request.getRequestURI();
		if (uri.endsWith(".json")) {
			// return JSON
			if (uri.endsWith("list.json") || uri.endsWith("all.json")) {
				return "common/list/pageList_json";
			} else {
				return "common/showInfo_json";
			}
		} else if (uri.endsWith(".xml")) {
			return "common/showInfo_xml"; // return XML
		} else {
			// updateUI
			if (jspPath != null)
				return jspPath;
		}
		return getService().getTableName();
	}
	
	/**
	 * 后台管理-新建
	 * 
	 * @param model
	 *            SpringMVC 模型
	 * @return JSP 路径
	 */
	public String createUI(ModelAndView model) {
		LOGGER.info("后台管理-新建");
		model.put("isCreate", true); // 因为新建/编辑为同一套 jsp模版，所以表识为 创建，以便区分开来。
		prepareData(null, model);
		
		return getService().getTableName();
	}
	
	/**
	 * 后台管理-修改
	 * 
	 * @param id
	 *            id 序号
	 * @param request
	 *            请求对象
	 * @return JSP 路径
	 */
//	public String updateUI(int id, ModelAndView model) {
//		LOGGER.info("后台管理-修改:{0}", id);
//		
//		getById(id, model, null);
//		model.put("isCreate", false);
//		prepareData(null, model);
//		
//		return getService().getTableName();
//	}
	
	/**
	 * 设置实体 id 和 现实名称 。通过设置下面的注解可以对 model 增添字段。
	 * 
	 * @param model
	 *            SpringMVC 模型
	 */
//	@ModelAndViewAttribute
	public void setText(ModelAndView model) {
		// 每次 servlet 都会执行的。记录时间
		model.put("requestTimeRecorder",  System.currentTimeMillis());
		
		model.put("uiName", getService().getUiName());
		System.out.println(getService().getClass().getName());
		model.put("tableName", getService().getTableName());
	}
	
	/**
	 * 输出文档
	 * 
	 * @param model
	 *            SpringMVC 模型
	 * @param entity
	 *            POJO
	 * @return
	 */
//	@RequestMapping(value = "/document", method = RequestMethod.GET)
	public String getDocument(ModelAndView model, T entity) {
//		String[] strs = DocumentRenderer.getEntityInfo(entity.getClass());
//		model.put("entityInfo", strs[0]);
//		if(strs[1] != null) { // 更多关于该实体的文档
//			model.put("moreDocument", strs[1]);
//		}
//		
//		model.put("meta", DocumentRenderer.getDocument(entity.getClass(), getService().getSQL_TableName()));
//		
		return "common/entity/showDocument";
	}

	// ----------------写操作-------------------
	//----------------POST-------------------
	@POST
	@Override
	public String create(@Valid T entity, ModelAndView model) {
		if (!isEnableDefaultWrite()) {
			return "common/entity/json_noWrite";
		}
		
//		if (result.hasErrors()) {
//			List<ObjectError> errors = result.getAllErrors();
//		
//			String str = "";
//			for(ObjectError err : errors) {
//				str += err.getCodes()[0] + err.getObjectName() + "\\n";
//				LOGGER.info(err.getCode() + ":" + err.getObjectName() + err.getDefaultMessage());
//			}
//			
//			model.put("errMsg", str);
//		}else{
			LOGGER.info("创建记录" + entity.getName());
			entity.setService(getService());
			
			try {
				getService().create(entity);
				model.put("newlyId", entity.getId());
			} catch (ServiceException e) {
				model.put("errMsg", e.toString());
			}
			
//		}
		
		return "common/entity/json_cud";
	}

	@Path("/{id}")
	@POST
	@Override
	public String update(@PathParam("id") long id, T entity, ModelAndView model) {
		if (!isEnableDefaultWrite()) {
			return "common/entity/json_noWrite";
		}
		
		LOGGER.info("修改 name:{0}，数据库将执行 UPDATE 操作", entity.getName());
		
		entity.setService(getService());
		entity.setId(id);
		model.put("isUpdate", true);
		
		try {
			getService().update(entity);
		} catch (ServiceException e) {
			model.put("errMsg", e.getMessage());
		}
		
		return "common/entity/json_cud";
	}

	@Path("/{id}")
	@DELETE
	@Override
	public String delete(@PathParam("id") long id, ModelAndView model) {
		if (!isEnableDefaultWrite()) {
			return "common/entity/json_noWrite";
		}
		
		LOGGER.info("删除 id:{0}，数据库将执行 DELETE 操作", id);

		try {
			if (!getService().deleteByID(id)) {
				throw new ServiceException("删除失败！");
			}
		} catch (ServiceException e) {
			model.put("ServiceException", e);
		}

		return "common/entity/json_delete";
	}
	
	/**
	 * 模版方法，用于装备其他数据，如分类这些外联的表。 不使用 abstract 修饰，因为这将强制各个子类都要实现，麻烦。
	 * 
	 * @param reqeust
	 *            请求对象
	 * @param model
	 *            SpringMVC 模型
	 */
	public void prepareData(HttpServletRequest reqeust, ModelAndView model){};
	
	/**
	 * 模版方法，用于装备其他数据，如分类这些外联的表。
	 * 
	 * @param reqeust
	 *            请求对象
	 * @param model
	 *            SpringMVC 模型
	 */
	public void on_entity_prepareData(HttpServletRequest reqeust, ModelAndView model){};
	
	/**
	 * 指定日期格式
	 * @param binder
	 */
//	@InitBinder
//	public void initBinder(WebDataBinder binder) {
//		binder.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd hh:mm"), true));
//	}
	
	public IService<T> getService() {
		return service;
	}

	public void setService(IService<T> service) {
		this.service = service;
	}

	public boolean isEnableDefaultWrite() {
		return isEnableDefaultWrite;
	}

	public void setEnableDefaultWrite(boolean isEnableDefaultWrite) {
		this.isEnableDefaultWrite = isEnableDefaultWrite;
	}
}
