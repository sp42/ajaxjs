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

/**
 * 业务逻辑的一个标识，用于注入
 * 
 * @author Frank
 *
 */
/**
 * 
 * @author xinzhang
 *
 * @param <ID>
 *            序号类型，可以是 INTEGER/LONG/String
 * @param <D>
 *            DAO 对象
 */
public interface IService<T, ID extends Serializable> {
	/**
	 * 查询单个记录。如果找不到则返回 null
	 * 
	 * @param id
	 *            序号
	 * @return POJO
	 */
	public T findById(ID id);

	/**
	 * 新建记录
	 * 
	 * @param bean
	 *            POJO 对象
	 * @return 新建记录之序号
	 */
	public ID create(T bean);

	/**
	 * 修改记录
	 * 
	 * @param bean
	 *            POJO 对象
	 * @return 影响的行数，理应 = 1
	 */
	public int update(T bean);

	/**
	 * 单个删除
	 * 
	 * @param id
	 *            实体序号
	 * @return 影响的行数
	 */
	public boolean delete(T bean);
	
	/**
	 * 
	 * @param param
	 * @return
	 */
	public PageResult<T> findPagedList(QueryParams parame);
	
	/**
	 * 返回业务名称，可用于 UI 显示
	 * 
	 * @return 业务名称
	 */
	String getName();
	
	/**
	 * 模版方法，用于装备其他数据，如分类这些外联的表。 不使用 abstract 修饰，因为这将强制各个子类都要实现，麻烦。
	 * 
	 * @param model
	 *            模型
	 */
	public void prepareData(ModelAndView model);
	
	
}
