package com.ajaxjs.database_meta.model;

import lombok.Data;

@Data
public class TableIndex {
    private String table;
    private String nonUnique;
    private String keyName;
    private String seqInIndex;
    private String columnName;
    private String collation;
    private String cardinality;
    private String subPart;
    private String packed;
    private String nullValue;
    private String indexType;
    private String comment;
    private String indexComment;
    private String visible;
    private String expression;
}
