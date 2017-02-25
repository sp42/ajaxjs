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
package test.com.ajaxjs.framework;

import com.ajaxjs.framework.service.BaseCrudService;
import com.ajaxjs.framework.service.IService;
import com.ajaxjs.framework.service.ServiceException;

public class NewsService extends BaseCrudService<News, NewsDAO> implements IService<News>{
	
	/**
	 * 映射器
	 */
//	private Class<NewsDAO> mapperClz = NewsDAO.class; 
	public NewsService() {
		setMapper(NewsDAO.class);
		setTableName("news");
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
