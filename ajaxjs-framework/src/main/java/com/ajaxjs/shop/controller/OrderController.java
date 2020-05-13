package com.ajaxjs.shop.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;

import com.ajaxjs.framework.BaseController;
import com.ajaxjs.framework.IBaseService;
import com.ajaxjs.framework.filter.DataBaseFilter;
import com.ajaxjs.ioc.Bean;
import com.ajaxjs.ioc.Resource;
import com.ajaxjs.mvc.ModelAndView;
import com.ajaxjs.mvc.filter.MvcFilter;
import com.ajaxjs.shop.ShopConstant;
import com.ajaxjs.shop.model.OrderInfo;
import com.ajaxjs.shop.model.OrderItem;
import com.ajaxjs.shop.payment.ali.Alipay;
import com.ajaxjs.shop.payment.wechat.WxPay;
import com.ajaxjs.shop.payment.wechat.WxPayService;
import com.ajaxjs.shop.payment.wechat.model.PerpayReturn;
import com.ajaxjs.shop.service.OrderService;
import com.ajaxjs.user.controller.BaseUserController;
import com.ajaxjs.user.filter.LoginCheck;
import com.ajaxjs.user.service.UserAddressService;
import com.ajaxjs.util.logger.LogHelper;
import com.alipay.api.AlipayApiException;

/**
 * 
 * 控制器
 */
@Bean
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
		mv.put(PAGE_RESULT, service.findPagedList(start, limit, 0, 0, null, BaseUserController.getUserId()));
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
		List<OrderItem> items = WxPayService.dao.findOrderItemListByOrderId(id);
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
	@MvcFilter(filters = { LoginCheck.class, DataBaseFilter.class })
//	@Produces(MediaType.APPLICATION_JSON)
	public String processOrder(@FormParam("addressId") @NotNull long addressId, @FormParam("payType") int payType,
			@FormParam("cartIds") @NotNull String _cartIds, HttpServletRequest r) throws AlipayApiException {
		LOGGER.info("处理订单 结账");

		UserAddressService.initData(r);
		String[] cartIds = _cartIds.split(",");
		OrderInfo order = service.processOrder(BaseUserController.getUserId(), addressId, cartIds, payType);
		service.onProcessOrderDone(order);

		if (payType != 0) {
			switch (payType) {
			case ShopConstant.ALI_PAY:
				order.setPayType(ShopConstant.ALI_PAY);
				LOGGER.info("进行支付");

				Alipay alipay = new Alipay();
				alipay.setSubject("支付我们的产品");
				alipay.setBody("");
				alipay.setOut_trade_no(order.getOrderNo());
				alipay.setTotal_amount(order.getTotalPrice().toString());

				return "html::" + Alipay.connect(alipay);
			}
		}

		return order != null ? jsonOk("交易成功！") : jsonNoOk("交易不成功！");
	}

	@POST
	@Path("directOrder")
	@MvcFilter(filters = { LoginCheck.class, DataBaseFilter.class })
	public String directProcessOrder(@FormParam("addressId") @NotNull long addressId, @FormParam("payType") int payType,
			@NotNull @FormParam("goodsId") long goodsId, @NotNull @FormParam("formatId") long formatId,
			@NotNull @FormParam("goodsNumber") int goodsNumber, HttpServletRequest r, HttpServletResponse response,
			ModelAndView mv) throws AlipayApiException {
		LOGGER.info("处理订单 结账-直接单个商品");

		UserAddressService.initData(r);
		OrderInfo order = service.processOrder(BaseUserController.getUserId(), addressId, goodsId, formatId,
				goodsNumber, payType);
		service.onProcessOrderDone(order);

		LOGGER.info("进行支付");
		if (payType != 0) {
			switch (payType) {
			case ShopConstant.ALI_PAY:
				order.setPayType(ShopConstant.ALI_PAY);

				Alipay alipay = new Alipay();
				alipay.setSubject("支付我们的产品");
				alipay.setBody("");
				alipay.setOut_trade_no(order.getOrderNo());
				alipay.setTotal_amount(order.getTotalPrice().toString());

				return "html::" + Alipay.connect(alipay);
			case ShopConstant.WX_PAY:
				order.setPayType(ShopConstant.WX_PAY);
				PerpayReturn p = WxPay.pcUnifiedOrder(order);

				mv.put("totalPrice", order.getTotalPrice());
				mv.put("codeUrl", p.getCode_url());

				return jsp("/shop/wxpay");
			}
		}

		return order != null ? jsonOk("交易成功！") : jsonNoOk("交易不成功！");
	}

	@Override
	public IBaseService<OrderInfo> getService() {
		return service;
	}
}
