package com.github.chaijunkun.wechat.common.util;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;

import java.io.*;

public enum JSONUtil {
    /**
     * 单例实例
     */
    instance;

    private ObjectMapper objectMapper;

    /**
     * 懒惰单例模式得到ObjectMapper实例
     * 此对象为Jackson的核心
     */
    private JSONUtil() {
        this.objectMapper = new ObjectMapper();
        //当找不到对应的序列化器时 忽略此字段
        this.objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        //支持双引号
        this.objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        //禁止一个Map中value为null时,对应key参与序列化
        this.objectMapper.configure(SerializationFeature.WRITE_NULL_MAP_VALUES, false);
        //未知字段在反序列化时忽略
        this.objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        //使Jackson JSON支持Unicode编码非ASCII字符
        SimpleModule module = new SimpleModule();
        //此处可以自定义某种类型的默认序列化与反序列化方法
        //module.addSerializer(String.class, new StringUnicodeSerializer());
        this.objectMapper.registerModule(module);
        //设置null值不参与序列化(字段不被显示)
        this.objectMapper.setSerializationInclusion(Include.NON_NULL);
    }

    /**
     * 获取单例ObjectMapper
     *
     * @return 单例ObjectMapper
     */
    private ObjectMapper getObjectMapper() {
        return this.objectMapper;
    }

    /**
     * 创建JSON字符串处理器的静态方法
     *
     * @param content JSON字符串
     * @return
     * @throws IOException
     */
    private JsonParser getParser(String content) throws IOException {
        return getObjectMapper().getFactory().createParser(content);
    }

    /**
     * 创建JSON流式处理器的静态方法
     *
     * @param in JSON输入流
     * @return
     * @throws IOException
     */
    private JsonParser getParser(InputStream in) throws IOException {
        return getObjectMapper().getFactory().createParser(in);
    }

    /**
     * 创建JSON生成器的静态方法, 使用标准输出
     *
     * @param writer 用于JSON输出的书写器
     * @return
     * @throws IOException
     */
    private JsonGenerator getGenerator(Writer writer) throws IOException {
        return getObjectMapper().getFactory().createGenerator(writer);
    }

    /**
     * 创建JSON生成器的静态方法, 使用标准输出
     *
     * @param out 用于JSON输出的输出流
     * @return
     * @throws IOException
     */
    private JsonGenerator getGenerator(OutputStream out) throws IOException {
        return getObjectMapper().getFactory().createGenerator(out);
    }

    /**
     * JSON对象序列化
     *
     * @param obj 待序列化的对象
     * @return JSON字符串
     */
    public static String toJSON(Object obj) throws IOException {
        try (StringWriter sw = new StringWriter();
             JsonGenerator jsonGen = JSONUtil.instance.getGenerator(sw)) {

            //由于在getGenerator方法中指定了writer
            //因此调用writeObject会将数据输出到writer
            jsonGen.writeObject(obj);
            //由于采用流式输出 在输出完毕后务必清空缓冲区并关闭输出流
            jsonGen.flush();

            return sw.toString();
        }
    }

    /**
     * JSON对象序列化
     *
     * @param obj    待序列化的对象
     * @param writer 输出写入者
     */
    public static void toJSON(Object obj, Writer writer) throws IOException {
        try (JsonGenerator jsonGen = JSONUtil.instance.getGenerator(writer)) {
            //由于在getGenerator方法中指定了writer
            //因此调用writeObject会将数据输出到writer
            jsonGen.writeObject(obj);
        }
    }

    /**
     * JSON对象序列化
     *
     * @param obj 待序列化的对象
     * @param out 输出流
     */
    public static void toJSON(Object obj, OutputStream out) throws IOException {
        try (JsonGenerator jsonGen = JSONUtil.instance.getGenerator(out)) {
            //由于在getGenerator方法中指定了writer
            //因此调用writeObject会将数据输出到writer
            jsonGen.writeObject(obj);
        }
    }

    /**
     * JSON对象反序列化
     *
     * @param json  JSON字符串
     * @param clazz 目标对象类型(仅适用于简单对象,即非泛型对象)
     * @return 目标对象
     */
    public static <T> T fromJSON(String json, Class<T> clazz) throws IOException {
        JsonParser jp = JSONUtil.instance.getParser(json);
        return jp.readValueAs(clazz);
    }

    /**
     * JSON对象反序列化
     *
     * @param json         JSON字符串
     * @param valueTypeRef 目标对象类型(适用于泛型对象,例如 new TypeReference<List<User>>(){})
     * @return 目标对象
     */
    public static <T> T fromJSON(String json, TypeReference<T> valueTypeRef) throws IOException {
        return JSONUtil.instance.getParser(json).readValueAs(valueTypeRef);
    }

    /**
     * JSON对象反序列化
     *
     * @param in    JSON输入流
     * @param clazz 目标对象类型(仅适用于简单对象,即非泛型对象)
     * @return 目标对象
     */
    public static <T> T fromJSON(InputStream in, Class<T> clazz) throws IOException {
        return JSONUtil.instance.getParser(in).readValueAs(clazz);
    }

    /**
     * JSON对象反序列化
     *
     * @param in           JSON输入流
     * @param valueTypeRef 目标对象类型(适用于泛型对象,例如 new TypeReference<List<User>>(){})
     * @return 目标对象
     */
    public static <T> T fromJSON(InputStream in, TypeReference<T> valueTypeRef) throws IOException {
        return JSONUtil.instance.getParser(in).readValueAs(valueTypeRef);
    }

    /**
     * 将bean对象转化为指定类型对象
     *
     * @param bean 待转化bean
     * @return 指定类型对象
     * @throws IllegalArgumentException 转换失败时抛出的异常
     */
    public static <T> T convertValue(Object bean, Class<T> clazz) throws IllegalArgumentException {
        return JSONUtil.instance.getObjectMapper().convertValue(bean, clazz);
    }

}
