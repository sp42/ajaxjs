package com.ajaxjs.app;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.ajaxjs.framework.BaseController;
import com.ajaxjs.framework.BaseService;
import com.ajaxjs.framework.IBaseDao;
import com.ajaxjs.framework.IBaseService;
import com.ajaxjs.framework.Repository;
import com.ajaxjs.framework.filter.DataBaseFilter;
import com.ajaxjs.ioc.Bean;
import com.ajaxjs.mvc.Constant;
import com.ajaxjs.mvc.ModelAndView;
import com.ajaxjs.mvc.filter.MvcFilter;
import com.ajaxjs.orm.annotation.Select;
import com.ajaxjs.orm.annotation.TableName;
import com.ajaxjs.util.logger.LogHelper;

@Bean
@Deprecated
@Path("/admin/DataDict")
public class DataDictController extends BaseController<Map<String, Object>> {
	private static final LogHelper LOGGER = LogHelper.getLog(DataDictController.class);
	
	public static class DataDictService extends BaseService<Map<String, Object>> {
		@TableName(value = "general_data_dict", beanClass = Map.class)
		public static interface DataDictDao extends IBaseDao<Map<String, Object>> {
			@Select("SELECT * FROM ${tableName} WHERE pid = ?")
			public List<Map<String, Object>> findByParentId(long pid);
		}

		public static DataDictDao dao = new Repository().bind(DataDictDao.class);

		{
			setUiName("数据字典");
			setShortName("datadict");
			setDao(dao);
		}
		
		public static final int ENTRY_ARTICLE = 52;
		public static final int ENTRY_TOPIC = 54;
		public static final int ENTRY_ADS = 55;
		
		public static final Map<Integer, String> Entry_IdName = new HashMap<Integer, String>() {
			private static final long serialVersionUID = -1L;

			{
				put(ENTRY_ARTICLE, new ArticleService().getUiName());
				put(ENTRY_TOPIC, new AdsService().getUiName());
			}
		};

		/**
		 * 把列表（Map结构）转换为 map，以 id 作为键值。
		 * 
		 * @param LIST 实体列表
		 * @return 以 id 作为键值的 map
		 */
		public static Map<Integer, Map<String, Object>> idAsKey(int id) {
			Map<Integer, Map<String, Object>> map = new HashMap<>();

			DataDictService.dao.findByParentId(id).forEach(dict -> {
				map.put((Integer) dict.get("id"), dict);
			});

			return map;
		}
	}
	
	private DataDictService service = new DataDictService();
	
	@GET
	@Override
	public String createUI(ModelAndView mv) {
		LOGGER.info("数据字典管理");
		super.createUI(mv);
		
		return admin(service.getShortName());
	}

	@GET
	@Path(LIST)
	@Produces(MediaType.APPLICATION_JSON)
	@MvcFilter(filters = DataBaseFilter.class)
	public String list() {
		return toJson(service.getDao().findList(null));
	}

	@GET
	@Path("getDictListByParentId/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@MvcFilter(filters = DataBaseFilter.class)
	public List<Map<String, Object>> getDictListByParentId(@PathParam(ID) long pId) {
		return DataDictService.dao.findByParentId(pId);
	}

	@POST
	@MvcFilter(filters = DataBaseFilter.class)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public String create(Map<String, Object> entity) {
		return super.create(entity);
	}

	@PUT
	@MvcFilter(filters = DataBaseFilter.class)
	@Path(Constant.ID_INFO)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public String update(@PathParam(ID) Long id, Map<String, Object> entity) {
		return super.update(id, entity);
	}

	@DELETE
	@Path(Constant.ID_INFO)
	@MvcFilter(filters = DataBaseFilter.class)
	@Produces(MediaType.APPLICATION_JSON)
	public String delete(@PathParam(ID) Long id) {
		return delete(id, new HashMap<String, Object>());
	}

	@Override
	public IBaseService<Map<String, Object>> getService() {
		return service;
	}
}
