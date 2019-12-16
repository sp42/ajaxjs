package com.ajaxjs.shop.controller;

import java.util.List;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.ajaxjs.framework.BaseController;
import com.ajaxjs.framework.BaseService;
import com.ajaxjs.framework.IBaseDao;
import com.ajaxjs.framework.IBaseService;
import com.ajaxjs.framework.Repository;
import com.ajaxjs.ioc.Bean;
import com.ajaxjs.mvc.ModelAndView;
import com.ajaxjs.mvc.filter.DataBaseFilter;
import com.ajaxjs.mvc.filter.MvcFilter;
import com.ajaxjs.orm.annotation.Select;
import com.ajaxjs.orm.annotation.TableName;
import com.ajaxjs.shop.model.Seller;
import com.ajaxjs.util.logger.LogHelper;

@Bean
@Path("/admin/seller")
public class SellerController extends BaseController<Seller> {
	private static final LogHelper LOGGER = LogHelper.getLog(BaseController.class);
	
	@TableName(value = "shop_seller", beanClass = Seller.class)
	public static interface SellerDao extends IBaseDao<Seller> {
		@Select("SELECT * FROM ${tableName} WHERE userId = ?")
		public List<Seller> findListByUserId(long userId);
		
		@Select("SELECT id FROM ${tableName} WHERE isDefault = 1")
		public Long getDefaultAddressId();
	}
	
	public static class SellerService extends BaseService<Seller> {
		public static SellerDao dao = new Repository().bind(SellerDao.class);

		{
			setUiName("商家");
			setShortName("seller");
			setDao(dao);
		}
	}
	
	private SellerService service = new SellerService();

	@GET
	@Path(list)
	@MvcFilter(filters = DataBaseFilter.class)
	public String list(@QueryParam(start) int start, @QueryParam(limit) int limit, ModelAndView mv) {
		LOGGER.info("获取" + getService().getUiName() + "分页列表 GET list");
		
		page(mv, service.findPagedList(start, limit, null));
		return jsp("shop/seller-admin-list");
	}

	@GET
	@MvcFilter(filters = DataBaseFilter.class)
	@Path(idInfo)
	@Override
	public String editUI(@PathParam(id) Long id, ModelAndView mv) {
		super.editUI(id, mv);
		return jsp("shop/seller-edit");
	}
	
	@GET
	@Override
	public String createUI(ModelAndView mv) {
		super.createUI(mv);
		return jsp("shop/seller-edit");
	}

	@POST
	@MvcFilter(filters = DataBaseFilter.class)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public String create(Seller entity) {
		return super.create(entity);
	}

	@PUT
	@MvcFilter(filters = DataBaseFilter.class)
	@Path(idInfo)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public String update(@PathParam(id) Long id, Seller entity) {
		return super.update(id, entity);
	}

	@DELETE
	@MvcFilter(filters = DataBaseFilter.class)
	@Path(idInfo)
	@Produces(MediaType.APPLICATION_JSON)
	public String delete(@PathParam(id) Long id) {
		return delete(id, new Seller());
	}
	
	@Override
	public IBaseService<Seller> getService() {
		return service;
	}
}
