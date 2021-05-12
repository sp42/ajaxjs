package com.ajaxjs.jxc.base.controller;

import java.util.function.Function;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.ajaxjs.entity.filter.DataBaseFilter;
import com.ajaxjs.framework.BaseController;
import com.ajaxjs.framework.BaseService;
import com.ajaxjs.jxc.base.model.TaxRate;
import com.ajaxjs.sql.annotation.TableName;
import com.ajaxjs.sql.orm.IBaseDao;
import com.ajaxjs.sql.orm.IBaseService;
import com.ajaxjs.sql.orm.Repository;
import com.ajaxjs.util.ioc.Component;
import com.ajaxjs.web.mvc.ModelAndView;
import com.ajaxjs.web.mvc.filter.MvcFilter;

/**
 * 
 * 税率控制器
 */
@Component
@Path("/admin/jxc/base/tax_rate")
public class TaxRateController extends BaseController<TaxRate> {
	@TableName(value = "jxc_基础_税率", beanClass = TaxRate.class)
	public static interface TaxRateDao extends IBaseDao<TaxRate> {
	}

	public static TaxRateDao dao = new Repository().bind(TaxRateDao.class);

	public static class TaxRateService extends BaseService<TaxRate> {
		{
			setUiName("税率");
			setShortName("Supplier");
			setDao(dao);
		}
	}

	private TaxRateService service = new TaxRateService();

	@GET
	@MvcFilter(filters = DataBaseFilter.class)
	public String get(@QueryParam(START) int start, @QueryParam(LIMIT) int limit, ModelAndView mv) {
		if (isJson()) {
			Function<String, String> handler = BaseService::searchQuery_NameOnly;
			handler = handler.andThen(BaseService::betweenCreateDate);
			
			return toJson(service.findPagedList(start, limit, handler));
		} else {
			prepareData(mv);
			return jsp("jxc/base/tax_rate");
		}
	}

	@POST
	@MvcFilter(filters = DataBaseFilter.class)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public String create(TaxRate entity) {
		return super.create(entity);
	}

	@PUT
	@MvcFilter(filters = DataBaseFilter.class)
	@Path(ID_INFO)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public String update(@PathParam(ID) Long id, TaxRate entity) {
		return super.update(id, entity);
	}

	@DELETE
	@MvcFilter(filters = DataBaseFilter.class)
	@Path(ID_INFO)
	@Produces(MediaType.APPLICATION_JSON)
	public String delete(@PathParam(ID) Long id) {
		return delete(id, new TaxRate());
	}

	@Override
	public IBaseService<TaxRate> getService() {
		return service;
	}
}
