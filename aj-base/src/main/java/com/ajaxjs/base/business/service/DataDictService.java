package com.ajaxjs.base.business.service;

import java.util.List;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.ajaxjs.base.business.model.DataDict;
import com.ajaxjs.spring.easy_controller.ControllerMethod;

/**
 * 既是数据字典，也是树状接口的数据业务
 * 
 * @author Frank Cheung<sp42@qq.com>
 *
 */
public interface DataDictService extends DataDictDao {
	/**
	 * 获取数据字典，包含父节点下面所有的子节点
	 * 
	 * @param parentId 树节点的父 id
	 * @return
	 */
	@GetMapping("/{parentId}/children")
	@ControllerMethod("获取数据字典，包含父节点下面所有的子节点")
	List<DataDict> getDataDictChildren(@PathVariable Long parentId);

	/**
	 * 获取数据字典，只是父节点下一级的子节点
	 * 
	 * @param parentId 树节点的父 id
	 * @return
	 */
	@GetMapping("/{parentId}")
	@ControllerMethod("获取数据字典，只是父节点下一级的子节点")
	List<DataDict> getDataDict(@PathVariable Long parentId);

	/**
	 * 计算树节点的嵌套树深度
	 * 
	 * @param id 树节点的 id
	 * @return 树节点的嵌套树深度
	 */
	@GetMapping("/{id}/getDepthById")
	@ControllerMethod("计算树节点的嵌套树深度")
	public Integer getDepthById(@PathVariable Long id);

	/**
	 * 创建数据字典
	 * 
	 * @param dataDict 数据字典实体
	 * @return 新创建的数据字典实体
	 */
	@PostMapping
	@ControllerMethod("创建数据字典")
	DataDict createDataDict(@Valid @RequestBody DataDict dataDict);

	/**
	 * 修改数据字典
	 * 
	 * @param dataDict 数据字典实体
	 * @return 是否修改成功
	 */
	@PutMapping
	@ControllerMethod("创建数据字典")
	Boolean updateDataDict(DataDict dataDict);

	/**
	 * 删除数据字典
	 * 
	 * @param id               数据字典 id
	 * @param isDeleteChildren 是否删除所有子节点
	 * @return 是否删除成功
	 */
	@DeleteMapping("/{id}")
	@ControllerMethod("删除数据字典")
	Boolean deleteDataDict(@PathVariable Long id, @RequestParam(required = false) Boolean isDeleteChildren);
}
