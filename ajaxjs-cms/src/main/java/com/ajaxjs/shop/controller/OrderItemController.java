package com.ajaxjs.shop.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
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
import com.ajaxjs.mvc.filter.XslMaker;
import com.ajaxjs.shop.ShopConstant;
import com.ajaxjs.shop.model.OrderItem;
import com.ajaxjs.shop.model.Seller;
import com.ajaxjs.shop.service.OrderItemService;

/**
 * 
 * 控制器
 */
@Bean
@Path("/admin/orderItem")
public class OrderItemController extends BaseController<OrderItem> {
	@Resource("OrderItemService")
	private OrderItemService service;

	@GET
	@Path(list)
	@MvcFilter(filters = { DataBaseFilter.class, XslMaker.class })
	public String list(@QueryParam(start) int start, @QueryParam(limit) int limit, ModelAndView mv,
			HttpServletRequest r, HttpServletResponse response) {
		HttpSession session = r.getSession();
		long p = (long) session.getAttribute("privilegeTotal");
		long sellerId = session.getAttribute("sellerId") == null ? 0 : (long) session.getAttribute("sellerId");

		page(mv, service.findPagedList(start, limit, p, sellerId));

		mv.put(XslMaker.XSL_TEMPLATE_PATH, service.getUiName());

		return jsp("shop/order-item-admin-list");
	}

	@Override
	public void prepareData(ModelAndView mv) {
		super.prepareData(mv);
		// 商家数据，记录不多，可以这样做
		Map<Long, Seller> map = new HashMap<>();
		SellerController.SellerService.dao.findList(null).forEach(seller -> map.put(seller.getId(), seller));

		mv.put("sellers", map);
		mv.put("TradeStatusDict", ShopConstant.TradeStatus);
		mv.put("PayTypeDict", ShopConstant.PayType);
		mv.put("PayStatusDict", ShopConstant.PayStatus);
	}

	@GET
	@MvcFilter(filters = DataBaseFilter.class)
	@Path(idInfo)
	public String editUI(@PathParam(id) Long id, ModelAndView mv) {
		return editUI(mv, service.findById(id));
	}

	@PUT
	@MvcFilter(filters = DataBaseFilter.class)
	@Path(idInfo)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public String update(@PathParam(id) Long id, OrderItem entity) {
		return super.update(id, entity);
	}

	@DELETE
	@MvcFilter(filters = DataBaseFilter.class)
	@Path(idInfo)
	@Produces(MediaType.APPLICATION_JSON)
	public String delete(@PathParam(id) Long id) {
		return delete(id, new OrderItem());
	}

	@Override
	public IBaseService<OrderItem> getService() {
		return service;
	}
}
