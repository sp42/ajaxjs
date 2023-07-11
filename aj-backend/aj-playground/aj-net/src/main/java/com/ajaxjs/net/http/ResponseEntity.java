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

import lombok.Data;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.Map;

/**
 * 响应消息体
 */
@Data
public class ResponseEntity {
    /**
     * 返回响应文本结果
     */
    @Override
    public String toString() {
        return ResponseHandler.stream2Str(this).getResponseText();
    }

    /**
     * 连接对象
     */
    private HttpURLConnection connection;

    /**
     * 请求地址
     */
    private String url;

    /**
     * 请求方法
     */
    private String httpMethod;

    /**
     * 请求参数
     */
    private Map<String, Object> params;

    /**
     * 是否成功（http 200 即表示成功，4xx/500x 表示不成功）
     */
    private boolean isOk;

    /**
     * 程序异常，比 HTTP 请求靠前，例如非法网址，或者 dns 不存在的 UnknownHostException
     */
    private Exception ex;

    /**
     * HTTP 状态码
     */
    private Integer httpCode;

    /**
     * 响应消息字符串
     */
    private String responseText;

    /**
     * 结果的流
     */
    private InputStream in;
}
