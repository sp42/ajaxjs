package com.ajaxjs.user.controller;

import java.util.Map;
import java.util.HashMap;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;

import com.ajaxjs.framework.service.ServiceException;
import com.ajaxjs.ioc.Bean;
import com.ajaxjs.ioc.Resource;
import com.ajaxjs.mvc.ModelAndView;
import com.ajaxjs.simpleApp.CommonController;
import com.ajaxjs.simpleApp.CommonEntryAdminController;
import com.ajaxjs.user.role.service.UserRoleUserService;

/**
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
@Path("/admin/user_admin/")
@Bean("UserRoleUserController")
public class UserRoleUserController extends CommonController<Map<String, Object>, Integer, UserRoleUserService>
		implements CommonEntryAdminController<Map<String, Object>, Integer> {
	@Resource("UserRoleUserService")
	private UserRoleUserService service;

	@GET
	@Path("list")
	@Override
	public String list(@QueryParam("start") int start, @QueryParam("limit") int limit, ModelAndView model) throws ServiceException {
		super.list(start, limit, model);
		return jsp_perfix + "user-admin/user-list";
	}

	@GET
	@Override
	public String createUI(ModelAndView model) {
		super.createUI(model);
		return jsp_perfix + "user-admin/user";
	}

	@Override
	public String editUI(Integer id, ModelAndView model) {
		return show405;
	}

	@POST
	@Override
	public String create(Map<String, Object> entity, ModelAndView model) throws ServiceException {
		return super.create(entity, model);
	}

	@PUT
	@Path("{id}")
	@Override
	public String update(@PathParam("id") Integer id, Map<String, Object> entity, ModelAndView model) throws ServiceException {
		return super.update(id, entity, model);
	}

	@DELETE
	@Path("{id}")
	@Override
	public String delete(@PathParam("id") Integer id, ModelAndView model) throws ServiceException {
		Map<String, Object> entity = new HashMap<>();
		entity.put("id", id);
		return super.delete(entity, model);
	}
}
