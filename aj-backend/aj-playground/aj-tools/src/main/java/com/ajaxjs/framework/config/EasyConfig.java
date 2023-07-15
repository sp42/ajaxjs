package com.ajaxjs.framework.config;

import com.ajaxjs.util.io.FileHelper;
import com.ajaxjs.util.io.Resources;
import com.ajaxjs.util.logger.LogHelper;
import com.ajaxjs.util.map.JsonHelper;
import com.ajaxjs.utils.map.ListMap;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 不需要重启服务器的配置
 *
 * @author sp42 frank@ajaxjs.com
 */
public class EasyConfig extends HashMap<String, Object> {
    private static final LogHelper LOGGER = LogHelper.getLog(EasyConfig.class);

    private static final long serialVersionUID = 9099886055914666662L;

    /**
     * 配置文件路径
     */
    private String filePath;

    /**
     * 创建一个配置器
     *
     * @param filePath 配置文件路径
     */
    public EasyConfig(String filePath) {
        super();
        this.filePath = filePath;
        load();
    }

    /**
     * 创建一个配置器
     */
    public EasyConfig() {
        super();
        filePath = Resources.getResourcesFromClasspath("config.json");
        load();
    }

    /**
     *
     */
    private Map<String, Object> rawConfig;

    /**
     * 加载 JSON 配置
     */
    public void load() {
        if (filePath == null || !new File(filePath).exists()) {
            LOGGER.info("没有[{0}]项目配置文件", filePath);
            return;
        }

        loaded = false;

        try {
            String jsonStr = FileHelper.openAsText(filePath);
            Map<String, Object> map = JsonHelper.parseMap(jsonStr);
            rawConfig = map;

            clear();
            putAll(ListMap.flatMap2(map));
            loaded = true;

            LOGGER.infoGreen("加载[" + get("clientShortName") + "]项目配置成功！All config loaded.");
        } catch (Throwable e) {
            e.printStackTrace();
            LOGGER.warning("加载配置失败", e); // 可能 JSON 解析异常，搞到 Spring 都启动不了，加个 try/catch
        }
    }

    /**
     * 保存配置
     *
     * @param jsonStr 配置 JSON
     */
    public void save(String jsonStr) {
        FileHelper.saveText(filePath, jsonStr);
        load();
    }

    /**
     * 是否加载成功
     */
    private boolean loaded;

    /**
     * 内部的获取方法
     *
     * @param <T>         配置类型
     * @param key         配置键值
     * @param isNullValue 当配置为 null 时返回的值，相当于“默认值”
     * @return 配置内容
     */
    @SuppressWarnings("unchecked")
    private <T> T getAny(String key, T isNullValue) {
        if (!loaded) {
            LOGGER.warning("配置系统未准备好");
            return isNullValue;
        }

        Object v = get(key);

        if (v == null) {
            LOGGER.warning("没发现[{0}]配置", key);
            LOGGER.info(this);

            return isNullValue;
        }

        return (T) v;
    }

    /**
     * 读取配置并转换其为字符串类型。仅对扁平化后的配置有效，所以参数必须是扁平化的 aaa.bbb.ccc 格式。
     *
     * @param key 配置键值
     * @return 配置内容
     */
    public String getStr(String key) {
        return getAny(key, null);
    }

    /**
     * 获取解密的配置。采用 ASE 加密
     *
     * @param key       配置键值
     * @param secretAES 密钥
     * @return 配置内容
     */
    public String getStrDecrypt(String key, String secretAES) {
        String c = getStr(key);
        return SymmetricCipher.AES_Decrypt(c, secretAES);
    }

    /**
     * 读取配置并转换其为 布尔 类型。仅对扁平化后的配置有效，所以参数必须是扁平化的 aaa.bbb.ccc 格式。
     *
     * @param key 配置键值
     * @return 配置内容
     */
    public boolean getBol(String key) {
        return getAny(key, false);
    }

    /**
     * 读取配置并转换其为 int 类型。仅对扁平化后的配置有效，所以参数必须是扁平化的 aaa.bbb.ccc 格式。
     *
     * @param key 配置键值
     * @return 配置内容
     */
    public int getInt(String key) {
        return getAny(key, 0);
    }

    /**
     * 读取配置并转换其为 long 类型。仅对扁平化后的配置有效，所以参数必须是扁平化的 aaa.bbb.ccc 格式。
     *
     * @param key 配置键值
     * @return 配置内容
     */
    public long getLong(String key) {
        return getAny(key, 0L);
    }

    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> getListMap(String key) {
        if (rawConfig.containsKey(key))
            return (List<Map<String, Object>>) rawConfig.get(key);
        else
            return null;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
