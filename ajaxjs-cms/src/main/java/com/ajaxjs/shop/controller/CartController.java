
package com.ajaxjs.shop.controller;

import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.ajaxjs.framework.BaseController;
import com.ajaxjs.framework.IBaseService;
import com.ajaxjs.framework.filter.DataBaseFilter;
import com.ajaxjs.ioc.Bean;
import com.ajaxjs.ioc.Resource;
import com.ajaxjs.mvc.ModelAndView;
import com.ajaxjs.mvc.filter.MvcFilter;
import com.ajaxjs.shop.model.Cart;
import com.ajaxjs.shop.service.CartService;
import com.ajaxjs.user.controller.BaseUserController;
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

	// ------------- 前台 ---------

	@GET
	@Path("/shop/cart")
	@MvcFilter(filters = { LoginCheck.class, DataBaseFilter.class })
	public String account(ModelAndView mv) {
		LOGGER.info("浏览我的购物车");
		return jsp("shop/cart");
	}

	@POST
	@Path("/shop/cart")
	@MvcFilter(filters = { LoginCheck.class, DataBaseFilter.class })
	public String add(Cart cart) {
		LOGGER.info("添加购物车");
		cart.setUserId(BaseUserController.getUserId());
		create(cart);

		return jsonOk("添加购物车");
	}

	@GET
	@Path("/shop/cart/mycartlist")
	@MvcFilter(filters = { LoginCheck.class, DataBaseFilter.class })
	@Produces(MediaType.APPLICATION_JSON)
	public String findMyCartList() {
		LOGGER.info("列出用户的购物车列表");
		return toJson(service.findListByUserId(BaseUserController.getUserId()));
	}

	@POST
	@MvcFilter(filters = { LoginCheck.class, DataBaseFilter.class })
	@Path("/shop/cart/updateGoodsNumber/" + idInfo)
	@Produces(MediaType.APPLICATION_JSON)
	public String updateGoodsNumber(@PathParam(id) Long id, @FormParam("goodsNumber") int goodsNumber) {
		LOGGER.info("修改货品数量");

		if (CartService.dao.updateGoodsNumber(goodsNumber, id, BaseUserController.getUserId()) >= 1)
			return jsonOk("修改货品数量成功");
		else
			return jsonNoOk("修改货品数量失败！");

	}

	@DELETE
	@MvcFilter(filters = { LoginCheck.class, DataBaseFilter.class })
	@Path("/shop/cart/" + idInfo)
	@Produces(MediaType.APPLICATION_JSON)
	public String deleteMyCart(@PathParam(id) Long id) {
		LOGGER.info("删除用户的购物车");

		if (CartService.dao.deleteMyCart(id, BaseUserController.getUserId()))
			return jsonOk("删除成功");
		else
			return jsonNoOk("删除失败！");

	}

	// ------------- 后台 ---------

	/**
	 * 列出所有
	 */
	@GET
	@Path("list")
	@MvcFilter(filters = DataBaseFilter.class)
	public String list(@QueryParam(start) int start, @QueryParam(limit) int limit, @QueryParam("userId") long userId,
			ModelAndView mv) {
		LOGGER.info("后台-购物车-列表");
		page(mv, service.findPagedList(start, limit, userId));
		return jsp("shop/cart-admin-list");
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
