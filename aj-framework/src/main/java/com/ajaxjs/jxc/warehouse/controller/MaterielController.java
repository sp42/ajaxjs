package com.ajaxjs.jxc.warehouse.controller;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.ajaxjs.cms.common.DataDictService;
import com.ajaxjs.cms.common.TreeLikeService;
import com.ajaxjs.framework.BaseController;
import com.ajaxjs.framework.filter.DataBaseFilter;
import com.ajaxjs.jxc.Constant;
import com.ajaxjs.jxc.base.model.Supplier;
import com.ajaxjs.jxc.base.service.SupplierService;
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
@Path("/warehouse/materiel")
public class MaterielController extends BaseController<Supplier> {
	@Resource
	private SupplierService service;

	@Resource
	private DataDictService dataDictService;

	@Resource
	private TreeLikeService treeLikeService;

	@GET
	@MvcFilter(filters = DataBaseFilter.class)
	public String list(@QueryParam(START) int start, @QueryParam(LIMIT) int limit, ModelAndView mv) {
		if (isJson())
			return toJson(service.findPagedList(SupplierService.CATALOGID, start, limit));
		else {
			return jsp("jxc/warehouse/materiel");
		}
	}

	@GET
	@Path(ID_INFO)
	@Produces(MediaType.APPLICATION_JSON)
	@MvcFilter(filters = DataBaseFilter.class)
	public String info(@PathParam(ID) long id) {
		return toJson(service.findById(id));
	}

	@GET
	@MvcFilter(filters = DataBaseFilter.class)
	public String page(ModelAndView mv) {
		mv.put("暂停原因", dataDictService.getMap(Constant.DataDict.暂停原因));
		mv.put("货币主文件", dataDictService.getMap(Constant.DataDict.货币主文件));
		mv.put(DOMAIN_CATALOG_ID, SupplierService.CATALOGID);

		prepareData(mv);
		return jsp("jxc/supplier");
	}

	@POST
	@MvcFilter(filters = DataBaseFilter.class)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public String create(Supplier entity) {
		return super.create(entity);
	}

	@PUT
	@MvcFilter(filters = DataBaseFilter.class)
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public String update(@PathParam(ID) Long id, Supplier entity) {
		return super.update(id, entity);
	}

	@DELETE
	@MvcFilter(filters = DataBaseFilter.class)
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public String delete(@PathParam(ID) Long id) {
		return delete(id, new Supplier());
	}

	@Override
	public IBaseService<Supplier> getService() {
		return service;
	}
}
