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

import com.ajaxjs.util.StrUtil;
import com.ajaxjs.util.convert.ConvertToJson;
import com.ajaxjs.util.convert.EntityConvert;
import com.ajaxjs.util.convert.MapTool;
import com.ajaxjs.util.io.FileHelper;
import com.ajaxjs.util.io.StreamHelper;
import com.ajaxjs.util.regexp.RegExpUtils;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * POST 或 PUT 请求
 *
 * @author Frank Cheung
 */
@Slf4j
public class Post extends Base implements HttpConstants {
    /**
     * POST 或 PUT 请求
     *
     * @param isPost 是否 POST 请求，反之为 PUT
     * @param url    请求目标地址
     * @param params 请求参数，可以是
     *               <pre>     </pre>
     *               <p>
     *               类型，实际表示了表单数据 KeyValue 的请求数据
     * @param fn     自定义 HTTP 头的时候可设置，可选的
     * @return 响应消息体
     */
    private static ResponseEntity p(boolean isPost, String url, Object params, Consumer<HttpURLConnection> fn) {
        return any(isPost ? POST : PUT, url, params, fn);
    }

    /**
     * 向指定 URL 发送指定方法的请求并返回响应
     *
     * @param method 请求的方法类型，如 GET、POST
     * @param url    请求的 URL
     * @param params 请求的参数
     * @param fn     用于配置 HttpURLConnection 的 Consumer
     * @return 响应的  ResponseEntity  对象
     */
    public static ResponseEntity any(String method, String url, Object params, Consumer<HttpURLConnection> fn) {
        final byte[] _params;

        if (params instanceof String)
            _params = ((String) params).getBytes(StandardCharsets.UTF_8);
        else if (params instanceof Map) {
            @SuppressWarnings("unchecked")
            Map<String, Object> map = (Map<String, Object>) params;

            if (map.size() > 0) {
                String str = MapTool.join(map, v -> v == null ? null : StrUtil.urlEncode(v.toString()));
                _params = str.getBytes(StandardCharsets.UTF_8);
            } else
                _params = null;

        } else if (params instanceof byte[])
            _params = (byte[]) params;
        else
            _params = null;

        return connect(url, method, conn -> {
            conn.setDoOutput(true); // 允许写入输出流
            conn.setDoInput(true); // 允许读取输入流

            if (fn != null)
                fn.accept(conn);

            if (_params != null && _params.length > 0) {
                try (OutputStream out = conn.getOutputStream()) {
                    out.write(_params); // 通过输出流写入字节数据
                    out.flush();
                } catch (IOException e) {
                    log.warn("写入数据时失败！", e);
                }
            }
        });
    }

    /**
     * POST 请求
     *
     * @param url    请求目标地址
     * @param params 请求参数，可以是
     *               <pre>byte[]、String、Map&lt;String, Object&gt;</pre>
     *               <p>
     *               类型，实际表示了表单数据 KeyValue 的请求数据
     * @param fn     自定义 HTTP 头的时候可设置，可选的
     * @return 响应消息体
     */
    public static ResponseEntity post(String url, Object params, Consumer<HttpURLConnection> fn) {
        return p(true, url, params, fn);
    }

    /**
     * POST 请求
     *
     * @param url    请求目标地址
     * @param params 请求参数，可以是
     *               <pre>byte[]、String、Map&lt;String, Object&gt;</pre>
     *               <p>
     *               类型，实际表示了表单数据 KeyValue 的请求数据
     * @return 响应消息体
     */
    public static ResponseEntity post(String url, Object params) {
        return post(url, params, null);
    }

    /**
     * PUT 请求
     *
     * @param url    请求目标地址
     * @param params 请求参数，可以是
     *               <pre>byte[]、String、Map&lt;String, Object&gt;</pre>
     *               <p>
     *               类型，实际表示了表单数据 KeyValue 的请求数据
     * @param fn     自定义 HTTP 头的时候可设置，可选的
     * @return 响应消息体
     */
    public static ResponseEntity put(String url, Object params, Consumer<HttpURLConnection> fn) {
        return p(false, url, params, fn);
    }

    /**
     * PUT 请求
     *
     * @param url    请求目标地址
     * @param params 请求参数，可以是<pre>byte[]、String、Map&lt;String, Object&gt;</pre>类型，实际表示了表单数据 KeyValue 的请求数据
     */
    public static ResponseEntity put(String url, Object params) {
        return put(url, params, null);
    }

