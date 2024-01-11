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

import com.ajaxjs.util.io.StreamHelper;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.function.Consumer;

/**
 * 基础调用，初始化 Connection--配置 Connection--发起请求
 *
 * @author Frank Cheung
 */
@Slf4j
public abstract class Base {
    /**
     * 链接超时时间
     */
    private static final int CONNECT_TIMEOUT = 10000;

    /**
     * read 超时时间
     */
    private static final int READ_TIMEOUT = 15000;

    /**
     * HttpURLConnection 工厂函数，对输入的网址进行初始化
     *
     * @param url    请求目的地址
     * @param method 请求方法
     * @return 请求连接对象
     */
    public static HttpURLConnection initHttpConnection(String url, String method) {
        log.info("准备连接： " + method + " " + url);
        URL httpUrl = null;

        try {
            httpUrl = new URL(url);
        } catch (MalformedURLException e) {
            log.warn("初始化连接出错！URL[" + url + "]格式不对！", e);
        }

        HttpURLConnection conn = null;

        try {
            assert httpUrl != null;
            conn = (HttpURLConnection) httpUrl.openConnection();
        } catch (IOException e) {
            log.warn("初始化连接出错！URL[" + url + "]", e);
        }

        try {
            if (conn != null) {
                conn.setRequestMethod(method);
                conn.setConnectTimeout(CONNECT_TIMEOUT);// 设置链接超时时间和 read 超时时间
                conn.setReadTimeout(READ_TIMEOUT);
            }
        } catch (ProtocolException e) {
            log.warn("不能设置 HTTP 方法 " + method, e);
        }

        return conn;
    }

    /**
     * 发送请求，返回响应信息
     *
     * @param conn 请求连接对象
     * @return 返回类型
     */
    public static ResponseEntity connect(HttpURLConnection conn) {
        ResponseEntity resp = new ResponseEntity();
        resp.setConnection(conn);
        resp.setUrl(conn.getURL().toString());
        resp.setHttpMethod(conn.getRequestMethod());
//        LOGGER.info("开始请求 {0} {1}", resp.getHttpMethod(), resp.getUrl());

        try {
            int responseCode = conn.getResponseCode();
//            LOGGER.info("responseCode:" + responseCode);
            resp.setHttpCode(responseCode);

            InputStream in;
            if (responseCode >= 400) {// 如果返回的结果是 400 以上，那么就说明出问题了
                // an error stream if any, null if there have been no errors, the connection is
                // not connected or the server sent no useful data.
                // 在连接建立后服务器端并没有发数据，Stream是空的，只有在进行了getHeaderFields()操作后才会激活服务器进行数据发送，实验一下
                // https://blog.csdn.net/xia4820723/article/details/47804797
                conn.getExpiration();
                resp.setOk(false);

                // 错误通常为文本
                in = conn.getErrorStream();

                String result = null;
                if (in != null) {
                    result = StreamHelper.byteStream2string(in);
                    resp.setResponseText(result);
                }

                log.info("{} {} 请求正常但结果异常 HTTP CODE {}，返回结果：\n{}", resp.getHttpMethod(), resp.getUrl(), resp.getHttpCode(), result == null ? "未知" : result);
            } else {
                resp.setOk(true);
                in = conn.getInputStream();// 发起请求，接收响应
                resp.setIn(in);
            }
        } catch (IOException e) {
            log.warn("请求异常： " + resp.getHttpMethod() + " " + resp.getUrl(), e);
            resp.setOk(false);
            resp.setEx(e);
        }

        return resp;
    }

    /**
     * 联合以上两个方法
     *
     * @param url    请求目的地址
     * @param method 请求方法
     * @param fn     自定义 HTTP 头的时候可设置，可选的
     * @return 响应结果
     */
    public static ResponseEntity connect(String url, String method, Consumer<HttpURLConnection> fn) {
        HttpURLConnection conn = initHttpConnection(url, method);

        if (fn != null)
            fn.accept(conn);

        return connect(conn);
    }

    /**
     * 返回 JSON 时候的 Map 的 key
     */
    public final static String ERR_MSG = "errMsg";
}
