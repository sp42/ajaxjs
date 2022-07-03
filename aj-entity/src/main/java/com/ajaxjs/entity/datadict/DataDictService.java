package com.ajaxjs.entity.datadict;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.ajaxjs.entity.CRUD;

/**
 * 数据字典，也是树状结构
 * 
 * @author Frank Cheung<sp42@qq.com>
 *
 */
@Service
public class DataDictService {
	List<DataDict> getDataDictChildren(Long parentId) {

		return null;
	}

	List<DataDict> getDataDict(Long parentId) {
		List<DataDict> list = DataDictDao.DataDictDAO.setWhereQuery("parentId", parentId).findList();

		if (CollectionUtils.isEmpty(list))
			list = new ArrayList<>();

		return list;
	}

	DataDict createDataDict(DataDict dataDict) {
		return CRUD.create(dataDict, DataDictDao.DataDictDAO);
	}

	Boolean updateDataDict(DataDict dataDict) {
		return CRUD.update(dataDict, DataDictDao.DataDictDAO);
	}

	Boolean deleteDataDict(Long id, Boolean isDeleteChildren) {
		if (isDeleteChildren) {

		}

		return DataDictDao.DataDictDAO.delete(id);
	}

}
