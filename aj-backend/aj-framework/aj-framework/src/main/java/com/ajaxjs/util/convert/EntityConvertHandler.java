package com.ajaxjs.util.convert;

import java.util.Map;

/**
 * 实体转换器
 */
public interface EntityConvertHandler {
    /**
     * Bean 转为 Map
     *
     * @param bean 实体 bean 对象
     * @return Map 对象
     */
    Map<String, Object> bean2Map(Object bean);

    /**
     * Map 转为 Bean
     *
     * @param map 原始数据
     * @param clz 实体 bean 的类
     * @return 实体 bean 对象
     */
    <T> T map2bean(Map<String, Object> map, Class<T> clz);

    /**
     * JSON 序列化：Java Bean 转换为 JSON 字符串
     * 这个本框架提供支持，无须第三方库支持
     *
     * @param bean Java Bean 实体
     * @return JSON 字符串
     */
    String bean2json(Object bean);

    /**
     * JSON 序列化：Map 实体 转换为 JSON 字符串
     * 这个本框架提供支持，无须第三方库支持
     *
     * @param map Map 实体
     * @return JSON 字符串
     */
    String map2json(Map<String, Object> map);

    /**
     * JSON 反序列化：解析 JSON 字符串为 Map
     * 这个方法需要 JSON 工具支持
     *
     * @param json JSON 字符串
     * @return Map 实体
     */
    default Map<String, Object> json2map(String json) {
        throw new IllegalStateException();
    }

    /**
     * JSON 反序列化：解析 JSON 字符串为 Bean
     * 这个方法需要 JSON 工具支持
     *
     * @param json JSON 字符串
     * @param clz  实体 bean 的类
     * @return Java Bean 实体
     */
    default <T> T json2bean(String json, Class<T> clz) {
        throw new IllegalStateException();
    }
}
