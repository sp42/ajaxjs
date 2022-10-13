package com.ajaxjs.entity.datadict;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import com.ajaxjs.data_service.sdk.IDataService;
import com.ajaxjs.entity.CRUD;
import com.ajaxjs.framework.PageResult;
import com.ajaxjs.util.StrUtil;

/**
 * 数据字典，也是树状结构
 * 
 * @author Frank Cheung<sp42@qq.com>
 *
 */
@Service
public class DataDictServiceImpl implements IDataDictService {
	public List<DataDict> getDataDictChildren(Long parentId) {
		List<DataDict> list = DataDictDAO.getListByParentId(parentId);

		if (CollectionUtils.isEmpty(list))
			list = Collections.emptyList();

		return list;
	}

	@Autowired
	LocalValidatorFactoryBean v;

	@Override
	public List<DataDict> getDataDict(Long parentId) {
		List<DataDict> list = DataDictDAO.setWhereQuery("parentId", parentId).findList();

		if (CollectionUtils.isEmpty(list))
			list = Collections.emptyList();

		return list;
	}

	@Override
	public Integer getDepthById(Long id) {
		return DataDictDAO.getDepthById(id);
	}

	@Override
	public DataDict createDataDict(DataDict dataDict) {
		if (dataDict.getParentId() == null)
			throw new IllegalArgumentException("缺少 parentId 参数");

		return CRUD.create(dataDict, DataDictDAO);
	}

	@Override
	public Boolean updateDataDict(DataDict dataDict) {
		return CRUD.update(dataDict, DataDictDAO);
	}

	@Override
	public Boolean deleteDataDict(Long id, Boolean isDeleteChildren) {
		if (isDeleteChildren) {
			Long[] ids = CRUD.getIds(getDataDictChildren(id));

			if (ids.length > 0)
				DataDictDAO.deleteChildren(StrUtil.join(ids, ","));

			return false;
		} else

			return CRUD.delete(id, DataDictDAO, DataDict.class);
	}

	@Override
	public List<DataDict> getListByParentId(Long pid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean deleteChildren(String ids) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DataDict findById(Serializable id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DataDict findOne() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<DataDict> findList() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PageResult<DataDict> findPagedList(int start, int limit) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Serializable create(DataDict bean) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean update(DataDict bean) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public IDataService<DataDict> setQuery(Map<String, Object> queryParams) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IDataService<DataDict> setWhereQuery(Map<String, Object> queryWhereParams) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IDataService<DataDict> setWhereQuery(String where) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IDataService<DataDict> setWhereQuery(String fieldName, Object value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> findByIdAsMap(Serializable id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Map<String, Object>> findListAsListMap() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PageResult<Map<String, Object>> findPagedListAsMap(int start, int limit) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Serializable create(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean update(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean delete(Object beanOrMapOrId) {
		// TODO Auto-generated method stub
		return null;
	}

}
