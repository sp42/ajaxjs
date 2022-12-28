package com.ajaxjs.fast_doc;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ajaxjs.spring.easy_controller.ControllerMethod;
import com.ajaxjs.spring.easy_controller.Example;

/**
 * 抵近侦察区域控制器
 * 
 * @author Frank Cheung<sp42@qq.com>
 *
 */
@RestController
@RequestMapping("/close")
public interface FooController {
	/**
	 * 保存所有侦查点
	 * 
	 * @param taskId
	 * @param detectPoints
	 * @return
	 */
	@PostMapping("/detect_point/{taskId}")
	Boolean saveAllDetectPoint(@PathVariable @Example("task-20220232") String taskId, @RequestBody List<Map<String, Object>> detectPoints);

	/**
	 * 
	 * @param bean 提交的实体
	 * @return
	 */
	@PutMapping("/bean")
	@ControllerMethod("保存所有侦查点2")
	BarBean saveDetectPoint(@RequestBody FooBean bean);

	/**
	 * 航路规划 ABC
	 * 
	 * @param taskId 任务 id
	 * @return
	 */
	@GetMapping("/route_plan/{taskId}")
	@ControllerMethod("航路规划")
	@Example("[\"task-20220232\", \"task-20220233\"]")
	List<String> routePlan(@PathVariable String taskId);

}
