package com.ajaxjs.fast_doc.model;

import java.util.List;

/**
 * 控制器方法的信息
 *
 * @author Frank Cheung sp42@qq.com
 */
public  class Item implements Comparable<Item> {
    public String id;

    public String name;

    public String description;

    public String methodName;

    public String httpMethod;

    public String url;

    public String image;

    public List<ArgInfo> args;

    public Return returnValue;

    @Override
    public int compareTo(Item o) {
        return (url == null || o == null || o.url == null) ? 0 : url.compareTo(o.url);
    }
}