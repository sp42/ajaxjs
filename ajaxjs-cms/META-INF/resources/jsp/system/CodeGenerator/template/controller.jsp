<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>package ${packageName}.controller;

import java.util.Map;
import java.util.HashMap;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;


import com.ajaxjs.ioc.Bean;
import com.ajaxjs.ioc.Resource;
import com.ajaxjs.mvc.ModelAndView;
import com.ajaxjs.mvc.filter.DataBaseFilter;
import com.ajaxjs.mvc.filter.MvcFilter;
import com.ajaxjs.cms.controller.CommonController;
import com.ajaxjs.cms.controller.CommonEntryAdminController;
import ${packageName}.service.${beanName}Service;

/**
 * 
 * 控制器
 */
@Bean
@Path("/admin/user_admin/privilege")
public class ${beanName}Controller extends CommonController<${beanName}, Long> implements CommonEntryAdminController<${beanName}, Long> {
	@Resource("${beanName}Service")
	private ${beanName}Service service;
	
	{
		setTableName("${tableName}");
		setUiName("专题");
	}
	
	@GET
	@Path("list")
	@MvcFilter(filters = DataBaseFilter.class)
	@Override
	public String list(@QueryParam("start") int start, @QueryParam("limit") int limit, ModelAndView mv) {
		list(start, limit, mv, service);
		return adminList();
	}

	@GET
	@MvcFilter(filters = DataBaseFilter.class)
	@Override
	public String createUI(ModelAndView mv) {
		return super.createUI(mv);
	}

	@GET
	@Path("{id}")
	@MvcFilter(filters = DataBaseFilter.class)
	@Override
	public String editUI(@PathParam("id") Long id, ModelAndView mv) {
		return editUI(id, mv, service);
	}

	@POST
	@MvcFilter(filters = DataBaseFilter.class)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public String create(${beanName} entry) {
		return create(entry, service);
	}

	@PUT
	@MvcFilter(filters = DataBaseFilter.class)
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public String update(@PathParam("id") Long id, ${beanName} entry) {
		return update(id, entry, service);
	}

	@DELETE
	@MvcFilter(filters = DataBaseFilter.class)
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public String delete(@PathParam("id") Long id) {
		return delete(id, new ${beanName}(), service);
	}
}