    /**
     * PUT API 请求，返回 JSON
     *
     * @param url    请求目标地址
     * @param params 请求参数，可以是<pre>byte[]、String、Map&lt;String, Object&gt;</pre>类型，实际表示了表单数据 KeyValue 的请求数据
     * @param fn     自定义 HTTP 头的时候可设置，可选的
     * @return 响应消息体
     */
    public static Map<String, Object> putApi(String url, Object params, Consumer<HttpURLConnection> fn) {
        return ResponseHandler.toJson(p(false, url, params, fn));
    }

    /**
     * PUT API 请求，返回 JSON
     *
     * @param url    请求目标地址
     * @param params 请求参数，可以是<pre>byte[]、String、Map&lt;String, Object&gt;</pre>类型，实际表示了表单数据 KeyValue 的请求数据
     * @return 响应消息体
     */
    public static Map<String, Object> putApi(String url, Object params) {
        return putApi(url, params, null);
    }

    /**
     * POST API，返回 JSON
     *
     * @param url    请求目标地址
     * @param params 请求参数，可以是<pre>byte[]、String、Map&lt;String, Object&gt;</pre>类型，实际表示了表单数据 KeyValue 的请求数据
     * @param fn     自定义 HTTP 头的时候可设置，可选的
     * @return 响应的 JSON，Map 格式
     */
    public static Map<String, Object> api(String url, Object params, Consumer<HttpURLConnection> fn) {
        ResponseEntity resp = post(url, params, fn);

        return ResponseHandler.toJson(resp);
    }

    /**
     * POST JSON as RawBody
     */
    public static Map<String, Object> apiJsonBody(String url, Object params, Consumer<HttpURLConnection> fn) {
        return api(url, toJsonStr(params), fn);
    }

    /**
     * PUT JSON as RawBody
     */
    public static Map<String, Object> putJsonBody(String url, Object params, Consumer<HttpURLConnection> fn) {
        return putApi(url, toJsonStr(params), fn);
    }

    static String toJsonStr(Object params) {
        String json = ConvertToJson.toJson(params);
        json = json.replaceAll("[\\r\\n]", ""); // 不要换行，否则会不承认这个格式
        log.info(json);

        return json;
    }

    /**
     * POST API，返回 JSON
     *
     * @param url    请求目标地址
     * @param params 请求参数，可以是<pre>byte[]、String、Map&lt;String, Object&gt;</pre>类型，实际表示了表单数据 KeyValue 的请求数据
     * @return 响应的 JSON，Map 格式
     */
    public static Map<String, Object> api(String url, Object params) {
        return api(url, params, null);
    }

    /**
     * 调用 API 请求指定的 URL 并返回结果。
     *
     * @param url 请求的 URL
     * @return 包含 API 响应结果的 Map 对象
     */
    public static Map<String, Object> api(String url) {
        return api(url, null);
    }

    /**
     * POST API，返回 XML
     *
     * @param url    请求目标地址
     * @param params 请求参数，可以是<pre>byte[]、String、Map&lt;String, Object&gt;</pre>类型，实际表示了表单数据 KeyValue 的请求数据
     * @param fn     自定义 HTTP 头的时候可设置，可选的
     * @return 响应的 XML，Map 格式
     */
    public static Map<String, String> apiXML(String url, Object params, Consumer<HttpURLConnection> fn) {
        ResponseEntity resp = post(url, params, fn);

        return ResponseHandler.toXML(resp);
    }

    /**
     * POST API，返回 XML
     *
     * @param url    请求目标地址
     * @param params 请求参数，可以是<pre>byte[]、String、Map&lt;String, Object&gt;</pre>类型，实际表示了表单数据 KeyValue 的请求数据
     * @return 响应的 XML，Map 格式
     */
    public static Map<String, String> apiXML(String url, Object params) {
        return apiXML(url, params, null);
    }

    /**
     * 多段 POST 的分隔，request 头和上传文件内容之间的分隔符
     */
    private static final String DIV_FIELD = "\r\n--%s\r\nContent-Disposition: form-data; name=\"%s\"\r\n\r\n%s";

    // 换行符
    private static final String NEWLINE = "\r\n";

    private static final String BOUNDARY_PREFIX = "--";

    // 定义数据分隔线
    public static String BOUNDARY = "------------7d4a6d158c9";

    private static final String FIELD = BOUNDARY_PREFIX + BOUNDARY + NEWLINE + "Content-Disposition: form-data;name=\"%s\";filename=\"%s\"" + NEWLINE
            + "Content-Type:%s" + NEWLINE + NEWLINE;

    // 定义最后数据分隔线，即--加上BOUNDARY再加上--。
    private static final byte[] END_DATA = (NEWLINE + BOUNDARY_PREFIX + BOUNDARY + BOUNDARY_PREFIX + NEWLINE).getBytes();

