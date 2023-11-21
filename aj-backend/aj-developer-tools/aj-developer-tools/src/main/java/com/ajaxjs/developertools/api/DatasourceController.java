package com.ajaxjs.developertools.api;

import com.ajaxjs.developertools.model.DataSourceInfo;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 数据源配置的 CRUD，另外有一些实用工具方法。
 *
 * @author Frank Cheung sp42@qq.com
 */
@RestController
@RequestMapping("/datasource")
public interface DatasourceController {
    /**
     * 列出数据源
     *
     * @return 数据源列表
     */
    @GetMapping
    List<DataSourceInfo> list();

    /**
     * 测试数据源是否连接成功
     *
     * @param id 数据源 id
     * @return true 表示成功
     */
    @GetMapping("/test/{id}")
    Boolean test(@PathVariable Long id);

    /**
     * 创建数据源
     *
     * @param entity 数据源实体
     * @return 数据源 id
     */
    @PostMapping
    Long create(DataSourceInfo entity);

    /**
     * 修改数据源
     *
     * @param entity 数据源实体
     * @return true 表示成功
     */
    @PutMapping
    Boolean update(DataSourceInfo entity);

    /**
     * 删除数据源
     *
     * @param id 数据源 id
     * @return true 表示成功
     */
    @DeleteMapping("/{id}")
    Boolean delete(@PathVariable Long id);
}
