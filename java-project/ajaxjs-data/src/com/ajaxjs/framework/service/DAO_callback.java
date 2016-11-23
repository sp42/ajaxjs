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

import java.util.List;

import com.ajaxjs.framework.dao.DAO;
import com.ajaxjs.framework.model.BaseModel;
import com.ajaxjs.framework.model.Query;

/**
 * 创建一个回调，方便复用分页功能
 * 
 * @author frank
 *
 * @param <T>
 * @param <Mapper>
 */
public interface DAO_callback<T extends BaseModel, Mapper extends DAO<T>> {
	/**
	 * 使得调用 DAO 时能够传入更多参数
	 * 
	 * @param dao
	 * @param start
	 * @param limit
	 * @param sql_TableName
	 * @param query
	 * @return
	 */
	public List<T> doIt(Mapper dao, int start, int limit, String sql_TableName, Query query);
}