    /**
     * Map 转换为 byte
     *
     * @param data Map
     * @return Map 转换为 byte
     */
    public static byte[] toFromData(Map<String, Object> data) {
        byte[] bytes = null;

        for (String key : data.keySet()) {
            Object v = data.get(key);
            byte[] _bytes;

            if (v instanceof File) {
                File file = (File) v;
                String field = String.format(FIELD, key, file.getName(), "application/octet-stream");

                _bytes = StreamHelper.concat(field.getBytes(), Objects.requireNonNull(FileHelper.openAsByte(file)));
            } else { // 普通字段
                String field = String.format(DIV_FIELD, BOUNDARY, key, v.toString());
                _bytes = field.getBytes();
            }

            if (bytes == null) // 第一次时候为空
                bytes = _bytes;
            else
                bytes = StreamHelper.concat(bytes, _bytes);
        }

        assert bytes != null;
        return StreamHelper.concat(bytes, END_DATA);
    }

    /**
     * 多段上传
     *
     * @param url  请求目标地址
     * @param data 请求数据，若包含 File 对象则表示二进制（文件）数据
     * @return 请求之后的响应的内容
     */
    public static ResponseEntity multiPOST(String url, Map<String, Object> data) {
        return post(url, toFromData(data), conn -> conn.setRequestProperty(CONTENT_TYPE, "multipart/form-data; boundary=" + BOUNDARY));
    }

    /**
     * 以POST方法上传文件
     *
     * @param url       上传文件的URL
     * @param fieldName 文件字段名
     * @param fileName  文件名
     * @param file      文件内容
     * @param fn        用于设置HTTP连接的回调函数
     * @return 上传成功返回文件上传结果的Map，否则返回null
     */
    public static Map<String, Object> postFile(String url, String fieldName, String fileName, byte[] file, Consumer<HttpURLConnection> fn) {
        String field = String.format(FIELD, fieldName, fileName, "application/octet-stream");// 构造文件字段
        byte[] bytes = StreamHelper.concat(field.getBytes(), file);   // 将字段和文件内容拼接
        bytes = StreamHelper.concat(bytes, END_DATA);// 拼接结束数据

        // 如果回调函数不为空，添加设置HTTP请求内容类型的操作
        if (fn != null)
            fn = fn.andThen(conn -> conn.setRequestProperty(CONTENT_TYPE, "multipart/form-data; boundary=" + BOUNDARY));
            // 如果回调函数为空，直接设置HTTP请求内容类型
        else
            fn = conn -> conn.setRequestProperty(CONTENT_TYPE, "multipart/form-data; boundary=" + BOUNDARY);

        // 发送POST请求并获取响应实体
        ResponseEntity resp = post(url, bytes, fn);
        // 将响应实体转换为JSON字符串
        String json = resp.toString();

        // 如果JSON字符串有内容，将其转换为Map返回
        if (json != null && !json.isEmpty())
            return EntityConvert.json2map(json);
            // 如果JSON字符串为空，返回null
        else
            return null;
    }

    /**
     * 下载二进制文件
     *
     * @param url         请求目标地址
     * @param fn          自定义 HTTP 头的时候可设置，可选的
     * @param saveDir     保存的目录
     * @param newFileName 是否有新的文件名，如无请传 null
     * @return 下载文件的完整磁盘路径
     */
    public static String download(String url, Consumer<HttpURLConnection> fn, String saveDir, String newFileName) {
        HttpURLConnection conn = initHttpConnection(url, POST);
        conn.setDoInput(true);// for conn.getOutputStream().write(someBytes); 需要吗？
        conn.setDoOutput(true);

        if (fn != null)
            fn.accept(conn);

        String fileName = FileHelper.getFileNameFromUrl(url);
        if (newFileName != null)
            fileName = newFileName + RegExpUtils.regMatch("\\.\\w+$", fileName);// 新文件名 + 旧扩展名

        ResponseEntity resp = connect(conn);

        return ResponseHandler.download(resp, saveDir, fileName);
    }

    /**
     * 显示图片流到浏览器
     *
     * @param url      地址
     * @param fn       处理怎么请求
     * @param response 响应对象
     */
    public static void showPic(String url, Consumer<HttpURLConnection> fn, HttpServletResponse response) {
        HttpURLConnection conn = initHttpConnection(url, POST);
        conn.setDoInput(true);// for conn.getOutputStream().write(someBytes); 需要吗？
        conn.setDoOutput(true);

        if (fn != null)
            fn.accept(conn);

        ResponseEntity resp = connect(conn);
        response.setContentType("image/jpeg");

        try (OutputStream out = response.getOutputStream()) {
            StreamHelper.write(resp.getIn(), out, true);
        } catch (IOException e) {
            log.warn("ERR>>>", e);
            throw new RuntimeException(e);
        }
    }
}
