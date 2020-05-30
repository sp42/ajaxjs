package com.ajaxjs.shop.controller;

import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

import com.ajaxjs.app.catalog.CatalogService;
import com.ajaxjs.framework.BaseController;
import com.ajaxjs.framework.CommonConstant;
import com.ajaxjs.framework.IBaseService;
import com.ajaxjs.framework.filter.DataBaseFilter;
import com.ajaxjs.ioc.Bean;
import com.ajaxjs.ioc.Resource;
import com.ajaxjs.mvc.ModelAndView;
import com.ajaxjs.mvc.filter.MvcFilter;
import com.ajaxjs.user.model.User;
import com.ajaxjs.user.role.RoleService;
import com.ajaxjs.user.service.UserService;
import com.ajaxjs.util.logger.LogHelper;

/**
 * 用户系统后台部分的控制器
 */
@Bean
@Path("/admin/shop-user")
public class ShopUserAdminController extends BaseController<User> {
	private static final LogHelper LOGGER = LogHelper.getLog(ShopUserAdminController.class);
	
	@Resource("UserService")
	private UserService service;
	
	@Resource("UserRoleService")
	private RoleService roleService;

	@GET
	@MvcFilter(filters = DataBaseFilter.class)
	public String list(@QueryParam(START) int start, @QueryParam(LIMIT) int limit, ModelAndView mv) {
		LOGGER.info("后台-商城会员列表");
		List<Map<String, Object>> userGroups = roleService.getDao().findList(null);
		
		mv.put("UserGroups", CatalogService.list2map_id_as_key(userGroups));
		mv.put("UserGroupsJSON", toJson(userGroups, false).replaceAll("\"", "'"));
		
		page(mv, service.findPagedList(start, limit), CommonConstant.UI_ADMIN);

		return jsp("shop/shop-user-admin-list");
	}


	@Override
	public IBaseService<User> getService() {
		return service;
	}
}
