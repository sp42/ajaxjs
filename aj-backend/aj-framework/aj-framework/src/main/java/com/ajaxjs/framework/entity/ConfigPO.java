package com.ajaxjs.framework.entity;

import com.ajaxjs.framework.entity.model.BaseDataServiceConfig;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 通用接口的数据库配置 PO
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ConfigPO extends BaseDataServiceConfig {
    private Date createDate;
}
