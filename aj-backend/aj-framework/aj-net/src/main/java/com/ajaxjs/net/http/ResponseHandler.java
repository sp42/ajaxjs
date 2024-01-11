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

import com.ajaxjs.util.convert.EntityConvert;
import com.ajaxjs.util.convert.MapTool;
import com.ajaxjs.util.io.FileHelper;
import com.ajaxjs.util.io.StreamHelper;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;

/**
 * 最初结果为 InputStream，怎么处理？这里提供一些常见的处理手段。
 *
 * @author Frank Cheung
 */
@Slf4j
public abstract class ResponseHandler {
    private static final int MAX_LENGTH_TO_PRINT = 500;

    /**
     * 响应数据转换为文本
     *
     * @param resp 响应消息体
     * @return 响应消息体，但是已经有响应文本在内
     */
    public static ResponseEntity stream2Str(ResponseEntity resp) {
        if (resp.getIn() != null) {
            String result = StreamHelper.byteStream2string(resp.getIn());
            resp.setResponseText(result.trim());

            String resultMsg = (result.length() > MAX_LENGTH_TO_PRINT) ? result.substring(0, MAX_LENGTH_TO_PRINT) + " ..." : result;
            log.info("{} {} 响应状态：{}，请求结果\n{}", resp.getHttpMethod(), resp.getUrl(), resultMsg, resp.getHttpCode());
        }

        return resp;
    }

    /**
     * 下载文件
     *
     * @param resp     响应消息体
     * @param saveDir  保存的目录
     * @param fileName 保存的文件名
     * @return 下载文件的完整磁盘路径
     */
    public static String download(ResponseEntity resp, String saveDir, String fileName) {
        File file = FileHelper.createFile(saveDir, fileName);

        try (OutputStream out = Files.newOutputStream(file.toPath())) {
            StreamHelper.write(resp.getIn(), out, true);
            log.info("文件 [{}]写入成功", file);

            return file.toString();
        } catch (IOException e) {
            log.warn("ERROR>>", e);
        } finally {
            try {
                resp.getIn().close();
            } catch (IOException e) {
                log.warn("ERROR>>", e);
            }
        }

        return null;
    }

    /**
     * 把结果转换为 JSON 对象
     *
     * @param resp 响应消息体
     * @return JSON（Map 格式）
     */
    public static List<Map<String, Object>> toJsonList(ResponseEntity resp) {
        List<Map<String, Object>> list = null;

        if (resp.isOk()) {
            try {
                list = EntityConvert.json2MapList(resp.toString());
            } catch (Exception e) {
                log.warn("解析 JSON 时候发生异常", e);
            }
        } else {
            // TODO 列表如何返回错误信息？
            Map<String, Object> map;

            if (resp.getEx() != null) {
                map = new HashMap<>();
                map.put(Base.ERR_MSG, resp.getEx().getMessage());
            } else map = EntityConvert.json2map(resp.getResponseText());

            list = new ArrayList<>();
            list.add(map);
        }

        return list;
    }

    /**
     * 把结果转换为 JSON 对象
     *
     * @param resp 响应消息体
     * @return JSON（Map 格式）
     */
    public static Map<String, Object> toJson(ResponseEntity resp) {
        Map<String, Object> map = null;

        if (resp.isOk()) {
            try {
                map = EntityConvert.json2map(resp.toString());
            } catch (Exception e) {
                log.warn("解析 JSON 时候发生异常", e);
            }
        } else {
            if (resp.getEx() != null) {
                map = new HashMap<>();
                map.put(Base.ERR_MSG, resp.getEx().getMessage());
            } else map = EntityConvert.json2map(resp.getResponseText());
        }

        return map;
    }

    /**
     * 把结果转换为 XML 对象
     *
     * @param resp 响应消息体
     * @return XML（Map 格式）
     */
    public static Map<String, String> toXML(ResponseEntity resp) {
        Map<String, String> map = null;

        if (resp.isOk()) {
            try {
                map = MapTool.xmlToMap(resp.toString());
            } catch (Exception e) {
                log.warn("解析 XML 时候发生异常", e);
            }
        } else {
            if (resp.getEx() != null) {
                map = new HashMap<>();
                map.put(Base.ERR_MSG, resp.getEx().getMessage());
            } else map = MapTool.xmlToMap(resp.getResponseText());
        }

        return map;
    }

    /**
     * 将 ResponseEntity 响应对象转换为指定类型的对象
     *
     * @param resp ResponseEntity 响应对象
     * @param clz  要转换为的类型
     * @param <T>  转换后的类型
     * @return 转换后的对象
     */
    public static <T> T toBean(ResponseEntity resp, Class<T> clz) {
        return EntityConvert.map2Bean(toJson(resp), clz);
    }

    /**
     * 判断是否为 GZip 格式的输入流并返回相应的输入流
     * 有些网站强制加入 Content-Encoding:gzip，而不管之前的是否有 GZip 的请求
     *
     * @param conn HTTP 连接
     * @param in   输入流
     * @return 如果Content-Encoding为gzip，则返回  GZIPInputStream 输入流，否则返回 null
     */
    public static InputStream gzip(HttpURLConnection conn, InputStream in) {
        if ("gzip".equals(conn.getHeaderField("Content-Encoding"))) {
            try {
                return new GZIPInputStream(in);
            } catch (IOException e) {
                log.warn("ERROR>>", e);
            }
        }

        return null;
    }

}
