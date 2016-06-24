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
import java.util.Map;

import org.apache.ibatis.session.SqlSession;

import com.ajaxjs.framework.dao.MyBatis;
import com.ajaxjs.framework.dao.SqlProvider;
import com.ajaxjs.framework.exception.DaoException;

public class EntityUtil {
	public static List<Map<String, String>> getNeighbor(String tablename, long id) throws DaoException {
		List<Map<String, String>> neighbors = null;
		try {
			if (!MyBatis.configuration.hasMapper(SqlProvider.class)) {
				MyBatis.configuration.addMapper(SqlProvider.class);
			}
			
			SqlSession session = MyBatis.sqlSessionFactory.openSession();

			try {
				SqlProvider _mapper = session.getMapper(SqlProvider.class);
				neighbors = _mapper.getNeighbor(tablename, id);
			} catch (Throwable e) {
				throw e;
			} finally {
				session.close();
			}
		} catch (Throwable e) {
			e.printStackTrace();
//			LOGGER.warning(e);
			throw new DaoException(e.getMessage());
		}
		
		return neighbors;
	}
}
