package com.egdtv.crawler.dao;

import java.util.List;

import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

import com.ajaxjs.framework.dao.DAO;
import com.ajaxjs.framework.dao.DynamicSqlProvider;
import com.ajaxjs.framework.model.Query;
import com.egdtv.crawler.model.Portal;


public interface PortalDao extends DAO<Portal>  {
	@Select(selectById)
	@Override
	Portal selectById(@Param("id") long id, @Param("tablename") String tablename);
	
	@SelectProvider(type = DynamicSqlProvider.class, method = "pageCount")
	@Override
	int pageCount(@Param("tablename") String tablename, @Param("query") Query query);
	
	@SelectProvider(type = DynamicSqlProvider.class, method = "page")
	@Override
	List<Portal> page(@Param("start") int start, @Param("limit") int limit, @Param("tablename") String tablename, @Param("query") Query query);

	@UpdateProvider(type = DynamicSqlProvider.class, method = "update")
	@Override
	int update(Portal portal);
	
	@InsertProvider(type = DynamicSqlProvider.class, method = "create")
	@Override
	int create(Portal portal);
}
