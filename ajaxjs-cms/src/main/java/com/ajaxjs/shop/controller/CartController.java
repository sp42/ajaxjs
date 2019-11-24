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
import com.ajaxjs.ioc.Bean;
import com.ajaxjs.ioc.Resource;
import com.ajaxjs.mvc.ModelAndView;
import com.ajaxjs.mvc.filter.DataBaseFilter;
import com.ajaxjs.mvc.filter.MvcFilter;
import com.ajaxjs.shop.model.Cart;
import com.ajaxjs.shop.service.CartService;
import com.ajaxjs.user.filter.LoginCheck;
import com.ajaxjs.util.logger.LogHelper;

/**
 * 
 * 控制器
 */
@Bean
@Path("/admin/cart")
public class CartController extends BaseController<Cart> {
	private static final LogHelper LOGGER = LogHelper.getLog(CartController.class);

	@Resource("CartService")
	private CartService service;

	/**
	 * 列出所有
	 */
	@GET
	@Path("list")
	@MvcFilter(filters = DataBaseFilter.class)
	public String list(@QueryParam(start) int start, @QueryParam(limit) int limit, ModelAndView mv) {
		page(mv, service.findCartList(start, limit, null));
		return jsp("shop/cart-admin-list");
	}

	@DELETE
	@MvcFilter(filters = DataBaseFilter.class)
	@Path(idInfo)
	@Produces(MediaType.APPLICATION_JSON)
	public String delete(@PathParam(id) Long id) {
		return delete(id, new Cart());
	}

	@GET
	@Path("/shop/cart")
	@MvcFilter(filters = { LoginCheck.class, DataBaseFilter.class })
	public String account(ModelAndView mv) {
		LOGGER.info("浏览我的购物车");
		return jsp("shop/cart");
	}

	@Override
	public IBaseService<Cart> getService() {
		return service;
	}
}
