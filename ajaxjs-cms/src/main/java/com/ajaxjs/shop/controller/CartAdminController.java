package com.ajaxjs.shop.controller;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.ajaxjs.framework.BaseController;
import com.ajaxjs.framework.IBaseService;
import com.ajaxjs.framework.QueryParams;
import com.ajaxjs.ioc.Bean;
import com.ajaxjs.ioc.Resource;
import com.ajaxjs.mvc.ModelAndView;
import com.ajaxjs.mvc.filter.DataBaseFilter;
import com.ajaxjs.mvc.filter.MvcFilter;
import com.ajaxjs.shop.model.Cart;
import com.ajaxjs.shop.service.CartService;

/**
 * 
 * 控制器
 */
@Bean
@Path("/admin/cart")
public class CartAdminController extends BaseController<Cart> {
	@Resource("CartService")
	private CartService service;

	/**
	 * 列出所有
	 */
	@GET
	@Path("list")
	@MvcFilter(filters = DataBaseFilter.class)
	public String list(@QueryParam(start) int start, @QueryParam(limit) int limit, ModelAndView mv) {
		listPaged(start, limit, mv,  (s, l) -> service.findCartList(s, l, QueryParams.init()));
		return adminList();
	}

	@DELETE
	@MvcFilter(filters = DataBaseFilter.class)
	@Path(idInfo)
	@Produces(MediaType.APPLICATION_JSON)
	public String delete(@PathParam(id) Long id) {
		return delete(id, new Cart());
	}

	@Override
	public IBaseService<Cart> getService() {
		return service;
	}
}
