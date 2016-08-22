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

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;

import com.ajaxjs.framework.dao.DAO;
import com.ajaxjs.framework.dao.MyBatis;
import com.ajaxjs.framework.dao.SqlProvider;
import com.ajaxjs.framework.exception.BusinessException;
import com.ajaxjs.framework.exception.DaoException;
import com.ajaxjs.framework.exception.ServiceException;
import com.ajaxjs.framework.model.BaseModel;
import com.ajaxjs.framework.model.ModelAndView;
import com.ajaxjs.framework.model.PageResult;
import com.ajaxjs.framework.model.Query;
import com.ajaxjs.util.LogHelper;
import com.ajaxjs.util.StringUtil;
import com.ajaxjs.util.Util;

/**
 * 基础业务类，围绕数据库的增删改查。
 * 
 * @author frank
 *
 * @param <T>
 *            POJO
 * @param <Mapper>
 *            DAO
 */
public abstract class BaseCrudService<T extends BaseModel, Mapper extends DAO<T>> implements IService<T> {
	private static final LogHelper LOGGER = LogHelper.getLog(BaseCrudService.class);
	private Class<Mapper> mapperClz; 	// 映射器
	private String uiName; 				// UI 显示的文字
	private String tableName; 			// 实体表名
	private String mappingTableName; 	// 数据库里面真实的表名，可不设置（这时候读取 tableName 的）
	private ModelAndView model; 	// Extra data field container
	
	@Override
	public T getById(long id) throws ServiceException {
		T entry = null;

		try {
			if (id < 0)
				throw new IllegalArgumentException("实体 id 不能小于零");
		} catch (Throwable e) {
			LOGGER.warning(e);
			throw new BusinessException(e.getMessage());
		}

		try (SqlSession session = MyBatis.loadSession(mapperClz);) {
			Mapper _mapper = session.getMapper(mapperClz);
			entry = _mapper.selectById(id, getSQL_TableName());
		} catch (Throwable e) {
			LOGGER.warning(e);
			throw new DaoException(e.getMessage());
		}

		return entry;
	}

	public T getByUUID(String uuid) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public PageResult<T> getPageRows(int start, int limit, Query query) throws ServiceException {
		PageResult<T> result = null;

		try {
			if (start < 0 || limit < 0)
				throw new IllegalArgumentException("分页参数非法");
		} catch (Throwable e) {
			LOGGER.warning(e);
			throw new BusinessException(e.getMessage());
		}

		result = new PageResult<>();
		result.setStart(start);
		result.setPageSize(limit);
		
		try (SqlSession session = MyBatis.loadSession(mapperClz);){
			Mapper dao = session.getMapper(mapperClz);
			
			if (query == null) 
				query = new Query(){}; // 空，因为 MyBatis22 传 null 报错：result.setRows(dao.page(start, limit, getTableName(), query));
			
			if(getHidden_db_field_mapping().size() > 0) // 字段映射
				query.setDb_field_mapping(getHidden_db_field_mapping());
			
			// 先查询总数
			result.setTotalCount(dao.pageCount(getSQL_TableName(), query));

			if (result.getTotalCount() > 0) { // 然后执行分页
				result.page();
				result.setRows(dao.page(start, limit, getSQL_TableName(), query));
			} else {
//				result.setRows(null);
			}
		} catch(Throwable e) {
			e.printStackTrace();
			LOGGER.warning(e);
			throw new DaoException(e.getMessage());
		} 
	
		return result;
	}
	
	@Override
	public List<T> getAll(Query query) {
		PageResult<T> result;
		
		try {
			result = query == null ? getPageRows(0, 999, null) : getPageRows(0, 999, query);
		} catch (ServiceException e) {
			LOGGER.warning(e);
			return null;
		}
		
		if (result == null || result.getTotalCount() == 0) {
			return null;
		} else {
			return result.getRows();
		}
	}

	@Override
	public List<T> getAll() {
		return getAll(null);
	}
	
