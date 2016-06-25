package com.ajaxjs.framework.user;

import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;

import com.ajaxjs.framework.dao.DAO;
import com.ajaxjs.framework.dao.DynamicSqlProvider;

public interface Mapper extends DAO<User> {
	@SelectProvider(type = DynamicSqlProvider.class, method = "selectById")
	@Override
	User selectById(@Param("id") long id, @Param("tablename") String tablename);
	
	@InsertProvider(type = DynamicSqlProvider.class, method = "create")
	@Options(useGeneratedKeys = true, keyProperty = "id")
	@Override
	int create(User user);
}
