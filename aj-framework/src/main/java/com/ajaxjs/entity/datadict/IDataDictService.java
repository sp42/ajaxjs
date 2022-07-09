package com.ajaxjs.entity.datadict;

import java.util.List;

public interface IDataDictService {
	List<DataDict> getDataDictChildren(Long parentId);

	List<DataDict> getDataDict(Long parentId);

	DataDict createDataDict(DataDict dataDict);

	Boolean updateDataDict(DataDict dataDict);

	Boolean deleteDataDict(Long id, Boolean isDeleteChildren);
}
