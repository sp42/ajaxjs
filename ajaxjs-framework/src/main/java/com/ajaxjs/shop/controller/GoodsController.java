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

import com.ajaxjs.app.TreeLikeService;
import com.ajaxjs.framework.BaseController;
import com.ajaxjs.framework.CommonConstant;
import com.ajaxjs.framework.config.ConfigService;
import com.ajaxjs.framework.filter.DataBaseFilter;
import com.ajaxjs.framework.filter.FrontEndOnlyCheck;
import com.ajaxjs.shop.model.Goods;
import com.ajaxjs.shop.model.Seller;
import com.ajaxjs.shop.service.GoodsService;
import com.ajaxjs.sql.orm.IBaseService;
import com.ajaxjs.user.controller.BaseUserController;
import com.ajaxjs.util.ioc.Component;
import com.ajaxjs.util.ioc.Resource;
import com.ajaxjs.util.logger.LogHelper;
import com.ajaxjs.web.mvc.ModelAndView;
import com.ajaxjs.web.mvc.filter.MvcFilter;

/**
 * 
 * 控制器
 */
@Component
@Path("/admin/goods")
public class GoodsController extends BaseController<Goods> {
	private static final LogHelper LOGGER = LogHelper.getLog(GoodsController.class);

	@Resource
	private GoodsService service;

	@Resource
	private TreeLikeService treeLikeService;

	@GET
	@Path(LIST)
	@MvcFilter(filters = DataBaseFilter.class)
	public String list(@QueryParam(CATALOG_ID) int catalogId, @QueryParam(START) int start, @QueryParam(LIMIT) int limit, @QueryParam("sellerId") int sellerId, ModelAndView mv) {
		LOGGER.info("商城-商品-后台列表");

		prepareData(mv);
		mv.put("goodsCatalogs", treeLikeService.getAllChildrenAsMap(service.getDomainCatalogId()));

		return autoOutput(service.findPagedListByCatalogId(catalogId, start, limit, CommonConstant.OFF_LINE, sellerId), mv, jsp("shop/goods-admin-list"));
	}

	@GET
	@Path("/listJson_format")
	@MvcFilter(filters = DataBaseFilter.class)
	@Produces(MediaType.APPLICATION_JSON)
	public String listJson(@QueryParam(START) int start, @QueryParam(LIMIT) int limit, ModelAndView mv) {
		return toJson(service.findGoods_Format(start, limit));
	}

	@GET
	@MvcFilter(filters = DataBaseFilter.class)
	@Path(ID_INFO)
	public String editUI(@PathParam(ID) Long id, ModelAndView mv) {
		LOGGER.info("商城-商品-后台详情");
		setInfo(mv, service.findById(id));

		return jsp("shop/goods-edit");
	}

	@GET
	@MvcFilter(filters = DataBaseFilter.class)
	@Path("/shop/goods/getJson/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public String getInfo(@PathParam(ID) Long id) {
		return toJson(GoodsService.dao.findById(id));
	}

	@GET
	@MvcFilter(filters = DataBaseFilter.class)
	@Override
	public String createUI(ModelAndView mv) {
		super.createUI(mv);
		return jsp("shop/goods-edit");
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
	@Path(ID_INFO)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public String update(@PathParam(ID) Long id, Goods entity) {
		return super.update(id, entity);
	}

	@DELETE
	@MvcFilter(filters = DataBaseFilter.class)
	@Path(ID_INFO)
	@Produces(MediaType.APPLICATION_JSON)
	public String delete(@PathParam(ID) Long id) {
		return delete(id, new Goods());
	}

	@GET
	@Path("/shop/goods")
	@MvcFilter(filters = { DataBaseFilter.class })
	public String list(@QueryParam(CATALOG_ID) int catelogId, ModelAndView mv, @QueryParam(START) int start, @QueryParam(LIMIT) int limit, @QueryParam("sellerId") int sellerId) {
		LOGGER.info("浏览商品");

		page(mv, service.findPagedListByCatalogId(catelogId, start, 9, CommonConstant.ON_LINE, sellerId));
		return jsp("shop/goods");
	}

	@GET
	@Path("/shop/goods/{id}/")
	@MvcFilter(filters = { DataBaseFilter.class, FrontEndOnlyCheck.class })
	public String showInfo(@PathParam(ID) Long id, ModelAndView mv) {
		LOGGER.info("浏览商品 info");

		mv.put(INFO, service.getGoodsDetail(id, BaseUserController.getUserId()));
		return jsp("shop/goods-info");
	}

	@Override
	public IBaseService<Goods> getService() {
		return service;
	}

	@Override
	public void prepareData(ModelAndView mv) {
		// 商家数据，记录不多，可以这样做
		Map<Long, Seller> map = new HashMap<>();
		SellerController.SellerService.dao.findList(null).forEach(seller -> map.put(seller.getId(), seller));
		mv.put("sellers", map);
		mv.put("PRODUCT_AREA_ID", ConfigService.getValueAsInt("data.productArea_Id"));
		mv.put(DOMAIN_CATALOG_ID, service.getDomainCatalogId());
//		
//		Map<Long, BaseModel> cMap = CatalogServiceImpl.list_bean2map_id_as_key(new CatalogServiceImpl().findAllListByParentId(service.getDomainCatalogId()));
//		mv.put("goodsCatalogs", cMap);

		super.prepareData(mv);
	}

}
