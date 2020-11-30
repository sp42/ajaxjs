package com.ajaxjs.cms.common;

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

import com.ajaxjs.cms.service.DataDictService;
import com.ajaxjs.framework.BaseController;
import com.ajaxjs.framework.QueryTools;
import com.ajaxjs.framework.filter.DataBaseFilter;
import com.ajaxjs.sql.orm.IBaseService;
import com.ajaxjs.util.ReflectUtil;
import com.ajaxjs.util.ioc.Component;
import com.ajaxjs.util.ioc.ComponentMgr;
import com.ajaxjs.util.ioc.Resource;
import com.ajaxjs.web.mvc.ModelAndView;
import com.ajaxjs.web.mvc.filter.MvcFilter;

/**
 * 数据字典
 * 
 * @author sp42 frank@ajaxjs.com
 *
 *         收货信息： 姓名：黎志坚 邮寄地址：广东省 广州市 越秀区白云路45号之四602 邮政编码：510200 移动电话：13533476582
 * 
 */
@Path("/admin/common/datadict/")
@Component
public class DataDictController extends BaseController<Map<String, Object>> {
	@Resource
	private DataDictService service;

	@GET
	@MvcFilter(filters = DataBaseFilter.class)
	public String get(@QueryParam("tid") int tid, ModelAndView mv) {
		if (isJson()) {
			if (tid == 0)
				throw new IllegalArgumentException("没有 tid 参数！");

			return toJson(DataDictService.dao.findList(QueryTools.by("tid", tid)));
		} else {
			// Constant.DataDict.class
			try {
				Class<?> clz = ComponentMgr.get("DataDictClass", Class.class);

				if (clz != null)
					mv.put("DataDicts", ReflectUtil.getConstantsInt(clz));
			} catch (Throwable e) {
				// 忽略
			}

			prepareData(mv);

			return jsp("common/data-dict");
		}
	}

//	@POST
//	@Produces(MediaType.APPLICATION_JSON)
//	@MvcFilter(filters = DataBaseFilter.class)
//	public String save(@NotNull @QueryParam("json") String json) {
//		List<Map<String, Object>> list = JsonHelper.parseList(json);
//
//		list.forEach(map -> {
//			if (service.update(map) > 0) {
//
//			}
//		});
//
//		return jsonOk("更新成功！");
//	}

	@POST
	@MvcFilter(filters = DataBaseFilter.class)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public String create(Map<String, Object> entity) {
		return super.create(entity);
	}

	@PUT
	@MvcFilter(filters = DataBaseFilter.class)
	@Path(ID_INFO)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public String update(@PathParam(ID) Long id, Map<String, Object> entity) {
		return super.update(id, entity);
	}

	@DELETE
	@MvcFilter(filters = DataBaseFilter.class)
	@Path(ID_INFO)
	@Produces(MediaType.APPLICATION_JSON)
	public String delete(@PathParam(ID) Long id) {
		return delete(id, new HashMap<>());
	}

	@Override
	public IBaseService<Map<String, Object>> getService() {
		return service;
	}
}
