package com.github.chaijunkun.wechat.common.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.io.Writer;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;

public enum XMLUtil {
    /**
     * 单例实例
     */
    instance;

    private XmlMapper xmlMapper;

    /**
     * 懒惰单例模式得到xmlMapper实例
     * 此对象为Jackson的核心
     */
    private XMLUtil() {
        JacksonXmlModule module = new JacksonXmlModule();
        //关联BigDecimal默认使用货币专用序列化与反序列化组件
        module.setDefaultUseWrapper(false);
        //序列化与反序列化时针对特殊字符自动转义的工具
        module.addSerializer(String.class, new XMLStringSerializer());
        module.addDeserializer(String.class, new XMLStringDeserializer());

        this.xmlMapper = new XmlMapper(module);
        //当找不到对应的序列化器时 忽略此字段
        this.xmlMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        //禁止一个Map中value为null时,对应key参与序列化
        this.xmlMapper.configure(SerializationFeature.WRITE_NULL_MAP_VALUES, false);
        //未知字段在反序列化时忽略
        this.xmlMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        //生成xml头部信息
        this.xmlMapper.configure(ToXmlGenerator.Feature.WRITE_XML_DECLARATION, true);

        //设置null值不参与序列化(字段不被显示)
        this.xmlMapper.setSerializationInclusion(Include.NON_NULL);
    }

    /**
     * 获取单例XmlMapper
     *
     * @return 单例XmlMapper
     */
    private XmlMapper getXmlMapper() {
        return this.xmlMapper;
    }

    /**
     * 创建xml字符串处理器的静态方法
     *
     * @param content xml字符串
     * @return
     * @throws IOException
     */
    private FromXmlParser getParser(String content) throws IOException {
        return (FromXmlParser) getXmlMapper().getFactory().createParser(content);
    }

    /**
     * 创建xml流式处理器的静态方法
     *
     * @param in xml输入流
     * @return
     * @throws IOException
     */
    private FromXmlParser getParser(InputStream in) throws IOException {
        return (FromXmlParser) getXmlMapper().getFactory().createParser(in);
    }

    /**
     * 创建xml生成器
     *
     * @param writer 用于xml输出的书写器
     * @return
     * @throws IOException
     */
    private ToXmlGenerator getGenerator(Writer writer) throws IOException {
        return getXmlMapper().getFactory().createGenerator(writer);
    }

    /**
     * 创建xml生成器
     *
     * @param out 用于xml输出的输出流
     * @return
     * @throws IOException
     */
    private ToXmlGenerator getGenerator(OutputStream out) throws IOException {
        return getXmlMapper().getFactory().createGenerator(out);
    }

    /**
     * 对象序列化
     *
     * @param obj 待序列化的对象
     * @return xml字符串
     * @throws IOException
     */
    public static String toXML(Object obj) throws IOException {
        StringWriter writer = null;
        ToXmlGenerator xmlGen = null;
        try {
            writer = new StringWriter();
            xmlGen = XMLUtil.instance.getGenerator(writer);
            //由于在getGenerator方法中指定了writer
            //因此调用writeObject会将数据输出到writer
            xmlGen.writeObject(obj);
            return writer.toString();
        } finally {
            //由于采用流式输出 在输出完毕后务必清空缓冲区并关闭输出流
            IOUtils.closeQuietly(xmlGen);
            IOUtils.closeQuietly(writer);
        }
    }

    /**
     * 对象序列化
     *
     * @param obj    待序列化的对象
     * @param writer 输出写入者
     * @throws IOException
     */
    public static void toXML(Object obj, Writer writer) throws IOException {
        ToXmlGenerator xmlGen = null;
        try {
            xmlGen = XMLUtil.instance.getGenerator(writer);
            //由于在getGenerator方法中指定了writer
            //因此调用writeObject会将数据输出到writer
            xmlGen.writeObject(obj);
        } finally {
            //由于采用流式输出 在输出完毕后务必清空缓冲区并关闭输出流
            IOUtils.closeQuietly(xmlGen);
        }
    }

    /**
     * 对象序列化
     *
     * @param obj 待序列化的对象
     * @param out 输出流
     * @throws IOException
     */
    public static void toXML(Object obj, OutputStream out) throws IOException {
        ToXmlGenerator xmlGen = null;
        try {
            xmlGen = XMLUtil.instance.getGenerator(out);
            //由于在getGenerator方法中指定了writer
            //因此调用writeObject会将数据输出到writer
            xmlGen.writeObject(obj);
        } finally {
            //由于采用流式输出 在输出完毕后务必清空缓冲区并关闭输出流
            IOUtils.closeQuietly(xmlGen);
        }
    }

    /**
     * xml反序列化
     *
     * @param xml   xml字符串
     * @param clazz 目标对象类型(仅适用于简单对象,即非泛型对象)
     * @return 目标对象
     * @throws IOException
     */
    public static <T> T fromXML(String xml, Class<T> clazz) throws IOException {
        FromXmlParser xmlParser = XMLUtil.instance.getParser(xml);
        return xmlParser.readValueAs(clazz);
    }

    /**
     * xml反序列化
     *
     * @param xml          xml字符串
     * @param valueTypeRef 目标对象类型(适用于泛型对象,例如 new TypeReference<List<User>>(){})
     * @return 目标对象
     * @throws IOException
     */
    public static <T> T fromXML(String xml, TypeReference<T> valueTypeRef) throws IOException {
        FromXmlParser xmlParser = XMLUtil.instance.getParser(xml);
        return xmlParser.readValueAs(valueTypeRef);
    }

    /**
     * xml反序列化
     *
     * @param in    xml输入流
     * @param clazz 目标对象类型(仅适用于简单对象,即非泛型对象)
     * @return 目标对象
     * @throws IOException
     */
    public static <T> T fromXML(InputStream in, Class<T> clazz) throws IOException {
        FromXmlParser xmlParser = XMLUtil.instance.getParser(in);
        return xmlParser.readValueAs(clazz);
    }

    /**
     * xml反序列化
     *
     * @param in           xml输入流
     * @param valueTypeRef 目标对象类型(适用于泛型对象,例如 new TypeReference<List<User>>(){})
     * @return 目标对象
     * @throws IOException
     */
    public static <T> T fromXML(InputStream in, TypeReference<T> valueTypeRef) throws IOException {
        FromXmlParser xmlParser = XMLUtil.instance.getParser(in);
        return xmlParser.readValueAs(valueTypeRef);
    }

    /**
     * 将bean对象转化为指定类型对象
     *
     * @param bean 待转化bean
     * @return 指定类型对象
     * @throws IllegalArgumentException 转换失败时抛出的异常
     */
    public static <T> T convertValue(Object bean, Class<T> clazz) throws IllegalArgumentException {
        return XMLUtil.instance.getXmlMapper().convertValue(bean, clazz);
    }

}
