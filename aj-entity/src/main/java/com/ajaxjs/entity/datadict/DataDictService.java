package com.ajaxjs.entity.datadict;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

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
public class DataDictService {
	public List<DataDict> getDataDictChildren(Long parentId) {

		return null;
	}

	@Autowired
	LocalValidatorFactoryBean v;

	public List<DataDict> getDataDict(Long parentId) {
		List<DataDict> list = DataDictDao.DataDictDAO.setWhereQuery("parentId", parentId).findList();

		Validator validator = v.getValidator();
		Set<ConstraintViolation<DataDict>> violations = validator.validate(list.get(0));
		System.out.println(violations.size()); // 校验结果

		System.out.println(list.get(0).getParentId());
		if (CollectionUtils.isEmpty(list))
			list = new ArrayList<>();

		return list;
	}

	public DataDict createDataDict(DataDict dataDict) {
		return CRUD.create(dataDict, DataDictDao.DataDictDAO);
	}

	public Boolean updateDataDict(DataDict dataDict) {
		return CRUD.update(dataDict, DataDictDao.DataDictDAO);
	}

	public Boolean deleteDataDict(Long id, Boolean isDeleteChildren) {
		if (isDeleteChildren) {

		}

		return DataDictDao.DataDictDAO.delete(id);
	}

}
