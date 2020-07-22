package com.ajaxjs.shop.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;

import com.ajaxjs.framework.BaseController;
import com.ajaxjs.framework.ServiceException;
import com.ajaxjs.framework.filter.DataBaseFilter;
import com.ajaxjs.shop.ShopConstant;
import com.ajaxjs.shop.model.OrderInfo;
import com.ajaxjs.shop.model.OrderItem;
import com.ajaxjs.shop.service.OrderService;
import com.ajaxjs.shop.service.Pay;
import com.ajaxjs.sql.orm.IBaseService;
import com.ajaxjs.user.controller.BaseUserController;
import com.ajaxjs.user.filter.LoginCheck;
import com.ajaxjs.user.service.UserAddressService;
import com.ajaxjs.util.ioc.Component;
import com.ajaxjs.util.ioc.Resource;
import com.ajaxjs.util.logger.LogHelper;
import com.ajaxjs.web.mvc.ModelAndView;
import com.ajaxjs.web.mvc.controller.MvcRequest;
import com.ajaxjs.web.mvc.filter.MvcFilter;
import com.alipay.api.AlipayApiException;

/**
 * 
 * 控制器
 */
@Component
@Path("/shop/order")
public class OrderController extends BaseController<OrderInfo> {
	private static final LogHelper LOGGER = LogHelper.getLog(OrderController.class);

	@Resource("autoWire:ioc.OrderService|OrderService")
	private OrderService service;

	/////////// 前台 //////////////
	@GET
	@MvcFilter(filters = { LoginCheck.class, DataBaseFilter.class })
	public String account(@QueryParam(START) int start, @QueryParam(LIMIT) int limit, ModelAndView mv) {
		LOGGER.info("浏览我的订单");

		prepareData(mv);
		mv.put(PAGE_RESULT, service.findPagedList(start, limit, BaseUserController.getUserId()));
		return jsp("shop/order");
	}

	@GET
	@Path(ID_INFO)
	@MvcFilter(filters = { LoginCheck.class, DataBaseFilter.class })
	public String orderDetail(@PathParam(ID) Long id, ModelAndView mv) {
		LOGGER.info("浏览我的订单明细");

		prepareData(mv);
		mv.put(INFO, service.findById(id));

		// 订单明细
		List<OrderItem> items = OrderService.dao.findOrderItemListByOrderId(id);
		mv.put("orderItems", items);

		return jsp("shop/order-info");
	}

	@Override
	public void prepareData(ModelAndView mv) {
		super.prepareData(mv);
		mv.put("TradeStatusDict", ShopConstant.TradeStatus);
		mv.put("PayTypeDict", ShopConstant.PAY_TYPE);
		mv.put("PayStatusDict", ShopConstant.PayStatus);
	}

	@Path("checkout")
	@GET
	@MvcFilter(filters = { LoginCheck.class, DataBaseFilter.class })
	public String checkout(ModelAndView mv, @QueryParam("goodsId") long goodsId) {
		LOGGER.info("下单");

		service.showCheckout(mv);
		return jsp("shop/checkout");
	}

	@POST
	@Path("miniApp")
	@MvcFilter(filters = { LoginCheck.class, DataBaseFilter.class })
	public String processOrder_MiniApp(@FormParam("addressId") @NotNull long addressId, @FormParam("cartIds") @NotNull String _cartIds, MvcRequest r, ModelAndView mv) {
		LOGGER.info("处理订单 结账");

		UserAddressService.initData(r);
		String[] cartIds = _cartIds.split("_");
		OrderInfo order = service.processOrder(BaseUserController.getUserId(), addressId, cartIds, ShopConstant.WX_PAY);
		service.onProcessOrderDone(order);

		return toJson(Pay.miniAppPay(BaseUserController.getUserId(), order, r.getIp()));
	}

	@POST
	@MvcFilter(filters = { LoginCheck.class, DataBaseFilter.class })
	public String processOrder(@FormParam("addressId") @NotNull long addressId, @NotNull @FormParam("payType") int payType, @FormParam("cartIds") @NotNull String _cartIds,
			HttpServletRequest r, ModelAndView mv) throws ServiceException, AlipayApiException {
		LOGGER.info("处理订单 结账");

		UserAddressService.initData(r);
		String[] cartIds = _cartIds.split(",");
		OrderInfo order = service.processOrder(BaseUserController.getUserId(), addressId, cartIds, payType);
		service.onProcessOrderDone(order);

		return Pay.doPay(order, mv);
	}

	@POST
	@Path("directOrder")
	@MvcFilter(filters = { LoginCheck.class, DataBaseFilter.class })
	public String directProcessOrder(@FormParam("addressId") @NotNull long addressId, @NotNull @FormParam("payType") int payType, @NotNull @FormParam("goodsId") long goodsId,
			@NotNull @FormParam("formatId") long formatId, @NotNull @FormParam("goodsNumber") int goodsNumber, HttpServletRequest r, ModelAndView mv)
			throws ServiceException, AlipayApiException {
		LOGGER.info("处理订单 结账-直接单个商品");

		UserAddressService.initData(r);
		OrderInfo order = service.processOrder(BaseUserController.getUserId(), addressId, goodsId, formatId, goodsNumber, payType);
		service.onProcessOrderDone(order);

		return Pay.doPay(order, mv);
	}

	@Override
	public IBaseService<OrderInfo> getService() {
		return service;
	}
}
