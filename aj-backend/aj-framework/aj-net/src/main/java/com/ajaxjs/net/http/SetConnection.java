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
package com.ajaxjs.net.http;

import java.net.HttpURLConnection;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import com.ajaxjs.util.convert.MapTool;

/**
 * 为初始化 HTTP Connection 所准备的函数。该类不能创建实例 你可以链式调用这些 lambda，xxx.andThen(xxx)
 *
 * @author Frank Cheung
 */
public abstract class SetConnection {
    /**
     * 设置 cookies
     */
    public final static BiConsumer<HttpURLConnection, Map<String, String>> SET_COOKIES = (conn, map) -> conn.addRequestProperty("Cookie", MapTool.join(map, ";"));

    /**
     * 请求来源
     */
    public final static BiConsumer<HttpURLConnection, String> SET_REFERER = (conn, url) -> conn.addRequestProperty("Referer", url); // httpUrl.getHost()?

    /**
     * 设置超时 （单位：秒）
     */
    public final static BiConsumer<HttpURLConnection, Integer> SET_TIMEOUT = (conn, timeout) -> conn.setConnectTimeout(timeout * 1000);

    /**
     * 设置客户端识别
     */
    public final static BiConsumer<HttpURLConnection, String> SET_USER_AGENT = (conn, url) -> conn.addRequestProperty("User-Agent", url);

    /**
     * 默认的客户端识别
     */
    public final static Consumer<HttpURLConnection> SET_USER_AGENT_DEFAULT = conn -> SET_USER_AGENT.accept(conn, "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2");

    /**
     * 设置启动 GZip 请求
     */
    public final static Consumer<HttpURLConnection> SET_GZIP_REQUEST = conn -> conn.addRequestProperty("Accept-Encoding", "gzip, deflate");

    /**
     * 设置 POST 方式
     */
    public final static Consumer<HttpURLConnection> SET_FORM_POST = conn -> conn.setRequestProperty("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

    public final static Consumer<HttpURLConnection> SET_JSON = conn -> conn.setRequestProperty("Content-type", "application/json");

    /**
     * Map 转化到 HTTP HEAD。 这是高阶函数
     *
     * @param map 头数据
     * @return 函数
     */
    public static Consumer<HttpURLConnection> map2header(Map<String, ?> map) {
        return conn -> {
            for (String key : map.keySet())
                conn.setRequestProperty(key, map.get(key).toString());
        };
    }
}
