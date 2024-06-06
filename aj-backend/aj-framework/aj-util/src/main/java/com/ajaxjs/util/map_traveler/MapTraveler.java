package com.ajaxjs.util.map_traveler;

import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * 遍历 ListMap 或 MapList
 */
@Data
public class MapTraveler {
    /**
     * 递归遍历 map 中的所有键值对
     *
     * @param map 要遍历的 map
     */
    public void traveler(Map<String, Object> map) {
        traveler(map, null, 0);
    }

    /**
     * 递归遍历 map 中的所有键值对
     *
     * @param map      要遍历的 map
     * @param superMap 父亲 map
     * @param level    深度
     */
    @SuppressWarnings("unchecked")
    public void traveler(Map<String, Object> map, Map<String, Object> superMap, int level) {
        // 如果onMap不为空且onMap.execute(map, superMap, level)返回false，则直接返回
        if (onMap != null && !onMap.execute(map, superMap, level))
            return;

        // 遍历map中的每个键值对
        for (String key : map.keySet()) {
            Object value = map.get(key);

            // 如果onNode不为空且onNode.execute(key, value, map, superMap, level)返回false，则直接返回
            if (onNode != null && !onNode.execute(key, value, map, superMap, level))
                return;

            // 如果value是List或Map类型
            if (value instanceof List || value instanceof Map) {
                // 如果onNewKey不为空，则调用onNewKey的accept方法并传入key
                if (onNewKey != null)
                    onNewKey.accept(key);

                // 如果value是Map类型，则递归调用traveler方法，并传入value和map作为参数，level加1
                if (value instanceof Map)
                    traveler((Map<String, Object>) value, map, level + 1);

                if (value instanceof List) {// 如果 value 是 List 类型
                    List<?> list = (List<?>) value;

                    // 如果list的大小大于0且第一个元素是 Map 类型，则递归调用traveler方法，并传入 list 作为参数，level 加1
                    if (list.size() > 0 && (list.get(0) instanceof Map))
                        traveler((List<Map<String, Object>>) value, map, level + 1);
                }

                // 如果onExitKey不为空，则调用onExitKey的accept方法并传入key
                if (onExitKey != null)
                    onExitKey.accept(key);
            }
        }
    }

    /**
     * 遍历一个 ListMap
     *
     * @param list 输入的 ListMa
     */
    public void traveler(List<Map<String, Object>> list) {
        traveler(list, null, 0);
    }

    /**
     * 遍历一个 ListMap
     *
     * @param list     输入的 ListMap
     * @param superMap 父级 Map
     * @param level    深度
     */
    public void traveler(List<Map<String, Object>> list, Map<String, Object> superMap, int level) {
        for (Map<String, Object> map : list) {
            if (map == null)
                continue;

            traveler(map, superMap, level);
        }
    }

    /**
     * 遇到新 Key 的回调
     */
    private Consumer<String> onNewKey;

    /**
     * 退出这个 key 的回调
     */
    private Consumer<String> onExitKey;

    /**
     *
     */
    private MapHandler onMap;

    /**
     * 当遇到一个节点的时候的回调
     */
    private MapEntryHandler onNode;
}
