package com.ajaxjs.cms;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ajaxjs.framework.BaseModel;
import com.ajaxjs.framework.BaseService;
import com.ajaxjs.framework.IBaseDao;
import com.ajaxjs.framework.Repository;
import com.ajaxjs.ioc.Bean;
import com.ajaxjs.orm.annotation.Select;
import com.ajaxjs.orm.annotation.TableName;
import com.ajaxjs.util.CommonUtil;

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
	
	/**
	 * 把列表（BaseModel 结构）转换为 map，以 id 作为键值。key 本来是 long，为照顾 el 转换为 int
	 * 
	 * @param bean
	 *            实体列表
	 * @return 以 id 作为键值的 map
	 */
	public static Map<Integer, BaseModel> list_bean2map_id_as_key(List<? extends BaseModel> bean) {
		if (CommonUtil.isNull(bean))
			return null;

		Map<Integer, BaseModel> map = new HashMap<>();

		for (BaseModel item : bean)
			map.put(new Long(item.getId()).intValue(), item);

		return map;
	}

	/**
	 * 把列表（Map结构）转换为 map，以 id 作为键值。key 本来是 long，为照顾 el 转换为 int
	 * 
	 * @param list
	 *            实体列表
	 * @return 以 id 作为键值的 map
	 */
	public static Map<Integer, Object> list2map_id_as_key(List<Map<String, Object>> list) {
		if (CommonUtil.isNull(list))
			return null;

		Map<Integer, Object> map = new HashMap<>();

		for (Map<String, Object> item : list)
			map.put(new Long(item.get("id").toString()).intValue(), item);

		return map;
	}
	
	public static final int ENTRY_ARTICLE = 52;
	public static final int ENTRY_TOPIC = 54;
	public static final int ENTRY_ADS = 55;
}
