package test.com.ajaxjs.framework.v2;

import java.util.List;

import com.ajaxjs.framework.dao.BaseDao;
import com.ajaxjs.framework.dao.annotation.Delete;
import com.ajaxjs.framework.dao.annotation.Insert;
import com.ajaxjs.framework.dao.annotation.Select;
import com.ajaxjs.framework.dao.annotation.Update;

import test.com.ajaxjs.framework.News;

public interface NewsDao extends BaseDao<News, Long> {
	final static String tableName = "news";
	
	@Select("SELECT * FROM " + tableName + " WHERE id = ?")
	@Override
	public News findById(Long id);
	
	@Select("SELECT COUNT(*) AS Total FROM " + tableName)
	@Override
	public int count();
	
	@Select("SELECT * FROM news LIMIT ?, ?")
	@Override
	public List<News> findList(int start, int limit);
	
	@Insert(tableName=tableName)
	@Override
	public Long create(News bean);

	@Update(tableName=tableName)
	@Override
	public int update(News bean);

	@Delete(tableName=tableName)
	@Override
	public int delete(Long id);
}
