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

import lombok.Data;

import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Canonical Headers.
 * <p>
 * See <a href="http://docs.aws.amazon.com/general/latest/gr/sigv4-create-canonical-request.html">...</a> for more information
 * </p>
 */
@Data
public class CanonicalHeaders {
    private String names;

    private final String canonicalizedHeaders;

    private final TreeMap<String, List<String>> internalMap;

    public CanonicalHeaders(String names, String canonicalizedHeaders, TreeMap<String, List<String>> internalMap) {
        this.names = names;
        this.canonicalizedHeaders = canonicalizedHeaders;
        this.internalMap = internalMap;
    }

    public Optional<String> getFirstValue(String name) {
        return Optional.ofNullable(internalMap.get(name.toLowerCase())).map(values -> values.get(0));
    }

    private static final Collector<CharSequence, ?, String> HEADER_VALUE_COLLECTOR = Collectors.joining(",");

    /**
     * 构建并返回规范化的头信息对象。
     * 此方法将内部维护的头信息映射表转换为规范化的头信息字符串，其中头名称全部转换为小写，并按照字典序排序。
     * 头值也被规范化，并且多个相同的头名会被合并为一个，值之间用逗号分隔。
     *
     * @return CanonicalHeaders 返回构建好的规范头信息对象。
     */
    public static CanonicalHeaders build(Map<String, String> map) {
        TreeMap<String, List<String>> internalMap = new TreeMap<>();

        map.forEach((name, value) -> {
            String lowerCaseName = name.toLowerCase();// 将 name 转换为小写形式，以保证键的大小写不敏感

            // 尝试获取键对应的值列表，如果不存在则创建新的值列表，并添加值
            internalMap.put(lowerCaseName, Optional.ofNullable(internalMap.get(lowerCaseName)).map(values -> {
                values.add(value); // 如果值列表存在，将新值添加到列表中
                return values;
            }).orElse(newValueListWithValue(value))); // 如果值列表不存在，创建并返回一个新的值列表
        });

        // 将所有头部名称转换为小写并以分号连接，用于后续的比对和处理
        String names = internalMap.keySet().stream().map(String::toLowerCase).collect(Collectors.joining(";"));
        StringBuilder sb = new StringBuilder();

        // 遍历内部映射表，将每个头部名称和值规范化后追加到 StringBuilder 中
        // 每个头部以换行符结束
        internalMap.forEach((key, value) -> sb
                .append(key.toLowerCase()) // 规范化头部名称
                .append(':')
                .append(value.stream().map(CanonicalHeaders::normalizeHeaderValue).collect(HEADER_VALUE_COLLECTOR))// 规范化头部值
                .append('\n'));

        return new CanonicalHeaders(names, sb.toString(), internalMap); // 创建并返回规范化的头信息对象
    }

    private static List<String> newValueListWithValue(String value) {
        List<String> values = new ArrayList<>();
        values.add(value);

        return values;
    }

    /**
     * 标准化 HTTP 头字段值。
     * 该方法主要用于处理多行值，将它们处理为单个值，并且去除边缘的空格以及内部的连续空格。
     * 注意：AWS 测试套件期望我们按照非规范的方式处理多行值中的每一行作为单独的值。
     *
     * @param value 待标准化的 HTTP 头字段值，可能包含多行。
     * @return 标准化后的 HTTP 头字段值，行将被合并，且边缘空格和内部连续空格被移除。
     */
    private static String normalizeHeaderValue(String value) {
        /*
         * 将字符串按行分割成流
         * Strangely, the AWS test suite expects us to handle lines in
         * multi-line values as individual values, even though this is not
         * mentioned in the specs.
         */
        Stream<String> stream = Arrays.stream(value.split("\n"));
        stream = stream.map(String::trim);   // 去除字符串边缘的空格 Remove spaces on the edges of the string
        stream = stream.map(s -> s.replaceAll(" +", " ")); // 去除字符串内部的连续空格 Remove duplicate spaces inside the string

        return stream.collect(HEADER_VALUE_COLLECTOR);// 将处理后的行收集为一个字符串
    }
}
