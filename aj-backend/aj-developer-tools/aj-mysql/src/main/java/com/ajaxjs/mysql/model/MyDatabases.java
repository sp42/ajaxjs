/*
 * Copyright 2015, Yahoo Inc.
 * Copyrights licensed under the Apache License.
 * See the accompanying LICENSE file for terms.
 */
package com.ajaxjs.mysql.model;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class MyDatabases implements java.io.Serializable {
    private static final long serialVersionUID = -8586381924495834726L;

    private final Set<String> myDbSet = new TreeSet<>();

    synchronized public Set<String> getMyDbList() {
        return Collections.unmodifiableSet(myDbSet);
    }

    synchronized public void addDb(String name) {
        myDbSet.add(name);
    }

    synchronized public void addDbs(List<String> names) {
        myDbSet.addAll(names);
    }

    synchronized public void removeDb(String name) {
        myDbSet.remove(name);
    }

    synchronized public void replaceDb(String oldName, String newName) {
        if (!myDbSet.contains(oldName)) {
            myDbSet.remove(oldName);
            myDbSet.remove(newName);
        }
    }

    synchronized public int size() {
        return myDbSet.size();
    }
}
