package com.ajaxjs.user.dao;

import java.util.List;
import java.util.Map;

import com.ajaxjs.cms.dao.CatalogDao;
import com.ajaxjs.cms.model.Catalog;
import com.ajaxjs.orm.annotation.Delete;
import com.ajaxjs.orm.annotation.Insert;
import com.ajaxjs.orm.annotation.Select;
import com.ajaxjs.orm.annotation.Update;
import com.ajaxjs.orm.dao.IDao;
import com.ajaxjs.orm.dao.PageResult;
import com.ajaxjs.orm.dao.QueryParams;
import com.ajaxjs.user.model.User;

public interface UserAdminDao extends IDao<User, Long> {
	public static final String tableName = "user";
	public static int NewsCatalogId = 6;

	@Select(value = "SELECT * FROM " + tableName)
	@Override
	public PageResult<User> findPagedList(QueryParams param, int start, int limit);
	
	@Select(value = "SELECT * FROM " + tableName)
	@Override
	public PageResult<User> findPagedList(int start, int limit);
	
	@Select(value = "SELECT * FROM " + tableName)
	public PageResult<User> findAdminPagedList(QueryParams param);
	
	@Select("SELECT * FROM " + tableName + " WHERE id = ?")
	@Override
	public User findById(Long id);

	@Select(value = "SELECT * FROM " + tableName)
	public List<User> findAll(QueryParams param);
	
	@Insert(tableName = tableName)
	@Override
	public Long create(User map);

	@Update(tableName = tableName)
	@Override
	public int update(User map);

	@Delete(tableName = tableName)
	@Override
	public boolean delete(User map);
	
	@Select("SELECT * FROM " + tableName + " LIMIT 0, 5")
	public List<User> getTop5();
	
	
	@Select("SELECT * FROM " + CatalogDao.tableName + " WHERE pid = " + NewsCatalogId)
	public List<Catalog> getCatalog();
	
	@Select(value = "SELECT user_login_log.*, user.name AS name,  user.username AS username FROM user_login_log INNER JOIN user ON user.id = user_login_log.userId")
	public PageResult<Map<String, Object>> findLoginLog(QueryParams param, int start, int limit);
}
