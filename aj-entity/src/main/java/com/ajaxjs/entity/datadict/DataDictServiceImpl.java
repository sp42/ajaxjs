package com.ajaxjs.entity.datadict;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import com.ajaxjs.entity.CRUD;

/**
 * 数据字典，也是树状结构
 * 
 * @author Frank Cheung<sp42@qq.com>
 *
 */
@Service
public class DataDictServiceImpl implements IDataDictService {
	public List<DataDict> getDataDictChildren(Long parentId) {

		return null;
	}

	@Autowired
	LocalValidatorFactoryBean v;

	@Override
	public List<DataDict> getDataDict(Long parentId) {
		List<DataDict> list = DataDictDao.DataDictDAO.setWhereQuery("parentId", parentId).findList();

		if (CollectionUtils.isEmpty(list))
			list = new ArrayList<>();

		return list;
	}

	@Override
	public DataDict createDataDict(DataDict dataDict) {
		System.out.println(dataDict.getId());
		return CRUD.create(dataDict, DataDictDao.DataDictDAO);
	}

	@Override
	public Boolean updateDataDict(DataDict dataDict) {
		return CRUD.update(dataDict, DataDictDao.DataDictDAO);
	}

	@Override
	public Boolean deleteDataDict(Long id, Boolean isDeleteChildren) {
		if (isDeleteChildren) {

		}

		return DataDictDao.DataDictDAO.delete(id);
	}

}
