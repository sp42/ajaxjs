package com.ajaxjs.entity.datadict;

import java.util.List;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ajaxjs.spring.easy_controller.ControllerMethod;
import com.ajaxjs.spring.easy_controller.InterfaceBasedController;

@RestController
@InterfaceBasedController(serviceClass = DataDictServiceImpl.class)
@RequestMapping("/data_dict")
public interface DataDictController extends IDataDictService {
	@GetMapping("/{parentId}/children")
	@ControllerMethod("获取数据字典，包含父节点下面所有的子节点")
	@Override
	List<DataDict> getDataDictChildren(@PathVariable Long parentId);

	@GetMapping("/{parentId}")
	@ControllerMethod("获取数据字典，只是父节点下一级的子节点")
	@Override
	List<DataDict> getDataDict(@PathVariable Long parentId);

	@GetMapping("/{id}/getDepthById")
	@ControllerMethod("计算树节点的嵌套树深度")
	@Override
	public Integer getDepthById(@PathVariable Long id);

	@PostMapping
	@ControllerMethod("创建数据字典")
	@Override
	DataDict createDataDict(@Valid @RequestBody DataDict dataDict);

	@PutMapping
	@ControllerMethod("创建数据字典")
	@Override
	Boolean updateDataDict(DataDict dataDict);

	@DeleteMapping("/{id}")
	@ControllerMethod("删除数据字典")
	@Override
	Boolean deleteDataDict(@PathVariable Long id, @RequestParam(required = false) Boolean isDeleteChildren);
}