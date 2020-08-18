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
import com.ajaxjs.framework.filter.DataBaseFilter;
import com.ajaxjs.framework.filter.XslMaker;
import com.ajaxjs.shop.ShopConstant;
import com.ajaxjs.shop.model.OrderItem;
import com.ajaxjs.shop.model.Seller;
import com.ajaxjs.shop.service.OrderItemService;
import com.ajaxjs.sql.orm.IBaseService;
import com.ajaxjs.util.ioc.Component;
import com.ajaxjs.util.ioc.Resource;
import com.ajaxjs.web.mvc.ModelAndView;
import com.ajaxjs.web.mvc.filter.MvcFilter;

/**
 * 
 * 控制器
 */
@Component
@Path("/admin/orderItem")
public class OrderItemController extends BaseController<OrderItem> {
	@Resource("OrderItemService")
	private OrderItemService service;

	@GET
	@Path(LIST)
	@MvcFilter(filters = { DataBaseFilter.class, XslMaker.class })
	public String list(@QueryParam(START) int start, @QueryParam(LIMIT) int limit, ModelAndView mv, HttpServletRequest r, HttpServletResponse response) {
		HttpSession session = r.getSession();
		long p = (long) session.getAttribute("privilegeTotal");
		long sellerId = session.getAttribute("sellerId") == null ? 0 : (long) session.getAttribute("sellerId");

		mv.put(XslMaker.XSL_TEMPLATE_PATH, service.getUiName());

		return output(mv, service.findPagedList(start, limit, p, sellerId), "jsp::shop/order-item-admin-list");
	}

	@Override
	public void prepareData(ModelAndView mv) {
		super.prepareData(mv);
		// 商家数据，记录不多，可以这样做
		Map<Long, Seller> map = new HashMap<>();
		SellerController.SellerService.dao.findList(null).forEach(seller -> map.put(seller.getId(), seller));

		mv.put("sellers", map);
		mv.put("TradeStatusDict", ShopConstant.TradeStatus);
		mv.put("PayTypeDict", ShopConstant.PAY_TYPE);
		mv.put("PayStatusDict", ShopConstant.PayStatus);
	}

	@GET
	@MvcFilter(filters = DataBaseFilter.class)
	@Path(ID_INFO)
	public String editUI(@PathParam(ID) Long id, ModelAndView mv) {
		return output(mv, service.findById(id), "jsp::shop/orderItem-edit");
	}

	@PUT
	@MvcFilter(filters = DataBaseFilter.class)
	@Path(ID_INFO)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public String update(@PathParam(ID) Long id, OrderItem entity) {
		return super.update(id, entity);
	}

	@DELETE
	@MvcFilter(filters = DataBaseFilter.class)
	@Path(ID_INFO)
	@Produces(MediaType.APPLICATION_JSON)
	public String delete(@PathParam(ID) Long id) {
		return delete(id, new OrderItem());
	}

	@Override
	public IBaseService<OrderItem> getService() {
		return service;
	}
}
