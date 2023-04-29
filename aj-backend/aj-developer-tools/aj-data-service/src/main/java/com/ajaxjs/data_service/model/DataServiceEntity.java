package com.ajaxjs.data_service.model;

import com.ajaxjs.framework.BaseModel;
import com.ajaxjs.framework.IBaseModel;
import lombok.Data;

import java.util.Map;

/**
 * 配置实体，映射数据库里面的记录
 *
 * @author Frank Cheung
 */
@Data
public class DataServiceEntity extends BaseModel implements IBaseModel {
    private String tableName;

    private String urlDir;

    private String urlRoot;

    /**
     * 其他额外的配置信息
     */
    private String config;

    private Long projectId;

    private Long datasourceId;

    private String datasourceName;

    private String content;

    private String tags;

    /**
     * 主键生成策略 1=自增 2=雪花 3=uuid
     */
    private Integer keyGen;

    /**
     * key 字段名，value 字段注释
     */
    private Map<String, String> fields;

    private Map<String, Object> data;

    /**
     * 字段映射
     */
    private DataServiceFieldsMapping fieldsMapping;

    private String json;
}
