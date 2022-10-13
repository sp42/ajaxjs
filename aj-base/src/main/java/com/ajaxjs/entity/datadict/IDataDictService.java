package com.ajaxjs.entity.datadict;

import java.util.List;

/**
 * 既是数据字典，也是树状接口的数据业务
 * 
 * @author Frank Cheung<sp42@qq.com>
 *
 */
public interface IDataDictService extends DataDictDao {
	/**
	 * 获取数据字典，包含父节点下面所有的子节点
	 * 
	 * @param parentId 树节点的父 id
	 * @return
	 */
	List<DataDict> getDataDictChildren(Long parentId);

	/**
	 * 获取数据字典，只是父节点下一级的子节点
	 * 
	 * @param parentId 树节点的父 id
	 * @return
	 */
	List<DataDict> getDataDict(Long parentId);

	/**
	 * 计算树节点的嵌套树深度
	 * 
	 * @param id 树节点的 id
	 * @return 树节点的嵌套树深度
	 */
	public Integer getDepthById(Long id);

	/**
	 * 创建数据字典
	 * 
	 * @param dataDict 数据字典实体
	 * @return 新创建的数据字典实体
	 */
	DataDict createDataDict(DataDict dataDict);

	/**
	 * 修改数据字典
	 * 
	 * @param dataDict 数据字典实体
	 * @return 是否修改成功
	 */
	Boolean updateDataDict(DataDict dataDict);

	/**
	 * 删除数据字典
	 * 
	 * @param id               数据字典 id
	 * @param isDeleteChildren 是否删除所有子节点
	 * @return 是否删除成功
	 */
	Boolean deleteDataDict(Long id, Boolean isDeleteChildren);
}
