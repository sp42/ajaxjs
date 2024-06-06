package com.ajaxjs.util.map_traveler;

import java.util.*;

public class MapUtils {
    /**
     * 扁平化多层 map 为单层
     *
     * @param map 多层 Map，其键值对可以是嵌套的 Map 结构
     * @return 单层 Map，将多层 Map 中的所有键值对扁平化后存储在一个单层 Map 中，使用下划线(_)分隔嵌套层级间的键名
     */
    public static Map<String, Object> flatMap(Map<String, Object> map) {
        final Stack<String> stack = new Stack<>(); // 使用栈来跟踪当前嵌套层级的键名
        final Map<String, Object> _map = new HashMap<>();    // 存储扁平化后的键值对
        MapTraveler traveler = new MapTraveler();  // 创建 MapTraveler 对象来进行多层 Map 的遍历和扁平化处理
        traveler.setOnNewKey(stack::add);// 当遍历到新的键时，将键名压入栈中
        traveler.setOnExitKey(key -> stack.pop());// 当退出当前键的遍历时，从栈中弹出键名，表示返回上一层级

        // 当遍历到具体的键值对时，执行相应的处理逻辑
        traveler.setOnNode((key, obj, currentMap, superMap, i) -> {
            // 如果当前值为简单类型（String、Number、Boolean），则将其扁平化存入 _map
            if (obj == null || obj instanceof String || obj instanceof Number || obj instanceof Boolean) {
                StringBuilder sb = new StringBuilder();

                for (String s : stack) // 通过栈中的元素构建当前键的完整路径
                    sb.append(s).append("_");

                _map.put(sb + key, obj); // 将扁平化后的键值对存入 _map
            }

            return true; // 继续遍历下一个节点
        });

        traveler.traveler(map);  // 开始遍历多层 Map

        return _map;
    }

    public final static String ID = "id";

    public final static String CHILDREN = "children";

    public final static String PATH = "fullPath";

    public final static String LEVEL = "level";

    /**
     * 为每个 Map 加上 path 和 level 字段，分别是路径和深度。默认字段为 "id", "fullPath", "level"。
     *
     * @param list       输入的 ListMap
     * @param saveSupers 是否生成父级节点的信息（包含 id 和名称）
     */
    public static void buildPath(List<Map<String, Object>> list, boolean saveSupers) {
        buildPath(list, ID, PATH, LEVEL, saveSupers);
    }

    /**
     * 构建路径。
     * 该方法是一个静态方法，主要作用是基于传入的列表对象列表构建路径。此方法是 buildPath 的重载方法，它简化了调用时的参数传递
     *
     * @param list 包含路径信息的列表，列表中的每个元素都是一个 Map 对象，存储了路径相关的属性。
     *             该列表通常包含了一系列路径点的信息，每个路径点通过 Map 来表示，其中包含了ID、路径、级别等属性
     * @see #buildPath(List, String, String, String, boolean) 该方法调用了另一个重载方法，完成了实际的路径构建逻辑
     */
    public static void buildPath(List<Map<String, Object>> list) {
        buildPath(list, ID, PATH, LEVEL, false);
    }

    /**
     * 递归构建路径的辅助方法的入口点
     *
     * @param list  包含路径信息的列表对象
     * @param id    当前处理的对象的 ID
     * @param path  当前构建的路
     * @param level 当前的层级深度
     */
    public static void buildPath(List<Map<String, Object>> list, String id, String path, String level) {
        buildPath(list, id, path, level, false);
    }

    public final static String IS_QUERY_PARAMS = "isQueryParams";

    /**
     * 为每个 Map 加上 path 和 level 字段，分别是路径和深度
     *
     * @param list  输入的 ListMap
     * @param id    map 的表示字段
     * @param path  map 的路径字段
     * @param level map 的深度
     */
    public static void buildPath(List<Map<String, Object>> list, String id, String path, String level, boolean saveSupers) {
        MapTraveler traveler = new MapTraveler();

        traveler.setOnMap((map, superMap, _level) -> {
            String superPath = superMap == null ? "" : superMap.get(path).toString();

            if (map.containsKey(IS_QUERY_PARAMS))
                map.put(path, superPath + "/?type=" + map.get(id)); // 写死 type 参数名。另外这个逻辑写在这里是不合理的，不过当前也没有更好的办法
            else map.put(path, superPath + "/" + map.get(id));

            map.put(level, _level);

            if (saveSupers && superMap != null) {
                Object _supers = superMap.get("supers");
                String supers = _supers == null ? "" : _supers.toString();

                supers += ("".equals(supers) ? "" : ",");
                supers += superPath + ":" + superMap.get("name");
                map.put("supers", supers);
                // supers.add(superPath + ":" + superMap.get("name"));
            }

            // LOGGER.info(map.get(id) + "@" + _level + ":" + map.get(path));
            return true;
        });

        traveler.traveler(list);
    }

    /**
     * 输入一个路径，转换为队列结构
     *
     * @param path 路径，用 / 分隔开
     * @return 路径队列
     */
    public static Queue<String> split2Queue(String path) {
        String[] arr = path.split("/");

        return new LinkedList<>(Arrays.asList(arr));
    }

    /**
     * 根据给定的路径查找队列中对应的元素在列表中的位置并返回其对应的 Map 对象。
     *
     * @param queue 一个队列，包含需要查找的元素路径。
     * @param list  一个包含多个 Map 对象的列表，每个 Map 对象代表一个节点，其中包含元素的 ID和子元素信息。
     * @return 返回找到的元素对应的 Map 对象，如果没有找到则返回 null。
     */
    @SuppressWarnings("unchecked")
    private static Map<String, Object> findByPath(Queue<String> queue, List<Map<String, Object>> list) {
        Map<String, Object> map = null;

        while (!queue.isEmpty()) {
            String str = queue.poll();

            for (Map<String, Object> _map : list) { // 在列表中遍历，查找包含指定 ID 的 Map 对象
                Object v = _map.get(ID);

                if (v != null && v.toString().equals(str)) {
                    map = _map;
                    break; // 找到匹配的 ID，跳出循环
                }
            }

            if (map != null) {
                if (queue.isEmpty()) break; // 已经查找到队列中的所有元素，结束循环
                else if (map.get(CHILDREN) != null)
                    map = findByPath(queue, (List<Map<String, Object>>) map.get(CHILDREN));// 如果当前Map对象包含子元素，则递归在子元素中继续查找
                else map = null; // 当前 Map 对象没有子元素，继续查找下一个元素
            }
        }

        return map; // 返回最终找到的 Map 对象或 null
    }

    /**
     * 根据路径查找节点
     * <p>
     * 该方法通过将给定的路径字符串解析成队列，然后在提供的列表中查找对应的节点。
     * 路径字符串的格式应该以'/'开头和结尾，中间使用'/'分隔各个键名。
     *
     * @param str  表示要查找的路径的字符串。它应该以'/'开头和结尾，中间使用'/'分隔各个键名。
     * @param list 包含节点数据的列表，每个节点由一个 Map 表示，其中键值对代表节点的属性和值。
     * @return 返回一个 Map，代表根据路径找到的节点。如果未找到对应路径的节点，则返回 null。
     */
    public static Map<String, Object> findByPath(String str, List<Map<String, Object>> list) {
        // 移除路径字符串开头和结尾的'/'
        if (str.startsWith("/")) str = str.substring(1);

        if (str.endsWith("/")) str = str.substring(0, str.length() - 1);

        // 使用 split2Queue 方法将路径字符串分割成队列，然后调用 findByPath 方法进行查找
        return findByPath(split2Queue(str), list);
    }

}
