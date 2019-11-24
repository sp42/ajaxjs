package com.ajaxjs.shop.controller;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.ajaxjs.cms.app.CommonConstant;
import com.ajaxjs.framework.BaseController;
import com.ajaxjs.framework.IBaseService;
import com.ajaxjs.ioc.Bean;
import com.ajaxjs.ioc.Resource;
import com.ajaxjs.mvc.ModelAndView;
import com.ajaxjs.mvc.filter.DataBaseFilter;
import com.ajaxjs.mvc.filter.MvcFilter;
import com.ajaxjs.shop.ShopConstant;
import com.ajaxjs.shop.model.Group;
import com.ajaxjs.shop.model.Seller;
import com.ajaxjs.shop.service.GroupService;
import com.ajaxjs.shop.service.SellerService;
import com.ajaxjs.util.map.JsonHelper;

/**
 * 
 * 控制器
 */
@Bean
@Path("/admin/simple-group")
public class GroupController extends BaseController<Group> {
	@Resource("GroupService")
	private GroupService service;

	@GET
	@Path(list)
	@MvcFilter(filters = DataBaseFilter.class)
	public String list(@QueryParam(start) int start, @QueryParam(limit) int limit, ModelAndView mv) {
		page(mv, service.findPagedList(start, limit, null), CommonConstant.UI_ADMIN);
		return jsp("");
	}
	
	@GET
	@Path("/listJson")
	@MvcFilter(filters = DataBaseFilter.class)
	@Produces(MediaType.APPLICATION_JSON)
	public String listJson(@QueryParam(start) int start, @QueryParam(limit) int limit, ModelAndView mv) {
		return toJson(page(mv, service.findPagedList(start, limit, null), CommonConstant.UI_ADMIN));
	}

	@GET
	@MvcFilter(filters = DataBaseFilter.class)
	@Path(idInfo)
	@Override
	public String editUI(@PathParam(id) Long id, ModelAndView mv) {
		super.editUI(id, mv);
		return editUI();
	}

	@GET
	@Override
	public String createUI(ModelAndView mv) {
		super.createUI(mv);
		return editUI();
	}

	@POST
	@MvcFilter(filters = DataBaseFilter.class)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public String create(Group entity) {
		System.out.println(entity.getEndTime());
		return super.create(entity);
	}

	@PUT
	@MvcFilter(filters = DataBaseFilter.class)
	@Path(idInfo)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public String update(@PathParam(id) Long id, Group entity) {
		System.out.println(entity.getEndTime());
		return super.update(id, entity);
	}

	@DELETE
	@MvcFilter(filters = DataBaseFilter.class)
	@Path(idInfo)
	@Produces(MediaType.APPLICATION_JSON)
	public String delete(@PathParam(id) Long id) {
		return delete(id, new Group());
	}

	@Resource("SellerService")
	private SellerService sellerService;
	
	@Override
	public void prepareData(ModelAndView mv) {
		Map<Long, Seller> map = new HashMap<>();
		sellerService.findList().forEach(seller -> map.put(seller.getId(), seller));
		mv.put("sellers", map);
		
		mv.put("statusMap", ShopConstant.GroupStatus);
		mv.put("statusJSON", JsonHelper.toJson(ShopConstant.GroupStatus).replaceAll("\\\"", "'"));
		super.prepareData(mv);
	}

	@Override
	public IBaseService<Group> getService() {
		return service;
	}
}
