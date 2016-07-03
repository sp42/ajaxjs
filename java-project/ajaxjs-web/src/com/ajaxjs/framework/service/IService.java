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

import com.ajaxjs.framework.exception.ServiceException;
import com.ajaxjs.mvc.controller.ModelAndView;
import com.ajaxjs.mvc.model.Query;
import com.ajaxjs.mvc.view.PageResult;

/**
 * 业务逻辑层
 * 
 * @author Frank
 *
 * @param <T>
 */
public interface IService<T> {
	/**
	 * 获取单个实体
	 * 
	 * @param id
	 *            实体之 id
	 * @return POJO
	 * @throws ServiceException
	 */
	public T getById(long id) throws ServiceException;

	/**
	 * 分页
	 * 
	 * @param start
	 *            起始行数
	 * @param limit
	 *            偏量值
	 * @param query
	 *            查询条件
	 * @return
	 * @throws ServiceException
	 */
	public PageResult<T> getPageRows(int start, int limit, Query query) throws ServiceException;

	/**
	 * 根据 query 查询条件来查询记录，不分页。如果 query 为 null 则表示查询所有记录，与 getAll() 作用相同。
	 * 
	 * @param query
	 *            查询条件。如果 query 为 null 则表示查询所有记录，与 getAll() 作用相同。
	 * @return 记录的列表
	 * @throws ServiceException
	 */
	public List<T> getAll(Query query) throws ServiceException;

	/**
	 * 查询所有记录
	 * 
	 * @return 所有记录的列表
	 * @throws ServiceException
	 */
	public List<T> getAll() throws ServiceException;

	/**
	 * 创建记录
	 * 
	 * @param entry
	 *            实体
	 * @return 创建新实体之 id，若 < 0 则创建失败。
	 * @throws ServiceException
	 */
	public int create(T entry) throws ServiceException;

	/**
	 * 更新记录
	 * 
	 * @param entry
	 *            实体
	 * @return true 表示为更新成功，false 表示更新失败。
	 * @throws ServiceException
	 */
	public boolean update(T entry) throws ServiceException;

	/**
	 * 删除记录
	 * 
	 * @param entry
	 *            实体
	 * @return true 表示为删除成功，false 表示删除失败。
	 * @throws ServiceException
	 */
	public boolean delete(T entry) throws ServiceException;

	/**
	 * 传入实体 id 删除记录
	 * 
	 * @param id
	 *            实体 id
	 * @return true 表示为删除成功，false 表示删除失败。
	 * @throws ServiceException
	 */
	public boolean deleteByID(long id) throws ServiceException;

	/**
	 * 实体表名
	 * 
	 * @return
	 */
	public String getTableName();

	/**
	 * UI 显示的文字
	 * 
	 * @return
	 */
	public String getUiName();

	/**
	 * 数据库里面真实的表名，可不设置（这时候读取 tableName 的）
	 * 
	 * @return
	 */
	public String getMappingTableName();

	/**
	 * 字段名映射
	 * 
	 * @return
	 */
	public Map<String, String> getHidden_db_field_mapping();

	public String getSQL_TableName();

	/**
	 * 设置一个模型
	 * 
	 * @param model
	 */
	public void setModel(ModelAndView model);

	public ModelAndView getModel();
}
