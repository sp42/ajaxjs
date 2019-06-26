package com.ajaxjs.cms;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ajaxjs.cms.app.ArticleService;
import com.ajaxjs.framework.BaseService;
import com.ajaxjs.framework.IBaseDao;
import com.ajaxjs.framework.Repository;
import com.ajaxjs.ioc.Bean;
import com.ajaxjs.orm.annotation.Select;
import com.ajaxjs.orm.annotation.TableName;

@Bean("DataDictService")
public class DataDictService extends BaseService<Map<String, Object>> {
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
	 * @param list 实体列表
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
