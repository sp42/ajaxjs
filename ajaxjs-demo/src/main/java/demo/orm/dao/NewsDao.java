package demo.orm.dao;

import java.util.List;

import com.ajaxjs.framework.IBaseDao;
import com.ajaxjs.orm.annotation.Select;
import com.ajaxjs.orm.annotation.TableName;

@TableName(value = "news", beanClass = News.class)
public interface NewsDao extends IBaseDao<News> {
	@Select("SELECT * FROM ${tableName}")
	public List<News> findList();
}