package com.ajaxjs.shop.controller;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

import com.ajaxjs.cms.utils.sms.SMS;
import com.ajaxjs.mvc.ModelAndView;
import com.ajaxjs.mvc.filter.DataBaseFilter;
import com.ajaxjs.mvc.filter.MvcFilter;
import com.ajaxjs.user.controller.BaseUserController;
import com.ajaxjs.user.service.UserService;
import com.ajaxjs.util.logger.LogHelper;


@Path("/shop/goods")
public class GoodsController extends BaseUserController {
	private static final LogHelper LOGGER = LogHelper.getLog(CartController.class);

	@GET
	@MvcFilter(filters = { DataBaseFilter.class })
	public String list(ModelAndView mv) {
		LOGGER.info("浏览商品");
		return jsp("shop/goods");
	}

	@Override
	public UserService getService() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SMS getSms() {
		return null;
	}

}
