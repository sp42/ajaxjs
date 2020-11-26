<%@page pageEncoding="UTF-8"%>package ${packageName}.service;

import com.ajaxjs.framework.BaseService;
import com.ajaxjs.framework.Repository;
import com.ajaxjs.ioc.Bean;
import ${packageName}.model.${beanName};
import com.ajaxjs.framework.IBaseDao;
import com.ajaxjs.orm.annotation.TableName;

@Bean
public class ${beanName}Service extends BaseService<${beanName}> {
	@TableName(value = "${tableName}", beanClass = ${beanName}.class)
	public static interface ${beanName}Dao extends IBaseDao<${beanName}> {
	}
	
	public static ${beanName}Dao dao = new Repository().bind(${beanName}Dao.class);
	
	{
		setUiName("${tablesCommentShortName}");
		setShortName("${beanName}");
		setDao(dao);
	}

}