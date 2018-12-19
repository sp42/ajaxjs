<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>package ${packageName}.service;
import java.util.List;
import com.ajaxjs.orm.dao.DaoHandler;
import com.ajaxjs.orm.dao.PageResult;
import ${packageName}.dao.${beanName}Dao;
import ${packageName}.model.${beanName};
import com.ajaxjs.ioc.Bean;

@Bean("${beanName}Service")
public class ${beanName}ServiceImpl implements ${beanName}Service {
	public ${beanName}Dao dao = new DaoHandler().bind(${beanName}Dao.class);

	@Override
	public ${beanName} findById(Long id) {
		return dao.findById(id);
	}

	@Override
	public Long create(${beanName} bean) {
		return dao.create(bean);
	}

	@Override
	public int update(${beanName} bean) {
		return dao.update(bean);
	}

	@Override
	public boolean delete(${beanName} bean) {
		return dao.delete(bean);
	}


	@Override
	public PageResult<${beanName}> findPagedList(int start, int limit) {
		return dao.findPagedList(start, limit);
	}
	
	@Override
	public List<${beanName}> findList() {
		return null;
	} 

	@Override
	public String getName() {
		return "${tablesCommentChinese}";
	}

	@Override
	public String getTableName() {
		return "${tableName}";
	} 

}