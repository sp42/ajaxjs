package com.ajaxjs.framework;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 
 * @author Frank Cheung sp42@qq.com
 *
 */
public interface IBaseController<T> {
	/**
	 * 
	 * 
	 * @param id
	 * @return
	 */
	@GetMapping("/{id}")
	default T Info(@PathVariable long id) {
		return null;
	}

	/**
	 * 列出所有记录
	 * 
	 * @return
	 */
	@GetMapping
	default List<T> list() {
		return null;
	}

	/**
	 * 分页列出记录
	 * 
	 * @return
	 */
	@GetMapping("/page")
	default PageResult<T> page() {
		return null;
	}

	/**
	 * 
	 * @param entity
	 * @return
	 */
	@PostMapping
	default T create(@RequestBody T entity) {
		return null;
	}

	/**
	 * 
	 * @param entity
	 * @return
	 */
	@PutMapping
	default Boolean update(@RequestBody T entity) {
		return null;
	}

	/**
	 * 
	 * @param id
	 * @return
	 */
	@DeleteMapping("/{id}")
	default Boolean delete(@PathVariable long id) {
		return null;
	}

}
