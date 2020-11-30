package com.ajaxjs.cms.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import javax.ws.rs.BeanParam;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.ajaxjs.cms.model.Address;
import com.ajaxjs.cms.service.UserAddressService;
import com.ajaxjs.framework.BaseController;
import com.ajaxjs.framework.CommonConstant;
import com.ajaxjs.framework.filter.DataBaseFilter;
import com.ajaxjs.framework.filter.XslMaker;
import com.ajaxjs.sql.orm.IBaseService;
import com.ajaxjs.user.filter.LoginCheck;
import com.ajaxjs.user.login.LoginController;
import com.ajaxjs.util.ioc.Component;
import com.ajaxjs.util.ioc.Resource;
import com.ajaxjs.util.logger.LogHelper;
import com.ajaxjs.web.mvc.ModelAndView;
import com.ajaxjs.web.mvc.filter.MvcFilter;

/**
 * 
 * 控制器
 */
@Component
@Path("/user/address")
public class AddressController extends BaseController<Address> {
	private static final LogHelper LOGGER = LogHelper.getLog(AddressController.class);

	@Resource("UserAddressService")
	private UserAddressService service;

	static String jsp = "user/address";

	@GET
	@Path(LIST)
	@MvcFilter(filters = { LoginCheck.class, DataBaseFilter.class })
	public String list(@QueryParam(START) int start, @QueryParam(LIMIT) int limit, ModelAndView mv, HttpServletRequest r) {
		LOGGER.info("收货地址列表");
		mv.put(LIST, service.findListByUserId(LoginController.getUserId()));
		return jsp(jsp);
	}

	@GET
	@MvcFilter(filters = { LoginCheck.class })
	public String createUI(ModelAndView mv) {
		LOGGER.info("用户会员中心-我的地址");
		mv.put("isCreate", true);
		return jsp(jsp + "-info");
	}

	@GET
	@Path(ID_INFO)
	@MvcFilter(filters = { LoginCheck.class, DataBaseFilter.class })
	public String editUI(@PathParam(ID) Long id, ModelAndView mv) {
		return toJson(service.findById(id));
	}

	/*
	 * @POST
	 * 
	 * @MvcFilter(filters = { LoginCheck.class, DataBaseFilter.class })
	 * 
	 * @Produces(MediaType.APPLICATION_JSON) public String
	 * createUserAddress(@NotNull @HeaderParam(USER_ID) long userId, UserAddress
	 * entity) { entity.setUserId(userId); return create(entity, service); }
	 */
	@POST
	@MvcFilter(filters = { LoginCheck.class, DataBaseFilter.class })
	@Produces(MediaType.APPLICATION_JSON)
	public String createUserAddress(@NotNull @BeanParam Address bean) {
		bean.setOwner(LoginController.getUserId());
		return create(bean);
	}

	@PUT
	@Path(ID_INFO)
	@MvcFilter(filters = { LoginCheck.class, DataBaseFilter.class })
	@Produces(MediaType.APPLICATION_JSON)
	public String updateUserAddress(@PathParam(ID) Long id, Address bean) {
		return update(id, bean);
	}

	@DELETE
	@MvcFilter(filters = DataBaseFilter.class)
	@Path(ID_INFO)
	@Produces(MediaType.APPLICATION_JSON)
	public String delete(@PathParam(ID) Long id) {
		return delete(id, new Address());
	}

	@Override
	public IBaseService<Address> getService() {
		return service;
	}

	//////////////////// 后台 ///////////////////

	@GET
	@Path("/admin/address/list")
	@MvcFilter(filters = { DataBaseFilter.class, XslMaker.class })
	public String adminList(@QueryParam(START) int start, @QueryParam(LIMIT) int limit, @QueryParam(CATALOG_ID) int catalogId, ModelAndView mv) {
		return output(mv, service.findPagedList(catalogId, start, limit, CommonConstant.OFF_LINE, true), jsp + "-admin-list");
	}

	@DELETE
	@Path("/admin/address/{id}")
	@MvcFilter(filters = DataBaseFilter.class)
	@Produces(MediaType.APPLICATION_JSON)
	public String deleteAdmin(@PathParam(ID) Long id) {
		return delete(id);
	}
}
