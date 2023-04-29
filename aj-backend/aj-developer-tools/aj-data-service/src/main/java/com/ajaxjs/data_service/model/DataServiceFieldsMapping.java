package com.ajaxjs.data_service.model;

import lombok.Data;

/**
 * 不同字段的映射
 */
@Data
public class DataServiceFieldsMapping {
    private String id;

    private String createDate;

    private String createUser;

    private String updateDate;

    private String updateUser;

    private String delStatus;

    private Boolean dbStyle2CamelCase;

    private Boolean camelCase2DbStyle;
}
