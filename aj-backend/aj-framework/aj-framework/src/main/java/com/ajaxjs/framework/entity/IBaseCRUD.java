package com.ajaxjs.framework.entity;

import java.util.List;

import com.ajaxjs.framework.PageResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 通用的 CRUD 接口 for Controller
 *
 * @param <T> 实体类型
 * @param <K> 实体 id 类型，一般为 string or long
 */
public interface IBaseCRUD<T, K> {
    /**
     * 查询单笔实体
     *
     * @param id 实体 id
     * @return 实体
     */
//	@GetMapping("/{id}")
    default T info(@PathVariable K id) {
        return null;
    }

    /**
     * 列出所有记录
     *
     * @return 实体列表
     */
    default List<T> list() {
        return null;
    }

    /**
     * 分页列出记录
     *
     * @return 分页列表
     */
    default PageResult<T> page() {
        return null;
    }

    /**
     * 创建实体
     *
     * @param entity 实体
     * @return 新创建的 id
     */
//	@PostMapping
    default K create(@RequestBody T entity) {
        return null;
    }

    /**
     * 更新实体
     *
     * @param entity 实体
     * @return 是否成功
     */
//	@PutMapping
    default Boolean update(@RequestBody T entity) {
        return null;
    }

    /**
     * 删除实体
     *
     * @param id 实体 id
     * @return 是否成功
     */
//	@DeleteMapping("/{id}")
    default Boolean delete(@PathVariable K id) {
        return null;
    }

}
