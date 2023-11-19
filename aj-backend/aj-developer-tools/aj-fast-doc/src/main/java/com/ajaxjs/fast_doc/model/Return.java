package com.ajaxjs.fast_doc.model;

import com.ajaxjs.fast_doc.doclet.DocModel;

/**
 * 返回值信息
 *
 * @author Frank Cheung sp42@qq.com
 */
public  class Return extends CommonValue {
    /**
     * 返回值是否 List或 Array
     */
    public boolean isMany;

    /**
     * true = JavaBean/false = String/Boolean/Number
     */
    public boolean isObject;

    /**
     * 例子
     */
    public String example;

    /**
     * 如果返回值是一个 bean，这里说明 bean 的各个字段
     */
    public DocModel.FieldInfo[] fields;
}