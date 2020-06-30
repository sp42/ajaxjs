package com.ajaxjs.shop.controller;

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
import com.ajaxjs.framework.filter.DataBaseFilter;
import com.ajaxjs.mvc.ModelAndView;
import com.ajaxjs.mvc.filter.MvcFilter;
import com.ajaxjs.shop.model.Seller;
import com.ajaxjs.sql.annotation.TableName;
import com.ajaxjs.sql.orm.IBaseDao;
import com.ajaxjs.sql.orm.IBaseService;
import com.ajaxjs.sql.orm.Repository;
import com.ajaxjs.user.filter.LoginCheck;
import com.ajaxjs.util.ioc.Bean;
import com.ajaxjs.util.logger.LogHelper;

@Bean
@Path("/admin/seller")
public class SellerController extends BaseController<Seller> {
	private static final LogHelper LOGGER = LogHelper.getLog(BaseController.class);

	@TableName(value = "shop_seller", beanClass = Seller.class)
	public static interface SellerDao extends IBaseDao<Seller> {
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

	@Override
	public IBaseService<Seller> getService() {
		return service;
	}

	@GET
	@Path(LIST)
	@MvcFilter(filters = { LoginCheck.class, DataBaseFilter.class })
	public String list(@QueryParam(START) int start, @QueryParam(LIMIT) int limit, ModelAndView mv) {
		LOGGER.info("获取" + getService().getUiName() + "分页列表 GET list");

		page(mv, service.findPagedList(start, limit, null));
		return jsp("shop/seller-admin-list");
	}

	@GET
	@Path(ID_INFO)
	@MvcFilter(filters = { LoginCheck.class, DataBaseFilter.class })
	public String editUI(@PathParam(ID) Long id, ModelAndView mv) {
		setInfo(mv, service.findById(id));
		return jsp("shop/seller-edit");
	}

	@GET
	@MvcFilter(filters = { LoginCheck.class })
	@Override
	public String createUI(ModelAndView mv) {
		super.createUI(mv);
		return jsp("shop/seller-edit");
	}

	@POST
	@MvcFilter(filters = { LoginCheck.class, DataBaseFilter.class })
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public String create(Seller entity) {
		return super.create(entity);
	}

	@PUT
	@Path(ID_INFO)
	@MvcFilter(filters = { LoginCheck.class, DataBaseFilter.class })
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public String update(@PathParam(ID) Long id, Seller entity) {
		return super.update(id, entity);
	}

	@DELETE
	@MvcFilter(filters = { LoginCheck.class, DataBaseFilter.class })
	@Path(ID_INFO)
	@Produces(MediaType.APPLICATION_JSON)
	public String delete(@PathParam(ID) Long id) {
		return delete(id, new Seller());
	}

}
