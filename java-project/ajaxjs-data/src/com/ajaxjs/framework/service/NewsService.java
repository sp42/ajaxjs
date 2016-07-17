/**
 * Copyright 2015 Frank Cheung
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
package com.ajaxjs.framework.service;

import com.ajaxjs.framework.dao.NewsDAO;
import com.ajaxjs.framework.exception.ServiceException;
import com.ajaxjs.framework.model.News;

public class NewsService extends BaseCrudService<News, NewsDAO> {
	public NewsService() {
		setMapper(NewsDAO.class);
		setTableName("NEWS");
		setUiName("视频");
//		setReference(News.class);
	}
	
	@Override
	public News getById(long id) throws ServiceException {
		News bean = super.getById(id);
		
		if(bean != null && getModel() != null) {
//			Map<String, Object> model = getModel();
//			Common.getLinkDataForInfo(bean, model);
		}
		
		return bean;
	}

	
}
