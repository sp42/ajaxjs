/*
 * Copyright 2015, Yahoo Inc.
 * Copyrights licensed under the Apache License.
 * See the accompanying LICENSE file for terms.
 */
package com.ajaxjs.mysql.config;

import java.util.Map;
import java.util.TreeMap;

import lombok.Data;

/**
 * A config block includes the time the data was gathered and the configuration
 * data in name=value pair The data will be persisted in blocks, starting with
 * time in square bracket, followed by a list of lines of name value pair. The
 * time is in yyyy-MM-dd HH:mm:ss (UTC) format.
 *
 * @author xrao
 */
@Data
public class ConfigBlock {
    private String time;

    private Map<String, String> variables = new TreeMap<>();

    public void addVariable(String name, String value) {
        variables.put(name, value);
    }
}
