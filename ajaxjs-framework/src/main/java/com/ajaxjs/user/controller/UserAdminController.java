package com.ajaxjs.user.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.ajaxjs.app.CommonConstant;
import com.ajaxjs.app.catalog.CatalogService;
import com.ajaxjs.framework.BaseController;
import com.ajaxjs.framework.IBaseService;
import com.ajaxjs.framework.filter.DataBaseFilter;
import com.ajaxjs.ioc.Bean;
import com.ajaxjs.ioc.Resource;
import com.ajaxjs.mvc.ModelAndView;
import com.ajaxjs.mvc.filter.MvcFilter;
import com.ajaxjs.user.UserDict;
import com.ajaxjs.user.filter.LoginCheck;
import com.ajaxjs.user.model.User;
import com.ajaxjs.user.role.RoleService;
import com.ajaxjs.user.service.UserService;
import com.ajaxjs.util.logger.LogHelper;

/**
 * 用户系统后台部分的控制器
 */
@Bean
@Path("/admin/user")
public class UserAdminController extends BaseController<User> {
	private static final LogHelper LOGGER = LogHelper.getLog(UserAdminController.class);
	
	@Resource("UserService")
	private UserService service;

	@Resource("UserRoleService")
	private RoleService roleService;

	@GET
	@Path(list)
	@MvcFilter(filters = DataBaseFilter.class)
	public String list(@QueryParam(start) int start, @QueryParam(limit) int limit, ModelAndView mv) {
		LOGGER.info("后台-会员列表");
		
		List<Map<String, Object>> userGroups = roleService.getDao().findList(null);
		
		mv.put("SexGender", UserDict.SEX_GENDER);
		mv.put("UserGroups", CatalogService.list2map_id_as_key(userGroups));
		mv.put("UserGroupsJSON", toJson(userGroups, false).replaceAll("\"", "'"));

		page(mv, service.findPagedList(start, limit), CommonConstant.UI_ADMIN);

		return jsp("user/user-admin-list");
	}

	@GET
	@MvcFilter(filters = DataBaseFilter.class)
	@Path(idInfo)
	public String editUI(@PathParam(id) Long id, ModelAndView mv) {
		mv.put("UserGroupsJSON", toJson(roleService.getDao().findList(null), false).replaceAll("\"", "'"));
		mv.put("SexGender", UserDict.SEX_GENDER);
		editUI(mv, service.findById(id));

		return jsp("user/user-edit");
	}

	@GET
	@Override
	public String createUI(ModelAndView mv) {
		mv.put("SexGender", UserDict.SEX_GENDER);
		super.createUI(mv);

		return editUI();
	}

	@POST
	@MvcFilter(filters = DataBaseFilter.class)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public String create(User entity) {
		return super.create(entity);
	}

	@PUT
	@MvcFilter(filters = DataBaseFilter.class)
	@Path(idInfo)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public String update(@PathParam(id) Long id, User entity) {
		return super.update(id, entity);
	}

	@DELETE
	@MvcFilter(filters = DataBaseFilter.class)
	@Path(idInfo)
	@Produces(MediaType.APPLICATION_JSON)
	public String delete(@PathParam(id) Long id) {
		return delete(id, new User());
	}
	
	@GET
	@Path("account-center")
	@MvcFilter(filters = { LoginCheck.class, DataBaseFilter.class })
	public String accountCenter(ModelAndView mv, HttpServletRequest r) {
		LOGGER.info("后台-账号中心");
		
		mv.put("UserGroups", CatalogService.list2map_id_as_key(RoleService.dao.findList(null)));
		mv.put("info", service.findById(BaseUserController.getUserId(r)));
		
		return jsp("admin/account-center");
	}
	@GET
	@Path("profile")
	@MvcFilter(filters = { LoginCheck.class, DataBaseFilter.class })
	public String profile(ModelAndView mv, HttpServletRequest r) {
		LOGGER.info("后台-个人信息");
		
		mv.put("info", service.findById(BaseUserController.getUserId(r)));
		return jsp("admin/user-profile");
	}

	@Override
	public IBaseService<User> getService() {
		return service;
	}
}
