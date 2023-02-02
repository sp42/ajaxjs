package com.util;


import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.Properties;
import java.util.Set;
 
/**
 * 读配置文件可以保持顺序的 Java Properties 类
 * 
 * OrderedProperties
 * @author liii  https://blog.csdn.net/happylee6688/article/details/46894131
 * @date 2015-07-10
 */
public class OrderedProperties extends Properties {
    private static final long serialVersionUID = -4627607243846121965L;
     
    private final LinkedHashSet<Object> keys = new LinkedHashSet<>();
 
    public Enumeration<Object> keys() {
        return Collections.<Object> enumeration(keys);
    }
 
    public Object put(Object key, Object value) {
        keys.add(key);
        return super.put(key, value);
    }
 
    public Set<Object> keySet() {
        return keys;
    }
 
    public Set<String> stringPropertyNames() {
        Set<String> set = new LinkedHashSet<String>();
 
        for (Object key : this.keys) {
            set.add((String) key);
        }
 
        return set;
    }
}