package com.ajaxjs.shop.controller;

import java.util.List;

import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
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
import com.ajaxjs.shop.dep.WxPayService;
import com.ajaxjs.shop.model.OrderInfo;
import com.ajaxjs.shop.model.OrderItem;
import com.ajaxjs.shop.service.OrderService;
import com.ajaxjs.user.controller.BaseUserController;
import com.ajaxjs.user.filter.LoginCheck;
import com.ajaxjs.user.model.User;
import com.ajaxjs.user.service.UserService;
import com.ajaxjs.util.logger.LogHelper;

/**
 * 
 * 控制器
 */
@Bean
@Path("/admin/order")
public class OrderController extends BaseController<OrderInfo> {
	private static final LogHelper LOGGER = LogHelper.getLog(OrderController.class);

	@Resource("OrderInfoService")
	private WxPayService service;

	@Resource("autoWire:ioc.OrderService|OrderService")
	private OrderService orderSercice;

	/////////// 前台 //////////////
	@GET
	@Path("/shop/order")
	@MvcFilter(filters = { LoginCheck.class, DataBaseFilter.class })
	public String account(ModelAndView mv) {
		LOGGER.info("浏览我的订单");
		return jsp("shop/order");
	}

	@Path("/shop/order/checkout")
	@GET
	@MvcFilter(filters = { LoginCheck.class, DataBaseFilter.class })
	public String checkout(ModelAndView mv) {
		LOGGER.info("下单");
		orderSercice.showCheckout(mv);
		return jsp("shop/checkout");
	}

	@POST
	@Path("/shop/order")
	@MvcFilter(filters = { LoginCheck.class, DataBaseFilter.class })
	@Produces(MediaType.APPLICATION_JSON)
	public String processOrder(@FormParam("addressId") long addressId, String _cartIds) {
		LOGGER.info("处理订单");
		String[] cartIds = _cartIds.split("|");
		orderSercice.processOrder(BaseUserController.getUserId(), addressId, cartIds);
		return "";
	}

	/////////// 后台 //////////////

	@GET
	@Path(list)
	@MvcFilter(filters = { LoginCheck.class, DataBaseFilter.class })
	public String list(@QueryParam(start) int start, @QueryParam(limit) int limit, ModelAndView mv) {
		LOGGER.info("后台-订单列表");

		page(mv, service.findPagedList(start, limit, null), CommonConstant.UI_ADMIN);
		return jsp("shop/order-admin-list");
	}

	@Override
	public void prepareData(ModelAndView mv) {
		super.prepareData(mv);
		mv.put("TradeStatusDict", ShopConstant.TradeStatus);
		mv.put("PayTypeDict", ShopConstant.PayType);
		mv.put("PayStatusDict", ShopConstant.PayStatus);
	}

	@Resource("UserService")
	private UserService userService;

	@GET
	@MvcFilter(filters = { LoginCheck.class, DataBaseFilter.class })
	@Path(idInfo)
	@Override
	public String editUI(@PathParam(id) Long id, ModelAndView mv) {
		super.editUI(id, mv);

		// 获取用户名
		OrderInfo order = (OrderInfo) mv.get("info");
		User buyer = userService.findById(order.getBuyerId());
		if (buyer == null) {
			// 用户已经注销或删除
			mv.put("userName", null);
		} else {
			mv.put("userName", buyer.getUsername() == null ? buyer.getName() : buyer.getUsername());
		}

		// 订单明细
		List<OrderItem> items = WxPayService.dao.findOrderItemListByOrderId(id);
		mv.put("orderItems", items);

		return jsp("shop/order-edit");
	}

	@PUT
	@MvcFilter(filters = { LoginCheck.class, DataBaseFilter.class })
	@Path(idInfo)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public String update(@PathParam(id) Long id, OrderInfo entity) {
		return super.update(id, entity);
	}

	@DELETE
	@MvcFilter(filters = { LoginCheck.class, DataBaseFilter.class })
	@Path(idInfo)
	@Produces(MediaType.APPLICATION_JSON)
	public String delete(@PathParam(id) Long id) {
		return delete(id, new OrderInfo());
	}

	@Override
	public IBaseService<OrderInfo> getService() {
		return service;
	}
}
