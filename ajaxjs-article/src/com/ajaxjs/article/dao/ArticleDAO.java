package com.ajaxjs.article.dao;

import java.util.List;

import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

import com.ajaxjs.article.model.Article;
import com.ajaxjs.framework.dao.DAO;
import com.ajaxjs.framework.dao.DynamicSqlProvider;
import com.ajaxjs.framework.model.Query;

public interface ArticleDAO extends DAO<Article> {
	@Select(selectById)
	@Override
	Article selectById(@Param("id") long id, @Param("tablename") String tablename);

	@Select(selectByUUID)
	@Override
	Article selectByUUID(@Param("id") String uuid, @Param("tablename") String tablename);

	@SelectProvider(type = DynamicSqlProvider.class, method = "pageCount")
	@Override
	int pageCount(@Param("tablename") String tablename, @Param("query") Query query);

//	 @Results({@Result(column = "name", property = "name", javaType = String.class,
//			jdbcType =JdbcType.VARCHAR),
//	 })
	@SelectProvider(type = DynamicSqlProvider.class, method = "page")
	@Override
	List<Article> page(@Param("start") int start, @Param("limit") int limit, @Param("tablename") String tablename, @Param("query") Query query);

	@InsertProvider(type = DynamicSqlProvider.class, method = "create")
// @Insert("INSERT INTO news (name, isOnline) VALUES(#{service.tableName}, #{online})")
	@Options(useGeneratedKeys = true, keyProperty = "id")
	@Override
	int create(Article news);

	@UpdateProvider(type = DynamicSqlProvider.class, method = "update")
	@Override
	int update(Article news);
}