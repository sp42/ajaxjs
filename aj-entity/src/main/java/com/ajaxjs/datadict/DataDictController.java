package com.ajaxjs.datadict;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ajaxjs.spring.easy_controller.ControllerMethod;
import com.ajaxjs.spring.easy_controller.InterfaceBasedController;

@RestController
@InterfaceBasedController(serviceClass = DataDictService.class)
@RequestMapping("/data_dict")
public interface DataDictController {
	@GetMapping("/{parentId}")
	@ControllerMethod("获取数据字典，包含所有子节点")
	List<DataDict> getDataDictChildren(@PathVariable Long parentId);

	@GetMapping("/{parentId}")
	@ControllerMethod("获取数据字典，只是当期一级的节点")
	List<DataDict> getDataDict(@PathVariable Long parentId);

	@PostMapping
	@ControllerMethod("创建数据字典")
	DataDict createDataDict(DataDict dataDict);

	@PutMapping
	@ControllerMethod("创建数据字典")
	Boolean updateDataDict(DataDict dataDict);

	@PutMapping("/{id}")
	@ControllerMethod("删除数据字典")
	Boolean deleteDataDict(@PathVariable Long id, @RequestParam(required = false) Boolean isDeleteChildren);
}