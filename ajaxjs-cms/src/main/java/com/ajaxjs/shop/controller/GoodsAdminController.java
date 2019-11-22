package com.ajaxjs.shop.controller;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.DELETE;
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
import com.ajaxjs.framework.IBaseDao;
import com.ajaxjs.framework.IBaseService;
import com.ajaxjs.ioc.Bean;
import com.ajaxjs.ioc.Resource;
import com.ajaxjs.mvc.ModelAndView;
import com.ajaxjs.mvc.filter.DataBaseFilter;
import com.ajaxjs.mvc.filter.MvcFilter;
import com.ajaxjs.shop.model.Goods;
import com.ajaxjs.shop.model.Seller;
import com.ajaxjs.shop.service.GoodsService;
import com.ajaxjs.shop.service.SellerService;

/**
 * 
 * 控制器
 */
@Bean
@Path("/admin/goods")
public class GoodsAdminController extends BaseController<Goods> {
	@Resource("GoodsService")
	private GoodsService service;

	@GET
	@Path(list)
	@MvcFilter(filters = DataBaseFilter.class)
	public String list(@QueryParam(catalogId) int catelogId, @QueryParam(start) int start, @QueryParam(limit) int limit, ModelAndView mv) {
		prepareData(mv);
		page(mv, service.findPagedListByCatalogId(catelogId, start, limit), CommonConstant.UI_ADMIN);
		return jsp("shop/goods-admin-list");
	}

	@GET
	@Path("/listJson_format")
	@MvcFilter(filters = DataBaseFilter.class)
	@Produces(MediaType.APPLICATION_JSON)
	public String listJson(@QueryParam(start) int start, @QueryParam(limit) int limit, ModelAndView mv) {
		return toJson(service.findGoods_Format(start, limit));
	}

	@GET
	@MvcFilter(filters = DataBaseFilter.class)
	@Path(idInfo)
	@Override
	public String editUI(@PathParam(id) Long id, ModelAndView mv) {
		IBaseDao<Goods> dao = service.getDao();
		super.editUI(id, mv, dao::findById_catelog_avatar);
		return editUI();
	}
	
	@GET
	@MvcFilter(filters = DataBaseFilter.class)
	@Path("getJson/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public String getInfo(@PathParam(id) Long id) {
		return "json::" + toJson(GoodsService.dao.findById(id), false);
	}

	@GET
	@Override
	public String createUI(ModelAndView mv) {
		super.createUI(mv);
		return editUI();
	}

	@POST
	@MvcFilter(filters = DataBaseFilter.class)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public String create(Goods entity) {
		return super.create(entity);
	}

	@PUT
	@MvcFilter(filters = DataBaseFilter.class)
	@Path(idInfo)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public String update(@PathParam(id) Long id, Goods entity) {
		return super.update(id, entity);
	}

	@DELETE
	@MvcFilter(filters = DataBaseFilter.class)
	@Path(idInfo)
	@Produces(MediaType.APPLICATION_JSON)
	public String delete(@PathParam(id) Long id) {
		return delete(id, new Goods());
	}

	@Override
	public IBaseService<Goods> getService() {
		return service;
	}

	@Resource("SellerService")
	private SellerService sellerService;
	
	@Override
	public void prepareData(ModelAndView mv) {
		// 商家数据，记录不多，可以这样做
		Map<Long, Seller> map = new HashMap<>();
		sellerService.findList().forEach(seller -> map.put(seller.getId(), seller));
		mv.put("sellers", map);
		mv.put(domainCatalog_Id, service.getDomainCatalogId());
		super.prepareData(mv);
	}

}