	@Override
	public int create(T entry) throws ServiceException {
		int effectedRows = 0; // 受影响的行数

		try {
			if (StringUtil.isEmptyString(entry.getName())) {
				throw new IllegalAccessException("不能为空标题");
			}

			entry.setUid(Util.getUUID()); // 创建 uuid

			Date now = new Date();// 记录创建時間
			if(entry.getCreateDate() == null) {
				entry.setCreateDate(now); // 如果不指定创建时间，则采用当前时期。即允许 创建时间可以指定
			}
			entry.setUpdateDate(now);
		} catch (Throwable e) {
			LOGGER.warning(e);
			throw new BusinessException(e.getMessage());
		}
		
		LOGGER.info("插入一条新记录：" + entry.getName());
		
		try (SqlSession session = MyBatis.loadSession(mapperClz);) {
			if (!MyBatis.configuration.hasMapper(mapperClz)) {
				MyBatis.configuration.addMapper(mapperClz);
			}
			
			Mapper dao = session.getMapper(mapperClz);
			effectedRows = dao.create(entry);

			session.commit();
			if(effectedRows <= 0) {
				throw new RuntimeException("新建记录失败！");
			}
		} catch (Throwable e) {
			LOGGER.warning(e);
			throw new DaoException(e.getMessage());
		}

		return effectedRows;
	}

	@Override
	public boolean update(T entry) throws ServiceException {
		int effectedRows = 0; // 受影响的行数

		try {
			if (entry.getId() == 0) {
				throw new IllegalArgumentException("没有参数 id！");
			}
			if (StringUtil.isEmptyString(entry.getName())) {
				throw new IllegalArgumentException("不能为空标题");
			}

			entry.setUpdateDate(new Date());// 记录修改時間
		} catch (Throwable e) {
			LOGGER.warning(e);
			throw new BusinessException(e.getMessage());
		}
		
		try(SqlSession session = MyBatis.loadSession(mapperClz);) {
			effectedRows = session.getMapper(mapperClz).update(entry);
			session.commit();
		} catch (Throwable e) {
			LOGGER.warning(e);
			throw new DaoException(e.getMessage());
		}

		return effectedRows > 0;
	}

	@Override
	public boolean delete(T entry)  throws ServiceException {
		return deleteByID(entry.getId());
	}

	@Override
	public boolean deleteByID(long id) throws ServiceException {
		int effectedRows = 0; // 受影响的行数
		
		try {
			if (id == 0) throw new IllegalArgumentException("没有参数 id！");
		} catch (Throwable e) {
			LOGGER.warning(e);
			throw new BusinessException(e.getMessage());
		}
		
		SqlProvider p = null;
		
		try(SqlSession session = MyBatis.loadSession(null);){
			p = session.getMapper(SqlProvider.class);
			effectedRows = p.deleteById(getSQL_TableName(), id);
			session.commit();
		} catch (Throwable e) {
			LOGGER.warning(e);
			throw new DaoException(e.getMessage());
		}

		return effectedRows > 0;
	}
	
	public void createTable(T entry) throws DaoException {
	}
	
	/**
	 * 如果有 mappingTableName 则返回；如果没有则返回 tableName。
	 * 
	 * @return
	 */
	@Override
	public String getSQL_TableName() {
		if (getMappingTableName() != null) {
			return getMappingTableName();
		} else
			return getTableName();
	}
	
	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getUiName() {
		return uiName;
	}

	public void setUiName(String uiName) {
		this.uiName = uiName;
	}

	public void setMapper(Class<Mapper> mapper) {
		this.mapperClz = mapper;
	}

	public String getMappingTableName() {
		return mappingTableName;
	}

	public void setMappingTableName(String mappingTableName) {
		this.mappingTableName = mappingTableName;
	}

	/**
	 * MVC 通过 Model 交换数据。没 model 表示不深入获取信息
	 */
	@Override
	public ModelAndView getModel() {
		return model;
	}

	@Override
	public void setModel(ModelAndView model) {
		this.model = model;
	}
	
	// 表映射
	private Map<String, String> hidden_db_field_mapping = new HashMap<>();

	public Map<String, String> getHidden_db_field_mapping() {
		return hidden_db_field_mapping;
	}

	public void setHidden_db_field_mapping(Map<String, String> hidden_db_field_mapping) {
		this.hidden_db_field_mapping = hidden_db_field_mapping;
	}
}
