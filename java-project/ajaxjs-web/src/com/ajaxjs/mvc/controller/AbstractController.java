package com.ajaxjs.mvc.controller;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.PathParam;

import com.ajaxjs.framework.exception.ServiceException;
import com.ajaxjs.framework.model.BaseModel;
import com.ajaxjs.framework.model.ModelAndView;
import com.ajaxjs.framework.model.PageResult;
import com.ajaxjs.framework.model.Query;
import com.ajaxjs.framework.service.IService;
import com.ajaxjs.util.LogHelper;

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
				pageResult = getService().getPageRows(start, limit, Query.getQueryFactory(request));
			} else {
				pageResult = getService().getPageRows(start, limit, null);
			}

			model.put("PageResult", pageResult);
		} catch (ServiceException e) {
			model.put("ServiceException", e);
			LOGGER.warning(e);
		}
		
		prepareData(null, model);
		
		return getService().getTableName();
	}
	
	@Override
	public String list_all(ModelAndView model) {
		LOGGER.info("----获取全部列表----");
		
		prepareData(null, model);
		return list(0, 999,  model);
	}

	@Override
	public String getById(long id, ModelAndView model) {
		IService<T> service = getService();
		
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
		
		prepareData(null, model);
		
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
	public String updateUI(long id, ModelAndView model) {
		LOGGER.info("后台管理-修改:{0}", id);
		
		getById(id, model);
		model.put("isCreate", false);
		prepareData(null, model);
		
		return getService().getTableName();
	}
	
	/**
	 * 模版方法，用于装备其他数据，如分类这些外联的表。 不使用 abstract 修饰，因为这将强制各个子类都要实现，麻烦。
	 * 
	 * @param reqeust
	 *            请求对象
	 * @param model
	 *            模型
	 */
	public void prepareData(HttpServletRequest reqeust, ModelAndView model) {
		// 每次 servlet 都会执行的。记录时间
		model.put("requestTimeRecorder", System.currentTimeMillis());

		// 设置实体 id 和 现实名称 。
		model.put("uiName", getService().getUiName());
		model.put("tableName", getService().getTableName());
	}
	
	// ----------------写操作-------------------
	@Override
	public String create(/*@Valid*/ T entity, ModelAndView model) {
		LOGGER.info("创建记录" + entity.getName());
				
//			if (result.hasErrors()) {
//				List<ObjectError> errors = result.getAllErrors();
//			
//				String str = "";
//				for(ObjectError err : errors) {
//					str += err.getCodes()[0] + err.getObjectName() + "\\n";
//					LOGGER.info(err.getCode() + ":" + err.getObjectName() + err.getDefaultMessage());
//				}
//				
//				model.put("errMsg", str);
//			}else{
		
		entity.setService(getService());
		
		try {
			getService().create(entity);
			model.put("newlyId", entity.getId());
		} catch (ServiceException e) {
			model.put("errMsg", e.toString());
		}
		
//			}
		
		return perfix + "cud.jsp";
	}

	@Override
	public String update(@PathParam("id") long id, T entity, ModelAndView model) {
		LOGGER.info("修改 name:{0}，数据库将执行 UPDATE 操作", entity.getName());

		entity.setService(getService());
		entity.setId(id);
		model.put("isUpdate", true);

		try {
			getService().update(entity);
		} catch (ServiceException e) {
			model.put("errMsg", e.getMessage());
		}

		return perfix + "cud.jsp";
	}

	@Override
	public String delete(@PathParam("id") long id, ModelAndView model) {
		LOGGER.info("删除 id:{0}，数据库将执行 DELETE 操作", id);

		try {
			if (!getService().deleteByID(id)) {
				throw new ServiceException("删除失败！");
			}
		} catch (ServiceException e) {
			model.put("ServiceException", e);
		}

		return perfix + "delete.jsp";
	}
	
	public IService<T> getService() {
		if(service == null) throw new NullPointerException("没有业务层对象！");
		return service;
	}

	public void setService(IService<T> service) {
		this.service = service;
	}
}
