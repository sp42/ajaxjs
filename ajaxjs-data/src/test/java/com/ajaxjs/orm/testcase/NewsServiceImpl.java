/**
 * Copyright sp42 frank@ajaxjs.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ajaxjs.orm.testcase;

import com.ajaxjs.framework.service.ServiceException;
import com.ajaxjs.framework.service.aop.CacheService;
import com.ajaxjs.framework.service.aop.CommonTestService;
import com.ajaxjs.ioc.Bean;
import com.ajaxjs.orm.dao.DaoHandler;
import com.ajaxjs.orm.dao.PageResult;
import com.ajaxjs.orm.dao.QueryParams;

/**
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
@Bean(value = "newsService", aop = { CommonTestService.class, CacheService.class })
public class NewsServiceImpl implements NewsService {
	private NewsDao dao = new DaoHandler<NewsDao>().bind(NewsDao.class);// 实例化 DAO。因为是 class 所以不能注入，于是一般在 Service 构造器里面调用该方法

	@Override
	public String getName() {
		return "新闻";
	}

	@Override
	public String getTableName() {
		return "news";
	}

	public News getFirstNews() {
		News news = dao.findById(1L);
		return news;
	}

	@Override
	public News findById(Long id) {
		return dao.findById(id);
	}

	@Override
	public Long create(News bean) throws ServiceException {
		Long id = dao.create(bean);
		bean.setId(id);
		return bean.getId();
	}

	@Override
	public int update(News bean) {
		return dao.update(bean);
	}

	@Override
	public boolean delete(News bean) {
		return dao.delete(bean);
	}

	@Override
	public PageResult<News> findPagedList(QueryParams params, int start, int limit) {
		return dao.findPagedList(params, start, limit);
	}

	@Override
	public PageResult<News> findPagedList(int start, int limit) throws ServiceException {
		return findPagedList(start, limit);
	}

}