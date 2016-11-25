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

import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import com.ajaxjs.framework.exception.ServiceException;
import com.ajaxjs.framework.model.BaseModel;
import com.ajaxjs.framework.model.ModelAndView;
import com.ajaxjs.framework.model.PageResult;
import com.ajaxjs.framework.model.Query;
import com.ajaxjs.framework.service.DocumentRenderer;
import com.ajaxjs.framework.service.IService;
import com.ajaxjs.util.LogHelper;

/**
 * 抽象的增删改查控制器
 * @author frank
 *
 * @param <T>
 */
public abstract class AbstractController<T extends BaseModel> implements CrudController<T> {
	private static final LogHelper LOGGER = LogHelper.getLog(AbstractController.class);

	/**
	 * 对应的业务类
	 */
	private IService<T> service;
	
	@Override
	public String list(int start, int limit, ModelAndView model) {
		LOGGER.info("获取列表 GET list:{0}/{1}", start, limit);
		
		HttpServletRequest request = RequestHelper.getHttpServletRequest();
		PageResult<T> pageResult = null;
		
		try {
			if(Query.isAnyMatch(request)) {
				// 其他丰富的查询参数
				pageResult = service.getPageRows(start, limit, Query.getQueryFactory(request));
			} else {
				pageResult = service.getPageRows(start, limit, null);
			}

			model.put("PageResult", pageResult);
		} catch (ServiceException e) {
			model.put("ServiceException", e);
			LOGGER.warning(e);
		}
		
		prepareData(request, model);
		
		return service.getTableName();
	}
	
	@Override
	public String list_all(ModelAndView model) {
		LOGGER.info("----获取全部列表----");
		
		prepareData(null, model);
		return list(0, 999,  model);
	}

	@Override
	public String getById(long id, ModelAndView model) {		
		try {
			BaseModel bean = service.getById(id);
			model.put("info", bean);
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
		
		prepareData(RequestHelper.getHttpServletRequest(), model);
		
		return service.getTableName();
	}
	
	/**
	 * 后台管理-新建
	 * 
	 * @param model
	 *            Model 模型
	 * @return JSP 路径
	 */
	public String createUI(ModelAndView model) {
		LOGGER.info("后台管理-新建");
		
		model.put("isCreate", true); // 因为新建/编辑为同一套 jsp模版，所以表识为 创建，以便区分开来。
		prepareData(null, model);
		
		return service.getTableName();
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
	public String updateUI(long id, ModelAndView model) {
		getById(id, model);
		model.put("isCreate", false);
		prepareData(null, model);
		
		return service.getTableName();
	}
	
	@Override
	public void prepareData(HttpServletRequest reqeust, ModelAndView model) {
		// 每次 servlet 都会执行的。记录时间
		model.put("requestTimeRecorder", System.currentTimeMillis());

		// 设置实体 id 和 现实名称 。
		model.put("uiName", service.getUiName());
		model.put("tableName", service.getTableName());
	}
	
	// ----------------写操作-------------------
	@Override
	public String create(/* @Valid */ T entity, ModelAndView model) {
		LOGGER.info("控制器-创建记录-" + entity.getName());

//		if (result.hasErrors()) {
//		List<ObjectError> errors = result.getAllErrors();
//	
//		String str = "";
//		for(ObjectError err : errors) {
//			str += err.getCodes()[0] + err.getObjectName() + "\\n";
//			LOGGER.info(err.getCode() + ":" + err.getObjectName() + err.getDefaultMessage());
//		}
//		
//		model.put("errMsg", str);
//	}

		Validator v = ModelAndView.getValidator();
		Set<ConstraintViolation<T>> results = v.validate(entity);

		if (!results.isEmpty()) {
			String str = "";
			for (ConstraintViolation<T> result : results) {
				str += result.getPropertyPath() + result.getMessage() + "\\n";
				LOGGER.info(result.getPropertyPath() + ":" + result.getMessage());
			}

			model.put("errMsg", str);
		} else {
			entity.setService(service);

			try {
				service.create(entity);
				model.put("newlyId", entity.getId());
			} catch (ServiceException e) {
				model.put("errMsg", e.toString());
			}

		}

		return cud;
	}

	@Override
	public String update(long id, T entity, ModelAndView model) {
		LOGGER.info("修改 name:{0}，数据库将执行 UPDATE 操作", entity.getName());

		entity.setService(service);
		entity.setId(id);
		model.put("isUpdate", true);

		try {
			service.update(entity);
		} catch (ServiceException e) {
			model.put("errMsg", e.getMessage());
		}

		return cud;
	}

	@Override
	public String delete(long id, ModelAndView model) {
		LOGGER.info("删除 id:{0}，数据库将执行 DELETE 操作", id);

		try {
			if (!service.deleteByID(id)) {
				throw new ServiceException("删除失败！");
			}
		} catch (ServiceException e) {
			model.put("ServiceException", e);
		}

		return common_jsp_perfix + "delete.jsp";
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
	public String getDocument(ModelAndView model, T entity) {
		String[] strs = DocumentRenderer.getEntityInfo(entity.getClass());
		model.put("entityInfo", strs[0]);
		if (strs[1] != null) { // 更多关于该实体的文档
			model.put("moreDocument", strs[1]);
		}

		model.put("meta", DocumentRenderer.getDocument(entity.getClass(), service.getSQL_TableName()));

		return "common/entity/showDocument";
	}
	
	public IService<T> getService() {
		if(service == null) throw new NullPointerException("没有业务层对象！");
		return service;
	}

	public void setService(IService<T> service) {
		this.service = service;
	}
}
