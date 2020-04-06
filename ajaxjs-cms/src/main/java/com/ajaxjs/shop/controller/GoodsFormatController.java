package com.ajaxjs.shop.controller;

import javax.ws.rs.DELETE;
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
import com.ajaxjs.mvc.filter.MvcFilter;
import com.ajaxjs.shop.model.GoodsFormat;
import com.ajaxjs.shop.service.GoodsFormatService;

/**
 * 
 * 控制器
 */
@Bean
@Path("/admin/goodsFormat")
public class GoodsFormatController extends BaseController<GoodsFormat> {
	@Resource("GoodsFormatService")
	private GoodsFormatService service;

	@GET
	@MvcFilter(filters = DataBaseFilter.class)
	public String listFormatByGooddId(@QueryParam("goodsId") long goodsId) {
		return toJson(service.findByGoodsId(goodsId));
	}

	@POST
	@MvcFilter(filters = DataBaseFilter.class)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public String create(GoodsFormat entity) {
		return super.create(entity);
	}

	@DELETE
	@MvcFilter(filters = DataBaseFilter.class)
	@Path(ID_INFO)
	@Produces(MediaType.APPLICATION_JSON)
	public String delete(@PathParam(ID) Long id) {
		return delete(id, new GoodsFormat());
	}

	@Override
	public IBaseService<GoodsFormat> getService() {
		return service;
	}
}
