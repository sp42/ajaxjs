package com.ajaxjs.cms;

import java.util.Map;

import com.ajaxjs.framework.BaseService;
import com.ajaxjs.framework.IBaseDao;
import com.ajaxjs.framework.Repository;
import com.ajaxjs.ioc.Bean;
import com.ajaxjs.orm.annotation.TableName;

@Bean("DataDictService")
public class DataDictService extends BaseService<Map<String, Object>> {
	@TableName(value = "general_data_dict", beanClass = Map.class)
	public static interface DataDictDao extends IBaseDao<Map<String, Object>> {
	}

	public static DataDictDao dao = new Repository().bind(DataDictDao.class);

	{
		setUiName("数据字典");
		setShortName("datadict");
		setDao(dao);
	}
}
