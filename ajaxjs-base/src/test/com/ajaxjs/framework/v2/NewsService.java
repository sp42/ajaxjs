package test.com.ajaxjs.framework.v2;

import com.ajaxjs.framework.service.BaseServiceImpl;
import com.ajaxjs.jdbc.MyBatis;

import test.com.ajaxjs.framework.News;

public class NewsService extends BaseServiceImpl<News, Long> {
	public NewsService(){
		MyBatis.addDao(NewsDao.class);
	}
}