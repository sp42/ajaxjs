/*
 * Copyright 2015, Yahoo Inc.
 * Copyrights licensed under the Apache License.
 * See the accompanying LICENSE file for terms.
 */
package com.ajaxjs.mysql.model;

/**
 * An app specific object to be inserted into ResultList as part of result header
 *
 * @author xrao
 */
public interface CustomResultObject extends java.io.Serializable {
    /**
     * the name/key in json ResultList
     *
     * @return the name/key in json ResultList
     */
    String getName();

    /**
     * the value in json ResultList
     *
     * @return the value in json ResultList
     */
    String getValueJsonString();
}
