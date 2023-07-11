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
import java.util.function.Consumer;

/**
 * DELETE 请求
 *
 * @author Frank Cheung
 */
public class Delete extends Base {
    /**
     * DELETE 请求
     *
     * @param url 请求目标地址
     * @param fn  自定义 HTTP 头的时候可设置，可选的
     * @return 响应消息体
     */
    public static ResponseEntity del(String url, Consumer<HttpURLConnection> fn) {
        return connect(url, "DELETE", fn);
    }

    /**
     * DELETE 请求
     *
     * @param url 请求目标地址
     * @return 响应消息体
     */
    public static ResponseEntity del(String url) {
        return del(url, null);
    }

    /**
     * DELETE API 请求，返回 JSON
     *
     * @param url 请求目标地址
     * @param fn  自定义 HTTP 头的时候可设置，可选的
     * @return 响应的 JSON，Map 格式
     */
    public static Map<String, Object> api(String url, Consumer<HttpURLConnection> fn) {
        return ResponseHandler.toJson(del(url, fn));
    }

    /**
     * DELETE API 请求，返回 JSON
     *
     * @param url 请求目标地址
     * @return 响应的 JSON，Map 格式
     */
    public static Map<String, Object> api(String url) {
        return ResponseHandler.toJson(del(url));
    }
}
