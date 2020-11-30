package com.ajaxjs.cms.common;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ajaxjs.framework.BaseService;
import com.ajaxjs.framework.QueryTools;
import com.ajaxjs.sql.annotation.Insert;
import com.ajaxjs.sql.annotation.TableName;
import com.ajaxjs.sql.orm.IBaseDao;
import com.ajaxjs.sql.orm.Repository;
import com.ajaxjs.util.ioc.Component;

@Component
public class DataDictService extends BaseService<Map<String, Object>> {
	@TableName(value = "general_data_dict", beanClass = Map.class)
	public interface DataDictDao extends IBaseDao<Map<String, Object>> {

		@Insert("INSERT INTO `${tableName}` (`key`, `value`) VALUES (?, ?)")
		public Long create(String key, String value);
	}

	public static DataDictDao dao = new Repository().bind(DataDictDao.class);

	public Map<String, String> getMap(int tid) {
		List<Map<String, Object>> list = dao.findList(QueryTools.by("tid", tid));

		Map<String, String> map = new HashMap<>();
		for (Map<String, Object> _map : list) {
			map.put(_map.get("key").toString(), _map.get("value").toString());
		}

		System.out.println(map);

		return map;
	}
	
	{
		setUiName("数据字典");
		setShortName("DataDict");
		setDao(dao);
	}
}
