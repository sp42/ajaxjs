/*
  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
  the License. You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
  an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
  specific language governing permissions and limitations under the License.

  Copyright 2016 the original author or authors.
 */
package com.ajaxjs.s3client.signer_v4;

import com.ajaxjs.s3client.util.URLEncoding;
import lombok.Data;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * 用于生成签名的规范请求格式
 */
@Data
public class CanonicalRequest {
    private static final String S3_SERVICE = "s3";

    private static final char QUERY_PARAMETER_SEPARATOR = '&';

    private static final char QUERY_PARAMETER_VALUE_SEPARATOR = '=';

    private String service;

    private String method;

    private String path;

    /**
     * 原始查询字符串，可能为 null 或空
     */
    private String query;

    public CanonicalRequest(String method, URI uri) {
        this.method = method;
        this.path = uri.getRawPath();
        this.query = uri.getRawQuery();
    }

    public CanonicalRequest(String method, String pathAndQuery) {
        this.method = method;
        int queryStart = pathAndQuery.indexOf('?');

        if (queryStart >= 0) {
            this.path = pathAndQuery.substring(0, queryStart);
            this.query = pathAndQuery.substring(queryStart + 1);
        } else {
            this.path = pathAndQuery;
            this.query = null;
        }
    }

    /**
     * 获取规范化的路径字符串。
     * 此方法主要用于处理和转换输入路径，使其符合特定服务（如AWS S3）的要求或规范。
     * 对于空路径或null，将返回根路径"/"。
     * 对于S3服务，路径将不会被规范化处理，直接返回编码后的路径。
     * 对于其他服务，将对路径进行编码，并去除多余的斜杠等，使其规范化。
     *
     * @return 规范化后的路径字符串。对于S3服务，返回编码后的原始路径；对于其他情况，返回规范化处理后的路径。
     */
    public String getNormalizePath() {
        if (path == null || path.isEmpty()) return "/";
        String encoded = URLEncoding.encodePath(path);  // 对路径进行URL编码，以符合AWS的要求

        if (S3_SERVICE.equals(service))
            /*
             * 对于S3请求，不进行路径规范化处理。
             * 详情见：http://docs.aws.amazon.com/AmazonS3/latest/API/sig-v4-header-based-auth.html#canonical-request
             */
            return encoded;

        // 对路径进行规范化处理，例如处理"/foo/..", "/./", "/foo//bar/"等
        try {
            // 通过给路径添加"http://"前缀，来确保路径如"//"这样的形式也被认为是语法正确的
            return new URI("http://" + encoded).normalize().getRawPath();
        } catch (URISyntaxException e) {
            // 如果编码后的路径语法不正确，抛出非法状态异常
            throw new IllegalStateException("The encoded path '" + path + "' was deemed syntactically incorrect;" + " there is probably an internal issue with the encoding algorithm");
        }
    }

    /**
     * 标准化查询字符串
     * 此方法将给定的原始查询字符串规范化，按参数名称排序，并确保参数值被正确 URL 编码
     *
     * @return 规范化后的查询字符串。如果输入为 null 或空，则返回空字符串。
     */
    public String getNormalizeQuery() {
        if (query == null || query.isEmpty()) // 检查输入查询字符串是否为空，如果是，则直接返回空字符串
            return "";

        List<Parameter> parameters = extractQueryParameters(query);  // 从原始查询字符串中提取参数列表

        /*
         * 按参数名称字典序对参数列表进行排序
         * Sort query parameters. Simply sort lexicographically by character
         * code, which is equivalent to comparing code points (as mandated by AWS)
         */
//        parameters.sort((l, r) -> l.name.compareTo(r.name));
        parameters.sort(Comparator.comparing(Parameter::getName));

        StringBuilder builder = new StringBuilder();
        boolean first = true;

        for (Parameter parameter : parameters) { // 遍历排序后的参数列表，构造规范化后的查询字符串
            if (first)  // 为每个参数添加分隔符，除了第一个参数
                first = false;
            else builder.append(QUERY_PARAMETER_SEPARATOR);

            String value = parameter.getValue();

            if (value == null) value = ""; // No value => use an empty string as per the spec 如果参数没有值，则使用空字符串作为规范要求

            // 构造编码后的参数名称和值，使用 URL 编码
            builder.append(URLEncoding.encodeQueryComponent(parameter.getName())).append(QUERY_PARAMETER_VALUE_SEPARATOR).append(URLEncoding.encodeQueryComponent(value));
        }

        return builder.toString(); // 返回构建完成的规范化查询字符串
    }

    /**
     * Extract parameters from a query string, preserving encoding.
     * <p>
     * We can't use Apache HTTP Client's URLEncodedUtils.parse, mainly because
     * we don't want to decode names/values.
     *
     * @param rawQuery the query to parse
     * @return The list of parameters, in the order they were found.
     */
    private static List<Parameter> extractQueryParameters(String rawQuery) {
        List<Parameter> results = new ArrayList<>();
        int endIndex = rawQuery.length() - 1;
        int index = 0;

        while (0 <= index && index <= endIndex) {
            /*
             * Ideally we should first look for '&', then look for '=' before
             * the '&', but obviously that's not how AWS understand query
             * parsing; see the test "post-vanilla-query-nonunreserved" in the
             * test suite. A string such as "?foo&bar=qux" will be understood as
             * one parameter with name "foo&bar" and value "qux". Don't ask me
             * why.
             */
            String name;
            String value;
            int nameValueSeparatorIndex = rawQuery.indexOf(QUERY_PARAMETER_VALUE_SEPARATOR, index);

            if (nameValueSeparatorIndex < 0) {
                // No value
                name = rawQuery.substring(index);
                value = null;

                index = endIndex + 1;
            } else {
                int parameterSeparatorIndex = rawQuery.indexOf(QUERY_PARAMETER_SEPARATOR, nameValueSeparatorIndex);
                if (parameterSeparatorIndex < 0) parameterSeparatorIndex = endIndex + 1;

                name = rawQuery.substring(index, nameValueSeparatorIndex);
                value = rawQuery.substring(nameValueSeparatorIndex + 1, parameterSeparatorIndex);

                index = parameterSeparatorIndex + 1;
            }

            results.add(new Parameter(name, value));
        }

        return results;
    }

    @Data
    public static class Parameter {
        private final String name;

        private final String value;

        public Parameter(String name, String value) {
            this.name = name;
            this.value = value;
        }
    }
}
