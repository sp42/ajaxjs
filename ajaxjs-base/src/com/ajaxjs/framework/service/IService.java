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

import java.io.Serializable;

import com.ajaxjs.framework.dao.QueryParams;
import com.ajaxjs.framework.model.ModelAndView;
import com.ajaxjs.framework.model.PageResult;
import com.ajaxjs.framework.service.annotation.ValidIt;
import com.ajaxjs.framework.service.annotation.ValidObj;

/**
 * 业务层不绑定 DAO 泛型，目的是不强制依赖 DAO
 * @author xinzhang
 *
 * @param <T>
 *            实体类型
 * @param <ID>
 *            ID 类型，可以是 INTEGER/LONG/String
 */
public interface IService<T, ID extends Serializable> {
	/**
	 * 查询单个记录。如果找不到则返回 null
	 * 
	 * @param id
	 *            序号
	 * @return POJO
	 */
	public T findById(ID id) throws ServiceException;

	/**
	 * 新建记录
	 * 
	 * @param bean
	 *            POJO 对象
	 * @return 新建记录之序号
	 */
	@ValidIt
	public ID create(@ValidObj T bean) throws ServiceException;

	/**
	 * 修改记录
	 * 
	 * @param bean
	 *            POJO 对象
	 * @return 影响的行数，理应 = 1
	 */
	public int update(T bean) throws ServiceException;

	/**
	 * 单个删除
	 * 
	 * @param bean
	 *            POJO 对象
	 * @return 影响的行数
	 */
	public boolean delete(T bean) throws ServiceException;

	/**
	 * 
	 * @param parame
	 * @return
	 * @throws ServiceException
	 */
	public PageResult<T> findPagedList(QueryParams parame) throws ServiceException;

	/**
	 * 返回业务名称，可用于 UI 显示
	 * 
	 * @return 业务名称
	 */
	String getName();

	/**
	 * 模版方法，用于装备其他数据，如分类这些外联的表。
	 * 
	 * @param model
	 *            模型
	 */
	public void prepareData(ModelAndView model);
}
