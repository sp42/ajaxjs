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

import com.ajaxjs.app.catalog.CatalogService;
import com.ajaxjs.framework.BaseController;
import com.ajaxjs.framework.filter.DataBaseFilter;
import com.ajaxjs.ioc.Bean;
import com.ajaxjs.ioc.Resource;
import com.ajaxjs.mvc.ModelAndView;
import com.ajaxjs.mvc.filter.MvcFilter;
import com.ajaxjs.sql.orm.IBaseService;
import com.ajaxjs.user.UserConstant;
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
	@Path(LIST)
	@MvcFilter(filters = DataBaseFilter.class)
	public String list(@QueryParam(START) int start, @QueryParam(LIMIT) int limit, ModelAndView mv) {
		LOGGER.info("后台-会员列表");

		List<Map<String, Object>> userGroups = roleService.getDao().findList(null);

		mv.put("SexGender", UserConstant.SEX_GENDER);
		mv.put("UserGroups", CatalogService.idAsKey(userGroups));
		mv.put("UserGroupsJSON", toJson(userGroups, false).replaceAll("\"", "'"));

		return page(mv, service.findPagedList(start, limit), "user/user-admin-list");
	}

	@GET
	@MvcFilter(filters = DataBaseFilter.class)
	@Path(ID_INFO)
	public String editUI(@PathParam(ID) Long id, ModelAndView mv) {
		mv.put("UserGroupsJSON", toJson(roleService.getDao().findList(null), false).replaceAll("\"", "'"));
		mv.put("SexGender", UserConstant.SEX_GENDER);
		setInfo(mv, service.findById(id));

		return jsp("user/user-edit");
	}

	@GET
	@Override
	public String createUI(ModelAndView mv) {
		mv.put("SexGender", UserConstant.SEX_GENDER);
		return super.createUI(mv);
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
	@Path(ID_INFO)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public String update(@PathParam(ID) Long id, User entity) {
		return super.update(id, entity);
	}

	@DELETE
	@MvcFilter(filters = DataBaseFilter.class)
	@Path(ID_INFO)
	@Produces(MediaType.APPLICATION_JSON)
	public String delete(@PathParam(ID) Long id) {
		return delete(id, new User());
	}

	@GET
	@Path("account-center")
	@MvcFilter(filters = { LoginCheck.class, DataBaseFilter.class })
	public String accountCenter(ModelAndView mv, HttpServletRequest r) {
		LOGGER.info("后台-账号中心");

		mv.put("UserGroups", CatalogService.idAsKey(RoleService.dao.findList(null)));
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
