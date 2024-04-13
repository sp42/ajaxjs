package com.ajaxjs.util.map_traveler;

import java.util.Map;

/**
 * Map 对象的回调函数
 *
 * @author sp42 frank@ajaxjs.com
 */
@FunctionalInterface
public interface MapHandler {
    /**
     * 执行回调函数
     *
     * @param map      当前 Map
     * @param superMap 父级 Map
     * @param level    深度
     * @return true 表示为跳出遍历
     */
    boolean execute(Map<String, Object> map, Map<String, Object> superMap, int level);
}