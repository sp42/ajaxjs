package test.com.ajaxjs.framework.v2;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.ajaxjs.framework.dao.BaseDao;
import com.ajaxjs.framework.dao.annotation.Select;

import test.com.ajaxjs.framework.News;

public interface NewsDao extends BaseDao<News, Long> {
	@Select("SELECT * FROM news LIMIT ?, ?")
	@Override
	public List<News> findList(@Param("start") int start, @Param("limit") int limit);
}
