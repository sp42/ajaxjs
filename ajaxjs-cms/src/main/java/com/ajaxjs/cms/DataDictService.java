package com.ajaxjs.cms;

import com.ajaxjs.framework.BaseService;
import com.ajaxjs.framework.EntityMap;
import com.ajaxjs.framework.IBaseDao;
import com.ajaxjs.framework.Repository;
import com.ajaxjs.ioc.Bean;
import com.ajaxjs.orm.annotation.TableName;

@Bean("DataDictService")
public class DataDictService extends BaseService<EntityMap> {
	@TableName(value = "general_data_dict", beanClass = EntityMap.class)
	public static interface DataDictDao extends IBaseDao<EntityMap> {
	}

	public static DataDictDao dao = new Repository().bind(DataDictDao.class);

	{
		setUiName("数据字典");
		setShortName("datadict");
		setDao(dao);
	}
}
