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
package com.ajaxjs.framework.testcase;

import java.util.List;

import com.ajaxjs.framework.News;
import com.ajaxjs.framework.service.CommonTestService;
import com.ajaxjs.ioc.Bean;
import com.ajaxjs.orm.dao.DaoHandler;
import com.ajaxjs.orm.dao.PageResult;

/**
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
@Bean(value = "newsService", aop = { CommonTestService.class})
public class NewsServiceImpl implements NewsService {
	private NewsDao dao = new DaoHandler().bind(NewsDao.class);// 实例化 DAO。因为是 class 所以不能注入，于是一般在 Service
																		// 构造器里面调用该方法

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

	public News findById(Long id) {
		return dao.findById(id);
	}

	public Long create(News bean) {
		Long id = dao.create(bean);
		bean.setId(id);
		return bean.getId();
	}

	public int update(News bean) {
		return dao.update(bean);
	}

	public boolean delete(News bean) {
		return dao.delete(bean);
	}

	public PageResult<News> findPagedList(int start, int limit) {
		return findPagedList(start, limit);
	}

	@Override
	public List<News> findList() {
		return null;
	}

}