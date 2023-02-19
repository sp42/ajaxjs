package com.ajaxjs.auth.service.user;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.ajaxjs.spring.easy_controller.ControllerMethod;

public interface IOrgService {
	/**
	 * 删除节点
	 * 
	 * @param id
	 * @return
	 */
	@DeleteMapping("/{id}")
	@ControllerMethod("删除节点")
	boolean delete(@PathVariable Long id);
}
