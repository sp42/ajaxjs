<%@page pageEncoding="UTF-8"%>package ${packageName}.dao;


import ${packageName}.model.${beanName};
import com.ajaxjs.framework.IBaseDao;
import com.ajaxjs.orm.annotation.TableName;

@TableName(value = "${tableName}", beanClass = ${beanName}.class)
public interface ${beanName}Dao extends IBaseDao<${beanName}> {

}