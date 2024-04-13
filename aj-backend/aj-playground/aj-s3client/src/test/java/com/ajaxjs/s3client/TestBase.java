package com.ajaxjs.s3client;

import com.ajaxjs.util.io.Resources;
import com.ajaxjs.util.map_traveler.MapUtils;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public class TestBase {
    /**
     * 从指定的 YAML 配置文件中加载配置信息。
     *
     * @param configFile 配置文件的名称，该文件应在项目的资源目录中。
     * @return 一个包含配置信息的 Map 对象，其中键值对代表配置的名称和值。
     * @throws RuntimeException 如果无法读取配置文件或发生 IO 异常。
     */
    public static Map<String, Object> getConfigFromYml(String configFile) {
        Yaml yaml = new Yaml();

        try (InputStream resourceAsStream = Resources.getResource(configFile)) {
            Map<String, Object> m = yaml.load(resourceAsStream);

            return MapUtils.flatMap(m);  // 将解析后的嵌套Map转换为平铺的 Map，方便使用
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
