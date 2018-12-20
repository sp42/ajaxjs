<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>package ${packageName}.service;

import com.ajaxjs.framework.BaseService;
import com.ajaxjs.framework.Repository;
import com.ajaxjs.ioc.Bean;
import ${packageName}.dao.${beanName}Dao;
import ${packageName}.model.${beanName};

@Bean("${beanName}Service")
public class ${beanName}ServiceImpl extends BaseService<${beanName}>  implements ${beanName}Service {
	${beanName}Dao dao = new Repository().bind(${beanName}Dao.class);
	
	{
		setUiName("客户端更新接口");
		setShortName("${beanName}");
		setDao(dao);
	}

}