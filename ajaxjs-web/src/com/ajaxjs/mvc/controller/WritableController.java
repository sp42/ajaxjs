package com.ajaxjs.mvc.controller;

import java.io.Serializable;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import com.ajaxjs.framework.model.ModelAndView;
import com.ajaxjs.framework.service.IService;
import com.ajaxjs.framework.service.ServiceException;
import com.ajaxjs.util.LogHelper;

public class WritableController<T, ID extends Serializable> extends ListAndInfoController<T, ID> {
	private static final LogHelper LOGGER = LogHelper.getLog(WritableController.class);

	/**
	 * 后台管理-新建
	 * 
	 * @param model
	 *            Model 模型
	 * @return JSP 路径
	 */
	@GET
	public String createUI(ModelAndView model) {
		LOGGER.info("后台管理-新建");
		IService<T, ID> service = getService();

		model.put("isCreate", true); /* 因为新建/编辑（update）为同一套 jsp 模版，所以用 isCreate = true 标识为创建，以便与 update 区分开来。*/
		service.prepareData(model);

		return jsp_perfix + service.getName() + "/adminInfo.jsp";
	}

	/**
	 * 后台管理-修改
	 * @param id id 序号
	 * @param model  Model 模型
	 * @return JSP 路径
	 */
	@GET
	@Path("/{id}")
	public String updateUI(@PathParam("id") ID id, ModelAndView model) {
		LOGGER.info("后台管理-修改");
		IService<T, ID> service = getService();
		
		getById(id, model);
		model.put("isCreate", false);
		service.prepareData(model);
		
		return jsp_perfix + service.getName() + "/adminInfo.jsp";
	}

	// @POST
	// @Override
	// public abstract String create(T entity, ModelAndView model);
	//
	// @PUT
	// @Path("/{id}")
	// @Override
	// public abstract String update(@PathParam("id") long id, T entry,
	// ModelAndView model);

	// @DELETE
	// @Path("/{id}")
	// @Override
	// public String delete(@PathParam("id") long id, ModelAndView model) {
	// return super.delete(id, model);
	// }

	public String create(/* @Valid */ T entity, ModelAndView model) {
		LOGGER.info("控制器-创建记录-" + entity);

		model.put("errMsg", str);

		getService().create(entity);
		// model.put("newlyId", entity.getId());

		return cud;
	}

	public String update(T entity, ModelAndView model) {
		LOGGER.info("修改 name:{0}，数据库将执行 UPDATE 操作", entity);

		model.put("isUpdate", true);

		try {
			getService().update(entity);
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		

		return cud;
	}

	public String delete(T entry, ModelAndView model) {
		LOGGER.info("删除 id:{0}，数据库将执行 DELETE 操作", entry);

		try {
			if (!getService().delete(entry)) {
				throw new ServiceException("删除失败！");
			}
		} catch (ServiceException e) {
			model.put(errMsg, e);
		}

		return common_jsp_perfix + "delete.jsp";
	}
}
