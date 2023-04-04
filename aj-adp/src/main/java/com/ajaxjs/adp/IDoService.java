package com.ajaxjs.adp;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.ajaxjs.framework.spring.easy_controller.anno.ControllerMethod;

/**
 * 探障任务
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
public interface IDoService  {
	/**
	 * 获取子区域切片和飞机
	 * 
	 * @param taskId 任务 id
	 * @return 子区域切片和飞机
	 */
	@GetMapping("/{taskId}")
	@ControllerMethod("获取子区域切片和飞机")
	String getSubAreaUav(@PathVariable Long taskId);

}
