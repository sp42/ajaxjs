package com.ajaxjs.fast_doc.model;

import com.ajaxjs.fast_doc.doclet.DocModel;

/**
 * 参数信息
 *
 * @author Frank Cheung sp42@qq.com
 */
public  class ArgInfo extends CommonValue {
    /**
     * 参数的位置，也就是参数的类型
     */
    public String position;

    /**
     * 是否必填
     */
    public boolean isRequired;

    /**
     * 默认值
     */
    public String defaultValue;

    /**
     * 例子
     */
    public String example;

    /**
     * 如果参数是一个 bean，这里说明 bean 的各个字段
     */
    public DocModel.FieldInfo[] fields;
}