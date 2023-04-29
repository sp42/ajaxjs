/**
 * Copyright Sp42 frank@ajaxjs.com Licensed under the Apache License, Version
 * 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law
 * or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package com.ajaxjs.util.io;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import com.ajaxjs.util.logger.LogHelper;
import org.springframework.util.StringUtils;

/**
 * 资源工具类
 */
public class Resources {
    private static final LogHelper LOGGER = LogHelper.getLog(Resources.class);

    /**
     * 获取 Classpath 根目录下的资源文件
     *
     * @param resource 文件名称，输入空字符串这返回 Classpath 根目录
     * @param isDecode 是否解码
     * @return 所在工程路径+资源路径，找不到文件则返回 null
     */
    public static String getResourcesFromClasspath(String resource, boolean isDecode) {
        URL url = Resources.class.getClassLoader().getResource(resource);

        if (url == null) {
            LOGGER.warning("获取资源 {0} 失败", resource);
            return null;
        }

        return url2path(url, isDecode);
    }

    /**
     * 获取当前类目录下的资源文件
     *
     * @param clz      类引用
     * @param resource 资源文件名
     * @return 当前类的绝对路径，找不到文件则返回 null
     */
    public static String getResourcesFromClass(Class<?> clz, String resource) {
        return getResourcesFromClass(clz, resource, true);
    }

    /**
     * 获取当前类目录下的资源文件
     *
     * @param clz      类引用
     * @param resource 资源文件名
     * @param isDecode 是否解码
     * @return 当前类的绝对路径，找不到文件则返回 null
     */
    public static String getResourcesFromClass(Class<?> clz, String resource, boolean isDecode) {
        return url2path(clz.getResource(resource), isDecode);
    }

    /**
     * 获取 Classpath 根目录下的资源文件
     *
     * @param resource 文件名称，输入空字符串这返回 Classpath 根目录。可以支持包目录，例如
     *                 com\\ajaxjs\\newfile.txt
     * @return 所在工程路径+资源路径，找不到文件则返回 null
     */
    public static String getResourcesFromClasspath(String resource) {
        return getResourcesFromClasspath(resource, true);
    }

    /**
     * url.getPath() 返回 /D:/project/a，需要转换一下
     *
     * @param url      URL 对象
     * @param isDecode 是否解码
     * @return 转换路径
     */
    private static String url2path(URL url, boolean isDecode) {
        if (url == null)
            return null;

        String path;
        if (isDecode) {
            path = StringUtils.uriDecode(new File(url.getPath()).toString(), StandardCharsets.UTF_8);
        } else {
            path = url.getPath();
            path = path.startsWith("/") ? path.substring(1) : path;
        }

        // path = path.replaceAll("file:\\", "");
        return path;
    }

    /**
     * Java 类文件 去掉后面的 .class 只留下类名
     *
     * @param file        Java 类文件
     * @param packageName 包名称
     * @return 类名
     */
    public static String getClassName(File file, String packageName) {
        String clzName = file.getName().substring(0, file.getName().length() - 6);

        return packageName + '.' + clzName;
    }

    /**
     * 从 classpath 获取资源文件的内容
     *
     * @param path 资源文件路径
     * @return 资源文件的内容
     */
    public static String getResourceText(String path) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

        try (InputStream in = classLoader.getResourceAsStream(path)) {
            if (in == null) {
                LOGGER.warning("[{0}] 下没有 [{1}] 资源文件", getResourcesFromClasspath(""), path);
                return null;
            }

            return StreamHelper.byteStream2string(in);
        } catch (IOException e) {
            LOGGER.warning(e);
            return null;
        }
    }

    /**
     * 获取正在运行的 JAR 文件的目录
     * 如果您在 IDE 中运行代码，则该代码可能会返回项目的根目录
     *
     * @return JAR 文件的目录
     */
    public static String getJarDir() {
        String jarDir = null;

        try {
            jarDir = new File(Resources.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath()).getParent();
        } catch (URISyntaxException e) {
            LOGGER.warning(e);
        }

        return jarDir;
    }
}
