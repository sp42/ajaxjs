package com.ajaxjs.jxc.base.controller;

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
import com.ajaxjs.cms.service.DataDictService;
import com.ajaxjs.cms.service.TreeLikeService;
import com.ajaxjs.cms.service.UserAddressService;
import com.ajaxjs.framework.BaseController;
import com.ajaxjs.framework.filter.DataBaseFilter;
import com.ajaxjs.jxc.Constant;
import com.ajaxjs.jxc.base.model.Supplier;
import com.ajaxjs.jxc.base.service.SupplierService;
import com.ajaxjs.sql.orm.IBaseService;
import com.ajaxjs.util.ioc.Component;
import com.ajaxjs.util.ioc.Resource;
import com.ajaxjs.web.mvc.ModelAndView;
import com.ajaxjs.web.mvc.controller.SubBean;
import com.ajaxjs.web.mvc.filter.MvcFilter;

/**
 * 
 * 控制器
 */
@Component
@Path("/base/supplier")
public class SupplierController extends BaseController<Supplier> {
	@Resource
	private SupplierService service;

	@Resource
	private DataDictService dataDictService;

	@Resource
	private TreeLikeService treeLikeService;

	@Resource
	private UserAddressService addService;

	@GET
	@MvcFilter(filters = DataBaseFilter.class)
	public String get(@QueryParam(CATALOG_ID) int catalogId, @QueryParam(START) int start, @QueryParam(LIMIT) int limit, ModelAndView mv) {
		if (isJson()) {
			return toJson(service.findPagedList(catalogId, start, limit));
		} else {
			mv.put("CATALOGS", toJson(treeLikeService.getAllChildrenAsMap(SupplierService.CATALOGID), false));
			mv.put(DOMAIN_CATALOG_ID, SupplierService.CATALOGID);
			mv.put("暂停原因", dataDictService.getMap(Constant.DataDict.暂停原因));
			mv.put("货币主文件", dataDictService.getMap(Constant.DataDict.货币主文件));
			prepareData(mv);

			return page("/base/supplier");
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
	public String create(Supplier entity, @SubBean("ContactAddress") Address address) {
		String json = super.create(entity);

		address.setOwner(entity.getUid());
		long newlyId = addService.create(address);

		Supplier saveAddressId = new Supplier(); // 保存外键字段
		saveAddressId.setId(entity.getId());
		saveAddressId.setAddressId(newlyId);
		update(entity.getId(), saveAddressId);

		return json;
	}

	@PUT
	@MvcFilter(filters = DataBaseFilter.class)
	@Path(ID_INFO)
	@Produces(MediaType.APPLICATION_JSON)
	public String update(@PathParam(ID) Long id, Supplier entity, @SubBean("ContactAddress") Address address) {
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
		return delete(id, new Supplier());
	}

	@Override
	public IBaseService<Supplier> getService() {
		return service;
	}
}
