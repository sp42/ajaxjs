package test.com.ajaxjs.framework;

import com.ajaxjs.framework.dao.QueryParams;
import com.ajaxjs.framework.model.PageResult;
import com.ajaxjs.framework.service.BaseDaoService;
import com.ajaxjs.framework.service.IService;

public class NewsService extends BaseDaoService<News, Long, NewsDao > implements IService<News, Long>{
	public NewsService() {
		initDao(NewsDao.class);
	}

	public News getFirstNews() {
		System.out.println("getDao:" + getDao());
		News news = getDao().findById(1L);

		return news;
	}

	@Override
	public News findById(Long id) {
		return getDao().findById(id);
	}

	@Override
	public Long create(News bean) {
		return null;
	}

	@Override
	public int update(News bean) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean delete(News bean) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public PageResult<News> findPagedList(QueryParams parame) {
		// TODO Auto-generated method stub
		return null;
	}
}