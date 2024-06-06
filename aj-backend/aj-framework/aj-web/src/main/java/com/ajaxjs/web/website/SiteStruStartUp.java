package com.ajaxjs.web.website;


import com.ajaxjs.util.map_traveler.MapTraveler;
import com.ajaxjs.util.map_traveler.MapUtils;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

//@WebListener
public abstract class SiteStruStartUp implements ServletContextListener {
    private BiFunction<String, ServletContext, List<Map<String, Object>>> makeChildren;

    public void setMakeChildren(BiFunction<String, ServletContext, List<Map<String, Object>>> makeChildren) {
        this.makeChildren = makeChildren;
    }

    public BiFunction<String, ServletContext, List<Map<String, Object>>> getMakeChildren() {
        return makeChildren;
    }

    /**
     * 当 Web 应用程序上下文被初始化时调用此方法。
     * 主要用于加载网站结构配置文件，并对网站结构进行预处理。
     *
     * @param sce ServletContextEvent 对象，提供了对当前 Servlet 上下文的访问。
     */
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext context = sce.getServletContext(); // 获取Servlet上下文
        String path = context.getRealPath("/WEB-INF/website-stru.json"); // 获取网站结构配置文件的物理路径
        SiteStru json = new SiteStru();
        json.setFilePath(path);
        json.load(); // 加载网站结构配置文件

        if (json.isLoaded()) { // 检查配置文件是否成功加载
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> list = (List<Map<String, Object>>) json.getJsonMap().get("website"); // 获取网站配置数据

            // 如果存在自定义的子节点生成逻辑，则对配置数据进行处理
            if (makeChildren != null) {
                MapTraveler traveler = new MapTraveler(); // 使用MapTraveler工具对数据进行遍历和处理

                // 定义遍历过程中的行为，主要针对包含"dbNode"的节点进行处理
                traveler.setOnMap((map, superMap, level) -> {
                    if (map.containsKey("dbNode")) {
                        // 应用自定义的子节点生成逻辑
                        List<Map<String, Object>> children = makeChildren.apply(map.get("dbNode").toString(), context);

                        if (map.containsKey(MapUtils.CHILDREN))
                            ((List<Map<String, Object>>) map.get(MapUtils.CHILDREN)).addAll(children);
                        else
                            map.put(MapUtils.CHILDREN, children); // 将生成的子节点添加到当前节点
//
//                        System.out.println(map.get("name")); // 打印当前节点名称，用于调试
                    }

                    return true; // 继续遍历
                });

                traveler.traveler(list); // 开始遍历和处理数据
            }

            MapUtils.buildPath(list); // 构建节点路径信息
        }

        context.setAttribute("WEBSITE_STRU", json); // 将网站结构信息存入Servlet上下文，供其他部分使用
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
    }

    public final static String isQueryParams = "isQueryParams";

    /**
     * 将列表中的每个节点标记为数据库节点。
     *
     * @param list 包含节点信息的列表，每个节点是一个Map对象，其中至少包含一个键值对。该方法会为每个节点添加一个名为"isQueryParams"的键值对，值为 true。
     */
    public static void markNodeAsDbNode(List<Map<String, Object>> list) {
        // 遍历列表，为每个节点添加"isQueryParams"标记
        list.forEach(node -> node.put(isQueryParams, true));
    }

    /**
     * 将数据列表设置到Servlet上下文缓存中。
     *
     * @param name    缓存的名称，用于标识缓存的数据。
     * @param list    包含数据的列表，每个数据项是一个Map，其中至少包含一个"id"键。
     * @param context Servlet上下文，用于存储应用级数据。
     */
    public static void setDataToServletCache(String name, List<Map<String, Object>> list, ServletContext context) {
        // 创建一个HashMap来存储转换后的数据
        Map<Long, Map<String, Object>> map = new HashMap<>();

        // 遍历列表，将每个节点的数据转换为Long键的Map形式存储
        list.forEach(node -> {
            Object id = node.get("id");
            // 支持Long和Integer类型的id，将其转换为Long类型存储
            if (id instanceof Long)
                map.put((Long) id, node);
            else if (id instanceof Integer)
                map.put(((Integer) id).longValue(), node);
        });

        // 将转换后的数据Map设置为Servlet上下文的属性
        context.setAttribute(name, map);
    }
}
