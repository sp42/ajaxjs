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

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.function.Consumer;

import com.ajaxjs.util.StrUtil;
import com.ajaxjs.util.io.FileHelper;
import com.ajaxjs.util.io.StreamHelper;
import com.ajaxjs.util.map.MapTool;

/**
 * POST 或 PUT 请求
 *
 * @author Frank Cheung
 */
public class Post extends Base {
    /**
     * POST 或 PUT 请求
     *
     * @param isPost 是否 POST 请求，反之为 PUT
     * @param url    请求目标地址
     * @param params 请求参数，可以是 <pre>byte[]、String、Map<String, Object></pre> 类型，实际表示了表单数据 KeyValue 的请求数据
     * @param fn     自定义 HTTP 头的时候可设置，可选的
     * @return 响应消息体
     */

    private static ResponseEntity p(boolean isPost, String url, Object params, Consumer<HttpURLConnection> fn) {
        return any(isPost ? "POST" : "PUT", url, params, fn);
    }

    public static ResponseEntity any(String method, String url, Object params, Consumer<HttpURLConnection> fn) {
        final byte[] _params;

        if (params instanceof String) {
            _params = ((String) params).getBytes(StandardCharsets.UTF_8);
        } else if (params instanceof Map) {
            @SuppressWarnings("unchecked")
            Map<String, Object> map = (Map<String, Object>) params;

            if (map != null && map.size() > 0)
                _params = MapTool.join(map, v -> v == null ? null : StrUtil.urlEncode(v.toString())).getBytes(StandardCharsets.UTF_8);
            else
                _params = null;
        } else if (params instanceof byte[])
            _params = (byte[]) params;
        else
            _params = null;

        return connect(url, method, conn -> {
            conn.setDoOutput(true); // for conn.getOutputStream().write(someBytes);
            conn.setDoInput(true);

            if (fn != null)
                fn.accept(conn);

            if (_params != null && _params.length > 0) {
                try (OutputStream out = conn.getOutputStream()) {
                    out.write(_params); // 输出流写入字节数据
                    out.flush();
                } catch (IOException e) {
                    e.printStackTrace();
//					LOGGER.warning("写入数据时失败！[{0}]", e);
                }
            }
        });
    }

    /**
     * POST 请求
     *
     * @param url    请求目标地址
     * @param params 请求参数，可以是 <pre>byte[]、String、Map<String, Object></pre> 类型，实际表示了表单数据 KeyValue 的请求数据
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
     * @param params 请求参数，可以是 <pre>byte[]、String、Map<String, Object></pre> 类型，实际表示了表单数据 KeyValue 的请求数据
     * @return 响应消息体
     */
    public static ResponseEntity post(String url, Object params) {
        return post(url, params, null);
    }

    /**
     * PUT 请求
     *
     * @param url    请求目标地址
     * @param params 请求参数，可以是 <pre>byte[]、String、Map<String, Object></pre> 类型，实际表示了表单数据 KeyValue 的请求数据
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
     * @param params 请求参数，可以是 <pre>byte[]、String、Map<String, Object></pre> 类型，实际表示了表单数据 KeyValue 的请求数据
     * @return 响应消息体
     */
    public static ResponseEntity put(String url, Object params) {
        return put(url, params, null);
    }

    /**
     * PUT API 请求，返回 JSON
     *
     * @param url    请求目标地址
     * @param params 请求参数，可以是 <pre>byte[]、String、Map<String, Object></pre> 类型，实际表示了表单数据 KeyValue 的请求数据
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
     * @param params 请求参数，可以是 <pre>byte[]、String、Map<String, Object></pre> 类型，实际表示了表单数据 KeyValue 的请求数据
     * @return 响应消息体
     */
    public static Map<String, Object> putApi(String url, Object params) {
        return putApi(url, params, null);
    }

    /**
     * POST API，返回 JSON
     *
     * @param url    请求目标地址
     * @param params 请求参数，可以是 <pre>byte[]、String、Map<String, Object></pre> 类型，实际表示了表单数据 KeyValue 的请求数据
     * @param fn     自定义 HTTP 头的时候可设置，可选的
     * @return 响应的 JSON，Map 格式
     */
    public static Map<String, Object> api(String url, Object params, Consumer<HttpURLConnection> fn) {
        ResponseEntity resp = post(url, params, fn);

        return ResponseHandler.toJson(resp);
    }

    /**
     * POST API，返回 JSON
     *
     * @param url    请求目标地址
     * @param params 请求参数，可以是 <pre>byte[]、String、Map<String, Object></pre> 类型，实际表示了表单数据 KeyValue 的请求数据
     * @return 响应的 JSON，Map 格式
     */
    public static Map<String, Object> api(String url, Object params) {
        return api(url, params, null);
    }

    /**
     * POST API，返回 XML
     *
     * @param url    请求目标地址
     * @param params 请求参数，可以是 <pre>byte[]、String、Map<String, Object></pre> 类型，实际表示了表单数据 KeyValue 的请求数据
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
     * @param params 请求参数，可以是 <pre>byte[]、String、Map<String, Object></pre> 类型，实际表示了表单数据 KeyValue 的请求数据
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

    private static String FIELD = BOUNDARY_PREFIX + BOUNDARY + NEWLINE + "Content-Disposition: form-data;name=\"%s\";filename=\"%s\"" + NEWLINE + "Content-Type:%s" + NEWLINE
            + NEWLINE;

    // 定义最后数据分隔线，即--加上BOUNDARY再加上--。
    private static byte[] END_DATA = (NEWLINE + BOUNDARY_PREFIX + BOUNDARY + BOUNDARY_PREFIX + NEWLINE).getBytes();

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

                _bytes = StreamHelper.concat(field.getBytes(), FileHelper.openAsByte(file));
            } else { // 普通字段
                String field = String.format(DIV_FIELD, BOUNDARY, key, v.toString());
                _bytes = field.getBytes();
            }

            if (bytes == null) // 第一次时候为空
                bytes = _bytes;
            else
                bytes = StreamHelper.concat(bytes, _bytes);
        }

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
        return post(url, toFromData(data), conn -> conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY));
    }
}
