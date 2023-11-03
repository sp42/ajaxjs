//package com.ajaxjs.base.business.service;
//
//import com.ajaxjs.base.business.controller.DataDictService;
//import com.ajaxjs.base.business.model.DataDict;
//import com.ajaxjs.data.CRUD;
//import com.ajaxjs.util.StrUtil;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.util.CollectionUtils;
//import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
//
//import java.util.Collections;
//import java.util.List;
//
///**
// * 数据字典，也是树状结构
// *
// * @author Frank Cheung sp42@qq.com
// *
// */
//@Service
//public class DataDictServiceImpl implements DataDictService {
//	public List<DataDict> getDataDictChildren(Long parentId) {
//		List<DataDict> list = DataDictDAO.getListByParentId(parentId);
//
//		if (CollectionUtils.isEmpty(list))
//			list = Collections.emptyList();
//
//		return list;
//	}
//
//	@Autowired
//	LocalValidatorFactoryBean v;
//
//	@Override
//	public List<DataDict> getDataDict(Long parentId) {
//		List<DataDict> list = DataDictDAO.setWhereQuery("parentId", parentId).findList();
//
//		if (CollectionUtils.isEmpty(list))
//			list = Collections.emptyList();
//
//		return list;
//	}
//
//	@Override
//	public List<DataDict> getListByParentId(Long pid) {
//		return null;
//	}
//
//	@Override
//	public Integer getDepthById(Long id) {
//		return DataDictDAO.getDepthById(id);
//	}
//
//	@Override
//	public Boolean deleteChildren(String ids) {
//		return null;
//	}
//
//	@Override
//	public DataDict createDataDict(DataDict dataDict) {
//		if (dataDict.getParentId() == null)
//			throw new IllegalArgumentException("缺少 parentId 参数");
//
//		CRUD.create(dataDict);
//
//		return dataDict;
//	}
//
//	@Override
//	public Boolean updateDataDict(DataDict dataDict) {
//		return CRUD.update(dataDict, DataDictDAO);
//	}
//
//	@Override
//	public Boolean deleteDataDict(Long id, Boolean isDeleteChildren) {
//		if (isDeleteChildren) {
//			Long[] ids = CRUD.getIds(getDataDictChildren(id));
//
//			if (ids.length > 0)
//				DataDictDAO.deleteChildren(StrUtil.join(ids, ","));
//
//			return false;
//		} else
//
//			return CRUD.delete(id, DataDictDAO, DataDict.class);
//	}
//
//}
