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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;

import com.ajaxjs.util.io.FileHelper;
import com.ajaxjs.util.io.StreamHelper;
import com.ajaxjs.util.logger.LogHelper;
import com.ajaxjs.util.map.JsonHelper;
import com.ajaxjs.util.map.MapTool;

/**
 * 最初结果为 InputStram，怎么处理？这里提供一些常见的处理手段。
 *
 * @author Frank Cheung
 */
public abstract class ResponseHandler {
	private static final LogHelper LOGGER = LogHelper.getLog(ResponseHandler.class);

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

			int maxLength = 500;
			String reslutMsg = (result.length() > maxLength) ? result.substring(0, maxLength) + " ..." : result;
			LOGGER.info("{0} {1} 响应状态：{3}，请求结果\n{2}", resp.getHttpMethod(), resp.getUrl(), reslutMsg, resp.getHttpCode());
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

		try (OutputStream out = new FileOutputStream(file)) {
			StreamHelper.write(resp.getIn(), out, true);
			LOGGER.info("文件 [{0}]写入成功", file.toString());

			return file.toString();
		} catch (IOException e) {
			LOGGER.warning(e);
		} finally {
			try {
				resp.getIn().close();
			} catch (IOException e) {
				LOGGER.warning(e);
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
				list = JsonHelper.parseList(resp.toString());
			} catch (Exception e) {
				LOGGER.warning(e, "解析 JSON 时候发生异常");
			}
		} else {
			// TODO 列表如何返回错误信息？
			Map<String, Object> map;
			if (resp.getEx() != null) {
				map = new HashMap<>();
				map.put(Base.ERR_MSG, resp.getEx().getMessage());
			} else {
				map = JsonHelper.parseMap(resp.getResponseText());
			}

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
				map = JsonHelper.parseMap(resp.toString());
			} catch (Exception e) {
				LOGGER.warning(e, "解析 JSON 时候发生异常");
			}
		} else {
			if (resp.getEx() != null) {
				map = new HashMap<>();
				map.put(Base.ERR_MSG, resp.getEx().getMessage());
			} else {
				map = JsonHelper.parseMap(resp.getResponseText());
			}
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
				LOGGER.warning(e, "解析 XML 时候发生异常");
			}
		} else {
			if (resp.getEx() != null) {
				map = new HashMap<>();
				map.put(Base.ERR_MSG, resp.getEx().getMessage());
			} else {
				map = MapTool.xmlToMap(resp.getResponseText());
			}
		}

		return map;
	}

	/**
	 * @param <T>
	 * @param resp
	 * @param clz
	 * @return
	 */
	public static <T> T toBean(ResponseEntity resp, Class<T> clz) {
		return MapTool.map2Bean(toJson(resp), clz);
	}

	/**
	 * 有些网站强制加入 Content-Encoding:gzip，而不管之前的是否有 GZip 的请求
	 *
	 * @param conn
	 * @param in
	 * @return
	 */
	public static InputStream gzip(HttpURLConnection conn, InputStream in) {
		if ("gzip".equals(conn.getHeaderField("Content-Encoding"))) {
			try {
				return new GZIPInputStream(in);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return null;
	}
}
