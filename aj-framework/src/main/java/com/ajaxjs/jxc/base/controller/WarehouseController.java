package com.ajaxjs.jxc.base.controller;

import java.util.function.Function;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.ajaxjs.cms.model.Address;
import com.ajaxjs.cms.service.UserAddressService;
import com.ajaxjs.framework.BaseController;
import com.ajaxjs.framework.BaseService;
import com.ajaxjs.framework.CommonConstant;
import com.ajaxjs.framework.QueryTools;
import com.ajaxjs.framework.filter.DataBaseFilter;
import com.ajaxjs.jxc.base.model.Warehouse;
import com.ajaxjs.sql.annotation.Select;
import com.ajaxjs.sql.annotation.TableName;
import com.ajaxjs.sql.orm.IBaseDao;
import com.ajaxjs.sql.orm.IBaseService;
import com.ajaxjs.sql.orm.PageResult;
import com.ajaxjs.sql.orm.Repository;
import com.ajaxjs.util.ReflectUtil;
import com.ajaxjs.util.ioc.Component;
import com.ajaxjs.util.ioc.Resource;
import com.ajaxjs.web.mvc.ModelAndView;
import com.ajaxjs.web.mvc.SubBean;
import com.ajaxjs.web.mvc.filter.MvcFilter;

/**
 * 
 * 仓库控制器
 */
@Component
@Path("/admin/jxc/warehouse")
public class WarehouseController extends BaseController<Warehouse> {
	@TableName(value = "jxc_仓库_仓库主文件", beanClass = Warehouse.class)
	public static interface WarehouseDao extends IBaseDao<Warehouse> {
		@Select("SELECT e.*, a.name AS contact, a.phone, a.locationProvince, a.locationCity, a.locationDistrict, a.address"
				+ " FROM ${tableName} e LEFT JOIN entity_address a ON e.addressId = a.id " + WHERE_REMARK_ORDER)
		@Override
		public PageResult<Warehouse> findPagedList(int start, int limit, Function<String, String> sqlHandler);
	}

	public static WarehouseDao dao = new Repository().bind(WarehouseDao.class);

	public class WarehouseService extends BaseService<Warehouse> {
		{
			setUiName("仓库");
			setShortName("Warehouse");
			setDao(dao);
		}

		public PageResult<Warehouse> findPagedList(int catalogId, int start, int limit) {
			Function<String, String> handler = BaseService::searchQuery_NameOnly;
			handler = handler.andThen(BaseService::betweenCreateDate);

			return findPagedList(start, limit, handler);
		}
	}

	private WarehouseService service = new WarehouseService();

	@Resource
	private UserAddressService addService;

	@GET
	@MvcFilter(filters = DataBaseFilter.class)
	public String get(HttpServletRequest r, @QueryParam("owner") long owner, @QueryParam(START) int start, @QueryParam(LIMIT) int limit, ModelAndView mv) {
		if (isJson()) {
			Function<String, String> handler = BaseService::searchQuery_NameOnly;
			handler = handler.andThen(QueryTools.byAny(r)).andThen(BaseService::betweenCreateDate);

			if (owner != 0L)
				handler = handler.andThen(QueryTools.by("owner", owner));

			return toJson(service.findPagedList(start, limit, handler));
		} else {
			mv.put("StatusDicts", ReflectUtil.getConstantsInt(CommonConstant.实体状态.class));
			prepareData(mv);

			return jsp("jxc/warehouse/warehouse");
		}
	}

	@GET
	@Path(ID_INFO)
	@Produces(MediaType.APPLICATION_JSON)
	@MvcFilter(filters = DataBaseFilter.class)
	public String info(@PathParam(ID) long id) {
		return toJson(service.findById(id));
	}

	@POST
	@MvcFilter(filters = DataBaseFilter.class)
	@Produces(MediaType.APPLICATION_JSON)
	public String create(Warehouse entity, @SubBean("ContactAddress") Address address) {
		String json = super.create(entity);

		address.setOwner(entity.getUid());
		long newlyId = addService.create(address);

		Warehouse saveAddressId = new Warehouse(); // 保存外键字段
		saveAddressId.setId(entity.getId());
		saveAddressId.setAddressId(newlyId);
		update(entity.getId(), saveAddressId);

		return json;
	}

	@PUT
	@MvcFilter(filters = DataBaseFilter.class)
	@Path(ID_INFO)
	@Produces(MediaType.APPLICATION_JSON)
	public String update(@PathParam(ID) Long id, Warehouse entity, @SubBean("ContactAddress") Address address) {
		String json = super.update(id, entity);

		if (entity.getAddressId() != null) {
			entity = getService().findById(id);
			address.setId(entity.getAddressId());
			addService.update(address);
		}

		return json;
	}

	@DELETE
	@MvcFilter(filters = DataBaseFilter.class)
	@Path(ID_INFO)
	@Produces(MediaType.APPLICATION_JSON)
	public String delete(@PathParam(ID) Long id) {
		return delete(id, new Warehouse());
	}

	@Override
	public IBaseService<Warehouse> getService() {
		return service;
	}
}
