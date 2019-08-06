/**
 * Copyright 2015 Sp42 frank@ajaxjs.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ajaxjs.net.http;

import java.io.File;
import java.util.Map;

import com.ajaxjs.util.io.StreamUtil;
import com.ajaxjs.util.logger.LogHelper;

/**
 * 远程请求器
 * 
 * @author Sp42 frank@ajaxjs.com
 *
 */
public class Client  {
	private static final LogHelper LOGGER = LogHelper.getLog(Client.class);
 

	/**
	 * request 头和上传文件内容之间的分隔符
	 */
	private static final String BOUNDARY = "---------------------------123821742118716";

	/**
	 * 多段 POST 的分隔
	 */
//	private static final String DIV = "\r\n--%s\r\nContent-Disposition: form-data; name=\"%s\"\r\n\r\n%s";

	/**
	 * 模拟多段上传 TODO
	 * 
	 * @param url 请求目标地址
	 * @param text 文本数据
	 * @param fileMap 二进制（文件）数据
	 * @return 携带请求信息的 Bean
	 */
	public static String multiPOST(String url, Map<String, Object> text, Map<String, String> fileMap) {
		byte[] data = null;

 

		if (fileMap != null && fileMap.size() > 0) {
			for (String name : fileMap.keySet()) {
				String value = fileMap.get(name);

				File file = new File(value);
				String filename = file.getName();

				// String contentType = FileUtil.getMime(file);
				// String str = String.format(DIV, BOUNDARY, name, filename) + "Content-Type:" +
				// contentType + "\r\n\r\n";

				StringBuffer strBuf = new StringBuffer();
				strBuf.append("\r\n").append("--").append(BOUNDARY).append("\r\n");
				strBuf.append("Content-Disposition: form-data; name=\"" + "ddd" + "\"; filename=\"" + filename + "\"\r\n");
				// strBuf.append("Content-Type:" + contentType + "\r\n\r\n");
				// out.write(strBuf.toString().getBytes());

				StreamUtil.concat(data, strBuf.toString().getBytes());
				StreamUtil.concat(data, StreamUtil.fileAsByte(file));
			}
		}

		StreamUtil.concat(data, ("\r\n--" + BOUNDARY + "--\r\n").getBytes());

//		Client client = new Client(url);
//		client.setData(data);
//		client.getConnection().setDoOutput(true); // for conn.getOutputStream().write(someBytes);
//		client.getConnection().setDoInput(true);
//		client.getConnection().setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);
//
//		try {
//			client.connect();
//		} catch (ConnectException e) {
//			LOGGER.warning(e);
//			return null;
//		}
//
//		client.setDone(true);

//		return client.getContent();
		return null;
	}
}