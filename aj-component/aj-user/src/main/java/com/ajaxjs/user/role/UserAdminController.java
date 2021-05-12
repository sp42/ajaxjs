package com.ajaxjs.user.role;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

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

import com.ajaxjs.entity.filter.DataBaseFilter;
import com.ajaxjs.entity.service.TreeLikeService;
import com.ajaxjs.framework.BaseController;
import com.ajaxjs.framework.BaseService;
import com.ajaxjs.framework.QueryTools;
import com.ajaxjs.sql.orm.IBaseService;
import com.ajaxjs.user.User;
import com.ajaxjs.user.UserConstant;
import com.ajaxjs.user.filter.LoginCheck;
import com.ajaxjs.user.login.LoginController;
import com.ajaxjs.user.profile.ProfileService;
import com.ajaxjs.util.ioc.Component;
import com.ajaxjs.util.ioc.ComponentMgr;
import com.ajaxjs.util.ioc.Resource;
import com.ajaxjs.util.logger.LogHelper;
import com.ajaxjs.web.mvc.ModelAndView;
import com.ajaxjs.web.mvc.filter.MvcFilter;

/**
 * 用户系统后台部分的控制器
 */
@Component
@Path("/admin/user")
public class UserAdminController extends BaseController<User> {
	private static final LogHelper LOGGER = LogHelper.getLog(UserAdminController.class);

	@Resource
	private ProfileService service;

	@Resource("UserRoleService")
	private RoleService roleService;

	@GET
	@Path(LIST)
	@MvcFilter(filters = DataBaseFilter.class)
	public String list(@QueryParam(START) int start, @QueryParam(LIMIT) int limit, ModelAndView mv) {
		LOGGER.info("后台-会员列表");

		List<Map<String, Object>> userGroups = roleService.getDao().findList(null);

		mv.put("UserGroupsJSON", toJson(userGroups, false, false));
		mv.put("UserGroups_IdAsKey", toJson(TreeLikeService.idAsKey(userGroups), false, false));

		// 扩展权限
		ExtendedRight extendedRight = ComponentMgr.getByInterface(ExtendedRight.class);
		if (extendedRight != null)
			mv.put("ExtendedRight", extendedRight.getRightInfos());

		return output(mv, service.findPagedList(start, limit), "jsp::user/admin/user-admin-list");
	}

	@GET
	@Path("listJson")
	@Produces(MediaType.APPLICATION_JSON)
	@MvcFilter(filters = DataBaseFilter.class)
	public String listJson(@QueryParam(START) int start, @QueryParam(LIMIT) int limit, ModelAndView mv, HttpServletRequest r,
			@QueryParam("roleId") int roleId) {
		LOGGER.info("后台-会员列表-json");
		String[] fields = { "id", "name", "username" };

		Function<String, String> fn = QueryTools.searchQuery(fields, r).andThen(BaseService::betweenCreateDate);

		if (roleId != 0)
			fn = fn.andThen(QueryTools.by("roleId", roleId));

		return toJson(service.findPagedList(start, limit, fn));
	}

	@GET
	@MvcFilter(filters = DataBaseFilter.class)
	@Path(ID_INFO)
	public String editUI(@PathParam(ID) Long id, ModelAndView mv) {
		mv.put("UserGroupsJSON", toJson(roleService.getDao().findList(null), false, false).replaceAll("\"", "'"));
		mv.put("SexGender", UserConstant.SEX_GENDER);

		return output(mv, id, "jsp::user/user-edit");
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

		mv.put("UserGroups", TreeLikeService.idAsKey(RoleService.dao.findList(null)));
		mv.put("info", service.findById(LoginController.getUserId(r)));

		return jsp("admin/account-center");
	}

	@GET
	@Path("profile")
	@MvcFilter(filters = { LoginCheck.class, DataBaseFilter.class })
	public String profile(ModelAndView mv, HttpServletRequest r) {
		LOGGER.info("后台-个人信息");

		mv.put("info", service.findById(LoginController.getUserId(r)));
		return jsp("admin/user-profile");
	}

	@Override
	public IBaseService<User> getService() {
		return service;
	}
}
