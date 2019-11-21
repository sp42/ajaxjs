package com.ajaxjs.cms.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.ajaxjs.cms.DataDictService;
import com.ajaxjs.framework.BaseController;
import com.ajaxjs.framework.IBaseService;
import com.ajaxjs.ioc.Bean;
import com.ajaxjs.ioc.Resource;
import com.ajaxjs.mvc.Constant;
import com.ajaxjs.mvc.ModelAndView;
import com.ajaxjs.mvc.filter.DataBaseFilter;
import com.ajaxjs.mvc.filter.MvcFilter;
import com.ajaxjs.util.logger.LogHelper;

@Bean
@Path("/admin/DataDict")
public class DataDictController extends BaseController<Map<String, Object>> {
	private static final LogHelper LOGGER = LogHelper.getLog(DataDictController.class);
	
	@Resource("DataDictService")
	private DataDictService service;
	
	@GET
	@Override
	public String createUI(ModelAndView mv) {
		LOGGER.info("数据字典管理");
		super.createUI(mv);
		
		return admin(service.getShortName());
	}

	@GET
	@Path(list)
	@Produces(MediaType.APPLICATION_JSON)
	@MvcFilter(filters = DataBaseFilter.class)
	public String list() {
		return toJson(service.getDao().findList(null));
	}

	@GET
	@Path("getDictListByParentId/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@MvcFilter(filters = DataBaseFilter.class)
	public List<Map<String, Object>> getDictListByParentId(@PathParam(id) long pId) {
		return DataDictService.dao.findByParentId(pId);
	}

	@Override
	public String editUI(@PathParam(id) Long id, ModelAndView mv) {
		return show405;
	}

	@POST
	@MvcFilter(filters = DataBaseFilter.class)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public String create(Map<String, Object> entity) {
		return super.create(entity);
	}

	@PUT
	@MvcFilter(filters = DataBaseFilter.class)
	@Path(Constant.idInfo)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public String update(@PathParam(id) Long id, Map<String, Object> entity) {
		return super.update(id, entity);
	}

	@DELETE
	@Path(Constant.idInfo)
	@MvcFilter(filters = DataBaseFilter.class)
	@Produces(MediaType.APPLICATION_JSON)
	public String delete(@PathParam(id) Long id) {
		return delete(id, new HashMap<String, Object>());
	}

	@Override
	public IBaseService<Map<String, Object>> getService() {
		return service;
	}
}
