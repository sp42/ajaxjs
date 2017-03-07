package test.com.ajaxjs.framework;

import java.util.List;

import com.ajaxjs.framework.dao.BaseDao;
import com.ajaxjs.framework.dao.QueryParam;
import com.ajaxjs.framework.dao.annotation.Delete;
import com.ajaxjs.framework.dao.annotation.Insert;
import com.ajaxjs.framework.dao.annotation.Select;
import com.ajaxjs.framework.dao.annotation.Update;
import com.ajaxjs.framework.model.PageResult;

public interface NewsDao extends BaseDao<News, Long> {
	final static String tableName = "news";
	
	@Select("SELECT * FROM " + tableName + " WHERE id = ?")
	@Override
	public News findById(Long id);
	
	@Select("SELECT COUNT(*) AS Total FROM " + tableName)
	@Override
	public int count();
	
	@Select(value="SELECT * FROM news LIMIT ?, ?")
	public List<News> findList(int start, int limit);
	
	@Select(value="SELECT * FROM news")
	public PageResult<News> findPagedList(QueryParam parame);
	
	@Select("SELECT * FROM news ORDER BY createDate LIMIT 0, 10")
	public List<News> findTop10News();
	
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
