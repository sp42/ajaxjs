package test.com.ajaxjs.framework.v2;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.ajaxjs.framework.dao.BaseDao;
import com.ajaxjs.framework.dao.MyBatisDao;

import test.com.ajaxjs.framework.News;

public interface NewsDao extends MyBatisDao<News, Long> {
	@Select("SELECT * FROM news LIMIT ${start}, ${limit}")
	@Override
	public List<News> findList(@Param("start") int start, @Param("limit") int limit);
}
